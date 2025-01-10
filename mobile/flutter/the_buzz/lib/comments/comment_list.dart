import 'package:flutter/material.dart';
import 'package:the_buzz/models/comment_object.dart';
import 'package:the_buzz/comments/comments.dart';

/// A widget that displays a list of comments fetched asynchronously.
/// 
/// Uses [FutureBuilder] to handle the asynchronous loading of comments and
/// displays them in a scrollable list view. Filters out duplicate comments
/// based on their commentId.
class CommentList extends StatelessWidget {
  /// Future that resolves to a list of comments to be displayed
  final Future<List<Comment>> futureCommentsList;
  final dynamic refreshFunc;
  const CommentList({super.key, required this.futureCommentsList, this.refreshFunc});

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<Comment>>(
      future: futureCommentsList,
      builder: (BuildContext context, AsyncSnapshot<List<Comment>> snapshot) {
        if (snapshot.hasData) {
          final newIdeas = <int>{};
          final filter = snapshot.data!.where((idea) {
            final isNewIdea = newIdeas.add(idea.commentId);
            return isNewIdea;
          }).toList();

          return ListView.builder(
            padding: const EdgeInsets.all(4.0),
            itemCount: filter.length,
            itemBuilder: (context, i) {
              return Column(
                children: <Widget>[
                  Comments(
                    ourComment: filter[i],
                    refreshFunc: refreshFunc,
                  ),
                  const SizedBox(height: 3),
                ],
              );
            },
          );
        } else {
          return Center(child: Text('${snapshot.error}'));
        }
      },
    );
  }



}
