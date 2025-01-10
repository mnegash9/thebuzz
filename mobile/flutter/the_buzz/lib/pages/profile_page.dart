import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';
import 'dart:developer' as developer;
import 'package:the_buzz/models/google_profile.dart';
import 'package:http/http.dart' as http;
import 'package:the_buzz/net/web_requests.dart';
import 'dart:async';
import 'package:the_buzz/pages/googe_login.dart';
import 'package:the_buzz/pages/edit_profile.dart';
/// Displays and manages user profile information.
/// 
/// Shows user details including name and email, and provides options
/// to edit profile information or log out.
class ProfilePage extends StatefulWidget {
  const ProfilePage({super.key});

  @override
  State<ProfilePage> createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  late Profile profile = const Profile(
    googleUserId: 0,
    googlePictureUrl: "Unknown",
    googleSessionId: 0,
    googleGivenName: "N/A",
    googleFamilyName: "N/A",
    googleEmail: "N/A",
  );

  @override
  void initState() {
    super.initState();
    loadProfileData();
  }

  Future<void> loadProfileData() async {
    final sharedPreferences = await SharedPreferences.getInstance();
    String? profileData = sharedPreferences.getString('profileData');

    if (profileData != null) {
      setState(() {
        profile = Profile.fromJson(jsonDecode(profileData));
      });
    }
  }

  Future<bool> logout(http.Client client) async {
    // Implement logout functionality
    return true;
  }

  Future<void> handleLogout(BuildContext context) async {
    final success = await logout(http.Client());
    if (success) {
      // Navigate to SignIn page and remove all previous routes
      if (!context.mounted) return;
      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (context) => const SignIn()),
        (Route<dynamic> route) => false,
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color.fromARGB(255, 238, 243, 242),
      appBar: AppBar(
        title: const Row(
          children: [
            SizedBox(
              width: 100,
            ),
            Align(
                alignment: Alignment.centerRight,
                child: Text(
                  'Profile',
                  style: TextStyle(fontSize: 28, color: Colors.pink),
                )),
          ],
        ),
        backgroundColor: Theme.of(context).secondaryHeaderColor,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            const CircleAvatar(
              child: Icon(Icons.person),
            ),
            const SizedBox(height: 16),
            Text(
              'Name: ${profile.googleGivenName} ${profile.googleFamilyName}',
              style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            Text(
              'Email: ${profile.googleEmail}',
              style: const TextStyle(fontSize: 18),
            ),
            const SizedBox(height: 8),
            // Logout Button at the bottom
            ElevatedButton(
  onPressed: () async {
    final result = await Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => const EditProfilePage()),
    );
    if (result == true) {
      // Reload profile data if edit was successful
      loadProfileData();
    }
  },
  style: ElevatedButton.styleFrom(
    backgroundColor: Colors.pink,
    padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 12),
    shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(8),
    ),
  ),
  child: const Text(
    'Edit Profile',
    style: TextStyle(fontSize: 16, color: Colors.white),
  ),
),
const SizedBox(height: 16),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                SizedBox(
                  width: 200,
                  height: 50,
                  child: GestureDetector(
                    onTap: () => handleLogout(context),
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
    );
  }
}