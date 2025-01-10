import 'package:flutter/material.dart';
import 'package:the_buzz/models/dashboard.dart';
import 'package:the_buzz/ideas/ideas.dart';

/// A widget that displays a list of ideas fetched asynchronously.
/// 
/// Uses [FutureBuilder] to handle the asynchronous loading of ideas and
/// displays them in a scrollable list view. Filters out duplicate ideas
/// and sorts them by ideaId in descending order.
class IdeaList extends StatelessWidget {
   /// Future that resolves to a list of ideas to be displayed
  final Future<List<Dashboard>> futureListIdeas;
  final dynamic refreshFunc;
  const IdeaList({super.key, required this.futureListIdeas, this.refreshFunc});


  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<Dashboard>>(
      future: futureListIdeas,
      builder: (BuildContext context, AsyncSnapshot<List<Dashboard>> snapshot) {
        if (snapshot.hasData) {
          final newIdeas = <int>{};
          final filter = snapshot.data!.where((idea) {
            final isNewIdea = newIdeas.add(idea.ideaId);
            return isNewIdea;
          }).toList()..sort((a, b) => b.ideaId.compareTo(a.ideaId));

          return ListView.builder(
            padding: const EdgeInsets.all(16.0),
            itemCount: filter.length,
            itemBuilder: (context, i) {
              return Column(
                children: <Widget>[
                  Ideas(
                    ourIdea: filter[i],
                    refreshFunc: refreshFunc,
                  ),
                  const SizedBox(height: 8),
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
