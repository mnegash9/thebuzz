import 'package:http/http.dart' as http;
import 'package:the_buzz/models/comment_object.dart';
import 'package:the_buzz/models/dashboard.dart';
import 'package:the_buzz/models/idea_object.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import 'package:the_buzz/pages/googe_login.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:the_buzz/services/auth_service.dart';

const String url = "https://team-dog.dokku.cse.lehigh.edu";

Future<List<Idea>> fetchIdeas(http.Client client) async {
  final SharedPreferences prefs = await SharedPreferences.getInstance();
  final String? etagValue = prefs.getString('ideas_etag');
  
  final headers = <String, String>{
    'If-None-Match': etagValue ?? '',
  };

  final response = await client.get(
    Uri.parse("$url/ideas"),
    headers: headers,
  );

  if (response.statusCode == 304) {
    // Not modified, use cached data
    final String? cachedData = prefs.getString('ideas_cache');
    if (cachedData != null) {
      final List<Idea> returnData;
      var res = jsonDecode(cachedData);
      
      if (res is List) {
        returnData = (res).map((x) => Idea.fromJson(x)).toList();
      } else if (res is Map) {
        List<dynamic> mData = res['mData'];
        returnData = mData.map((x) => Idea.fromJson(x)).toList();
        returnData.sort((a, b) => a.theIdeaId.compareTo(b.theIdeaId));
      } else {
        returnData = List.empty();
      }
      return returnData;
    }
  }

  if (response.statusCode == 200) {
    // Save ETag if provided
    final newEtag = response.headers['etag'];
    if (newEtag != null) {
      await prefs.setString('ideas_etag', newEtag);
    }

    // Cache the response data
    await prefs.setString('ideas_cache', response.body);

    final List<Idea> returnData;
    var res = jsonDecode(response.body);

    if (res is List) {
      returnData = (res).map((x) => Idea.fromJson(x)).toList();
    } else if (res is Map) {
      List<dynamic> mData = res['mData'];
      returnData = mData.map((x) => Idea.fromJson(x)).toList();
      returnData.sort((a, b) => a.theIdeaId.compareTo(b.theIdeaId));
    } else {
      returnData = List.empty();
    }
    return returnData;
  }

  throw Exception('Failed to load ideas');
}

Future<bool> postIdea(String idea, String? base64Image, http.Client client) async {
  final sharedPreferences = await SharedPreferences.getInstance();
  final int? sessionId = sharedPreferences.getInt('sessionId');

  if (sessionId == null) {
    developer.log('Session ID is missing. User is not logged in.');
    return false;
  }

  final Map<String, dynamic> data = {
    'mIdea': idea,
  };

  // Add image data if provided
  if (base64Image != null && base64Image.isNotEmpty) {
    data['file'] = base64Image;
  }

  final response = await client.post(
    Uri.parse("$url/ideas"),
    headers: {
      'Content-Type': 'application/json',
      'Cookie': 'sessionId=$sessionId',
    },
    body: jsonEncode(data),
  );

  if (response.statusCode == 200) {
    final responseData = jsonDecode(response.body);
    if (responseData['mStatus'] == 'ok') {
      developer.log('Idea posted successfully');
      return true;
    } else {
      developer.log('Failed to post idea: ${responseData['mMessage']}');
      return false;
    }
  } else {
    developer.log('Failed to post idea. Status: ${response.statusCode}');
    return false;
  }
}

Future<bool> postComment(int ideaId, String comment, String? base64Image, http.Client client) async {
  final sharedPreferences = await SharedPreferences.getInstance();
  final int? sessionId = sharedPreferences.getInt('sessionId');

  if (sessionId == null) {
    developer.log('Session ID is missing. User is not logged in.');
    return false;
  }

  final Map<String, dynamic> data = {
    'mIdea': comment,
  };

  // Add image data if provided
  if (base64Image != null && base64Image.isNotEmpty) {
    data['file'] = base64Image;
  }

  final response = await client.post(
    Uri.parse("$url/ideas/${ideaId.toString()}/comments"),
    headers: {
      'Content-Type': 'application/json',
      'Cookie': 'sessionId=$sessionId',
    },
    body: jsonEncode(data),
  );

  if (response.statusCode == 200) {
    final responseData = jsonDecode(response.body);
    if (responseData['mStatus'] == 'ok') {
      developer.log('Comment posted successfully');
      return true;
    } else {
      developer.log('Failed to post comment: ${responseData['mMessage']}');
      return false;
    }
  }
  
  developer.log('Failed to post comment. Status code: ${response.statusCode}');
  return false;
}

Future<bool> upVote(int ideaID, http.Client client) async {
  final sharedPreferences = await SharedPreferences.getInstance();
  final int? sessionId = await sharedPreferences.getInt('sessionId');

  if (sessionId == null) {
    developer.log('Session ID is null. User is not logged in.');
    return false;
  }

  final response = await client.put(
    Uri.parse("$url/ideas/${ideaID.toString()}/upvote"),
    headers: {
      'Content-Type': 'application/json',
      'Cookie': 'sessionId=$sessionId', 
    },
  );

  if (response.statusCode == 200) {
    developer.log('Idea upvoted successfully');
    return true;
  }
  
  developer.log('Failed to upvote idea. Status code: ${response.statusCode}');
  return false;
}

