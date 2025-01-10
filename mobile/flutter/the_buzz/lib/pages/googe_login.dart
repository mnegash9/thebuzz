import 'dart:async';
import 'dart:convert';
import 'dart:developer' as developer;
import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:http/http.dart' as http;
import 'package:the_buzz/main.dart';
import 'package:the_buzz/models/google_profile.dart';
import 'package:shared_preferences/shared_preferences.dart';

const String url = "https://team-dog.dokku.cse.lehigh.edu";

const List<String> scopes = <String>[
  'email',
  'https://www.googleapis.com/auth/userinfo.profile'
];

GoogleSignIn googleSignIn = GoogleSignIn(
  serverClientId: "209952242135-jpmrhec8pqu40o26hh70r73lfq9btp7k.apps.googleusercontent.com",
  scopes: scopes,
);
/// Handles Google Sign-In authentication for the application.
/// 
/// Manages the Google Sign-In process, token handling, and user profile data
/// storage using shared preferences.
class SignIn extends StatefulWidget {
  const SignIn({super.key});

  @override
  SignInAttempt createState() => SignInAttempt();
}
/// State class for the SignIn widget that implements the sign-in logic.
class SignInAttempt extends State<SignIn> {
  /// Initiates the Google Sign-In process and handles authentication.
  /// 
  /// On successful sign-in, saves the user profile data and navigates
  /// to the home page.
  Future<void> handleSignIn() async {
    try {
      final GoogleSignInAccount? googleAccount = await googleSignIn.signIn();

      if (googleAccount != null) {
        final GoogleSignInAuthentication accountAuth = await googleAccount.authentication;

        developer.log('Google Account: ${googleAccount.email}');
        developer.log('Google Account Auth: ${accountAuth.idToken}');

        String? idToken = accountAuth.idToken;

        await getUserInfo(idToken);
        if (idToken != null) {
          developer.log('ID Token: $idToken');
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(builder: (context) => const MyHomePage(title: "The Buzz")),
          );
        } else {
          developer.log('ID token is null');
        }
      }
    } catch (error, stackTrace) {
      developer.log('Error during sign-in: $error', error: error, stackTrace: stackTrace);
    }
  }

  Future<void> saveProfileData(Profile profile) async {
    final sharedPreferences = await SharedPreferences.getInstance();
    final profileJson = jsonEncode(profile.toJson());
    await sharedPreferences.setString('profileData', profileJson);
    
    developer.log('Profile data saved: $profileJson');  // Log the saved data
  }

  Future<void> getUserInfo(String? idToken) async {
    if (idToken == null) {
      developer.log('ID token is null');
      return;
    }

    final response = await http.post(
      Uri.parse("$url/login"),
      headers: {
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'jwtToken': idToken,
      }),
    );

    if (response.statusCode == 200) {
      final returnData = jsonDecode(response.body);
      if (returnData['mStatus'] == 'ok' && returnData['mData'] != null) {
        final userData = returnData['mData'];
        final profile = Profile.fromJson(userData);

        final sharedPreferences = await SharedPreferences.getInstance();
        await sharedPreferences.setInt('sessionId', returnData['mData']['sessionId']);
        await sharedPreferences.setString('profileData', jsonEncode(profile.toJson()));
        await sharedPreferences.setBool('isLoggedIn', true);
        await sharedPreferences.setString('jwtToken', idToken);

        developer.log('User data and session cached successfully');
      }
    }
  }

  Future<Profile?> getCachedProfile() async {
    final sharedPreferences = await SharedPreferences.getInstance();
    final String? profileJson = sharedPreferences.getString('profileData');
    
    if (profileJson != null) {
      return Profile.fromJson(jsonDecode(profileJson));
    }
    return null;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color.fromRGBO(238, 243, 242, 1),
      body: SafeArea(
        child: Center(
          child: Column(
            children: [
              const SizedBox(height: 300),
              const Text(
                'Welcome to the Buzz',
                style: TextStyle(fontSize: 28, color: Colors.pink),
              ),
              const SizedBox(height: 20),
              const Text(
                'Please Sign In',
                style: TextStyle(fontSize: 28, color: Colors.pink),
              ),
              const SizedBox(height: 50),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  SizedBox(
                    width: 200,
                    height: 50,
                    child: ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        shape: const CircleBorder(),
                        padding: EdgeInsets.zero,
                      ),
                      onPressed: handleSignIn,
                      child: ClipRRect(
                        borderRadius: BorderRadius.circular(20),
                        child: Container(
                          decoration: const BoxDecoration(
                            color: Colors.white,
                          ),
                          alignment: Alignment.center,
                          child: const Icon(
                            Icons.login,
                            color: Colors.pink,
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
