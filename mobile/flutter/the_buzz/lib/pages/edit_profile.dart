import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'dart:developer' as developer;
import 'package:the_buzz/models/google_profile.dart';

class EditProfilePage extends StatefulWidget {
  const EditProfilePage({super.key});

  @override
  State<EditProfilePage> createState() => _EditProfilePageState();
}

class _EditProfilePageState extends State<EditProfilePage> {
  String? selectedSexualIdentity;
  String? selectedGenderOrientation;
  final TextEditingController _notesController = TextEditingController();
  late Profile profile;
  bool isSaving = false;

  final List<String> sexualIdentities = [
    'heterosexual',
    'homosexual',
    'bisexual',
    'pansexual'
  ];

  final List<String> genderOrientations = [
    'male',
    'female',
    'nonbinary',
    'cisgender',
    'transgender'
  ];

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

  Future<void> saveProfile() async {
    setState(() {
      isSaving = true;
    });

    try {
      final sharedPreferences = await SharedPreferences.getInstance();
      final userId = profile.googleUserId;
      
      // Debug log the request data
      developer.log('Attempting to update profile for user ID: $userId');
      developer.log('Selected Gender Orientation: $selectedGenderOrientation');
      developer.log('Selected Sexual Identity: $selectedSexualIdentity');
      developer.log('Notes: ${_notesController.text}');

      final requestBody = {
        'userID': userId,
        'genderIdentity': selectedGenderOrientation,
        'sexualOrientation': selectedSexualIdentity,
        'note': _notesController.text,
      };

      developer.log('Request body: ${jsonEncode(requestBody)}');

      // Get the sessionId cookie
      final sessionId = sharedPreferences.getInt('sessionId');
      developer.log('SessionId from SharedPreferences: $sessionId');

      final response = await http.put(
        Uri.parse('https://team-dog.dokku.cse.lehigh.edu/profile/$userId'),
        headers: {
          'Content-Type': 'application/json',
          'Cookie': 'sessionId=$sessionId',
        },
        body: jsonEncode(requestBody),
      );

      // Debug log the response
      developer.log('Response status code: ${response.statusCode}');
      developer.log('Response body: ${response.body}');

      if (response.statusCode == 200) {
        final responseData = jsonDecode(response.body);
        developer.log('Decoded response data: $responseData');
        
        if (responseData['mStatus'] == 'ok') {
          if (!mounted) return;
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Profile updated successfully')),
          );
          Navigator.pop(context, true);
        } else {
          throw Exception(responseData['mMessage'] ?? 'Unknown error occurred');
        }
      } else {
        throw Exception('Failed to update profile: Status ${response.statusCode}');
      }
    } catch (e, stackTrace) {
      // Log both error and stack trace
      developer.log('Error updating profile', error: e, stackTrace: stackTrace);
      
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error updating profile: $e')),
      );
    } finally {
      setState(() {
        isSaving = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color.fromARGB(255, 238, 243, 242),
      appBar: AppBar(
        title: const Row(
          children: [
            SizedBox(width: 65),
            Align(
              alignment: Alignment.centerRight,
              child: Text(
                'Edit Profile',
                style: TextStyle(fontSize: 28, color: Colors.pink),
              ),
            ),
          ],
        ),
        backgroundColor: Theme.of(context).secondaryHeaderColor,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Sexual Identity',
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            Container(
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(8),
              ),
              child: DropdownButtonFormField<String>(
                value: selectedSexualIdentity,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  contentPadding: EdgeInsets.symmetric(horizontal: 16),
                ),
                items: sexualIdentities.map((String identity) {
                  return DropdownMenuItem<String>(
                    value: identity,
                    child: Text(identity),
                  );
                }).toList(),
                onChanged: (String? newValue) {
                  setState(() {
                    selectedSexualIdentity = newValue;
                  });
                },
              ),
            ),
            const SizedBox(height: 24),
            const Text(
              'Gender Orientation',
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            Container(
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(8),
              ),
              child: DropdownButtonFormField<String>(
                value: selectedGenderOrientation,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  contentPadding: EdgeInsets.symmetric(horizontal: 16),
                ),
                items: genderOrientations.map((String orientation) {
                  return DropdownMenuItem<String>(
                    value: orientation,
                    child: Text(orientation),
                  );
                }).toList(),
                onChanged: (String? newValue) {
                  setState(() {
                    selectedGenderOrientation = newValue;
                  });
                },
              ),
            ),
            const SizedBox(height: 24),
            const Text(
              'Notes',
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            Container(
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(8),
              ),
              child: TextField(
                controller: _notesController,
                maxLines: 4,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  hintText: 'Enter any additional notes...',
                  contentPadding: EdgeInsets.all(16),
                ),
              ),
            ),
            const SizedBox(height: 32),
            Center(
              child: ElevatedButton(
                onPressed: isSaving ? null : saveProfile,
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.pink,
                  padding: const EdgeInsets.symmetric(horizontal: 48, vertical: 16),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(8),
                  ),
                ),
                child: isSaving
                    ? const CircularProgressIndicator(color: Colors.white)
                    : const Text(
                        'Save',
                        style: TextStyle(fontSize: 18, color: Colors.white),
                      ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  @override
  void dispose() {
    _notesController.dispose();
    super.dispose();
  }
}