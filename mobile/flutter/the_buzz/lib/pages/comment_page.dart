import 'package:flutter/material.dart';
import 'package:the_buzz/comments/comment_list.dart';
import 'package:the_buzz/models/comment_object.dart';
import 'package:the_buzz/comments/post_comment.dart';
import 'package:the_buzz/net/web_requests.dart';
import 'package:http/http.dart' as http;


class MyCommentPage extends StatefulWidget {
  final int ideaId;
  const MyCommentPage({super.key, required this.ideaId});

  @override
  State<MyCommentPage> createState() => MyCommentPageState();
}

class MyCommentPageState extends State<MyCommentPage> {
  late Future<List<Comment>> futureCommentsList;

  @override
  void initState() {
    super.initState();
    futureCommentsList = fetchComments(widget.ideaId, http.Client());
  }

  Future<void> refreshItems() async {
    setState(() {
      futureCommentsList = fetchComments(widget.ideaId, http.Client());
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Theme.of(context).primaryColor,

      appBar: AppBar(
        title: const Row(
          children: [
            SizedBox(
              width: 73,
            ),
            const Align(
                alignment: Alignment.centerRight,
                child: Text(
                  'Comments', 
                  style: const TextStyle(fontSize: 28, color: Colors.pink),
                )),
              
          ],
        ),
        backgroundColor: Theme.of(context).secondaryHeaderColor,
      ),

      body: Center(
        child: RefreshIndicator(
          onRefresh: refreshItems,
          child: Column(
            children: [
              Expanded(
                child: CommentList(
                  futureCommentsList: futureCommentsList,
                  refreshFunc: refreshItems,
                ),
              ),

              SizedBox(
                height: 200,
                child: PostComment(
                  uniqueIdeaId: widget.ideaId,
                  refreshFunc: refreshItems,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
