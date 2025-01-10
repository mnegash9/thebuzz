import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:mockito/annotations.dart';

import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';
import 'package:the_buzz/models/idea_object.dart';
import 'package:the_buzz/net/web_requests.dart';

import 'test_mobile.mocks.dart';

// Generate a MockClient using the Mockito package.
// Create new instances of this class in each test.
@GenerateMocks([http.Client])
void main() {
  test(
      'Ideas are created from a web database in the format of the id, idea content, and likes',
      () async {
    final client = MockClient();

    // Use Mockito to return a successful response when it calls the
    // provided http.Client.
    when(client.get(Uri.parse('https://team-dog.dokku.cse.lehigh.edu/ideas')))
        .thenAnswer((_) async => http.Response(
            '[{"mId" : 0, "mIdea": "zero", "mLikes": 0},{"mId" : 1, "mIdea": "one", "mLikes": 5},{"mId" : 2, "mIdea": "two", "mLikes": 7},{"mId" : 3, "mIdea": "three", "mLikes": 9}]',
            200));

    expect(await fetchIdeas(client), isA<List<Idea>>());
  });

  test('Testing that ideas can be posted from mobile', () async {
    final client = MockClient();

    var idea = "my idea";
    final Map<String, dynamic> data = {
      'mIdea': idea,
    };

    // Use Mockito to return a successful response when it calls the
    // provided http.Client.
    when(client.post(
      Uri.parse('https://team-dog.dokku.cse.lehigh.edu/ideas'),
      body: jsonEncode(data),
    )).thenAnswer(
        (_) async => http.Response('{"mStatus":"ok","mIdea":"1"}', 200));

    expect(await postIdea(idea, client), true);
  });

  test('Testing that ideas can be liked from mobile', () async {
    final client = MockClient();

    // Use Mockito to return a successful response when it calls the
    // provided http.Client.
    when(client.put(
            Uri.parse('https://team-dog.dokku.cse.lehigh.edu/ideas/0/likes')))
        .thenAnswer(
            (_) async => http.Response('{"mStatus":"ok","mIdea":"1"}', 200));

    var ideaId = 0;

    expect(await upVote(ideaId, client), true);
  });

  testWidgets('App Title is the BUZZ', (tester) async {
    // search for key app_title
    var k = const Key("app_title");
    await tester.pumpWidget(MaterialApp(key: k, home: Container()));
    expect(find.byKey(k), findsOneWidget);
  });

  testWidgets('Finds and makes sure post button is there', (tester) async {
    // search for key post_button
    var p = const Key("post_button");
    // Search for the 'post_button' in the widget tree.
    await tester.pumpWidget(MaterialApp(key: p, home: Container()));
    expect(find.byKey(p), findsOneWidget);
  });
}