Future<bool> downVote(int ideaID, http.Client client) async {
  final sharedPreferences = await SharedPreferences.getInstance();
  final int? sessionId = sharedPreferences.getInt('sessionId');

  if (sessionId == null) {
    developer.log('Session ID is missing. User is not logged in.');
    return false;
  }

  final response = await client.put(
    Uri.parse("$url/ideas/${ideaID.toString()}/downvote"),
    headers: {
      'Content-Type': 'application/json',
      'Cookie': 'sessionId=$sessionId', 
    },
  );

  if (response.statusCode == 200) {
    developer.log('Idea downvoted successfully');
    return true;
  }
  
  developer.log('Failed to downvote idea. Status code: ${response.statusCode}');
  return false;
}

Future<List<Dashboard>> fetchDashboard(http.Client client) async {
  final response = await client.get(Uri.parse("$url/dashboard"));

  if (response.statusCode == 200) {
    final List<Dashboard> returnData;
    var res = jsonDecode(response.body);
    developer.log('Dashboard response: $res');  // Added for debugging

    if (res is List) {
      returnData = (res).map((x) => Dashboard.fromJson(x)).toList();
    } else if (res is Map) {
      List<dynamic> mData = res['mData'];
      returnData = mData.map((x) => Dashboard.fromJson(x)).toList();
      returnData.sort((a, b) => a.ideaId.compareTo(b.ideaId));
    } else {
      developer.log('ERROR: Unexpected json response type (was not a List or Map).');
      returnData = List.empty();
    }
    return returnData;
  }
  
  throw Exception('Did not receive success status code from request.');
}

Future<List<Comment>> fetchComments(int ideaID, http.Client client) async {
  developer.log('Fetching comments for idea: $ideaID');
  
  final sharedPreferences = await SharedPreferences.getInstance();
  final int? sessionId = sharedPreferences.getInt('sessionId');

  if (sessionId == null) {
    developer.log('Session ID is missing. User is not logged in.');
    throw Exception('User not authenticated');
  }

  try {
    final response = await client.get(
      Uri.parse("$url/comments/$ideaID"),
      headers: {
        'Content-Type': 'application/json',
        'Cookie': 'sessionId=$sessionId',
      },
    );

    developer.log('Response status code: ${response.statusCode}');
    developer.log('Response body: ${response.body}');

    if (response.statusCode == 500) {
      final errorJson = jsonDecode(response.body);
      throw Exception('Server error: ${errorJson['title']} - ${errorJson['details']}');
    }

    if (response.statusCode == 200) {
      final Map<String, dynamic> jsonResponse = jsonDecode(response.body);
      
      if (jsonResponse['mStatus'] == 'ok' && jsonResponse['mData'] != null) {
        final List<dynamic> commentsList = jsonResponse['mData'];
        return commentsList.map((json) => Comment.fromJson(json)).toList();
      } else {
        throw Exception('Invalid response format or error status: ${jsonResponse['mMessage']}');
      }
    }
    
    throw Exception('Failed to fetch comments. Status code: ${response.statusCode}');
  } catch (e) {
    developer.log('Error fetching comments: $e');
    rethrow;
  }
}

Future<bool> logout(http.Client client) async {
  try {
    await AuthService.clearLoginState();
    await googleSignIn.signOut();
    return true;
  } catch (e) {
    developer.log('Error during logout: $e');
    return false;
  }
}

Future<String?> uploadImage(String base64String, http.Client client) async {
  if (base64String.isEmpty) {
    developer.log('No image data provided');
    return null;
  }

  if (base64String.length > 500000) {
    developer.log('Image is too large (max 500KB)');
    return null;
  }

  final sharedPreferences = await SharedPreferences.getInstance();
  final int? sessionId = sharedPreferences.getInt('sessionId');

  if (sessionId == null) {
    developer.log('Session ID is missing. User is not logged in.');
    return null;
  }

  try {
    final Map<String, dynamic> data = {
      'mIdea': '',  // Empty idea text since we're just uploading the image
      'file': base64String
    };

    developer.log('Uploading image...');
    developer.log('Image size: ${base64String.length} bytes');

    final response = await client.post(
      Uri.parse("$url/ideas"),
      headers: {
        'Content-Type': 'application/json',
        'Cookie': 'sessionId=$sessionId',
      },
      body: jsonEncode(data),
    );

    if (response.statusCode == 200) {
      final Map<String, dynamic> responseData = jsonDecode(response.body);
      
      if (responseData['mStatus'] == 'ok' && responseData['mFileMessage'] != null) {
        final String fileId = responseData['mFileMessage'];
        developer.log('Image uploaded successfully. File ID: $fileId');
        return fileId;
      }
    }
    
    developer.log('Failed to upload image. Status: ${response.statusCode}');
    return null;
  } catch (e) {
    developer.log('Error uploading image: $e');
    return null;
  }
}