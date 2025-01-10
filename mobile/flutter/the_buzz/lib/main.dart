import 'package:flutter/material.dart';
import 'package:the_buzz/ideas/idea_list.dart';
import 'package:the_buzz/ideas/post_idea.dart';
import 'package:the_buzz/models/dashboard.dart';
import 'package:the_buzz/net/web_requests.dart';
import 'package:http/http.dart' as http;
import 'package:the_buzz/pages/profile_page.dart';
import 'package:the_buzz/pages/googe_login.dart';
import 'package:the_buzz/services/auth_service.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final bool isLoggedIn = await AuthService.isLoggedIn();
  
  runApp(MyApp(isLoggedIn: isLoggedIn));
}
/// The main application widget.
/// 
/// Initializes the app theme and handles initial routing based on
/// authentication status.
class MyApp extends StatelessWidget {
  final bool isLoggedIn;
  const MyApp({super.key, required this.isLoggedIn});
  final title = "The Buzz";

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: title,
      theme: ThemeData(
        primaryColor: Colors.pink[100],
        useMaterial3: true,
      ),
      home: isLoggedIn ? const MyHomePage(title: "The Buzz") : SignIn(),
    );
  }
}
/// The main page of the application.
/// 
/// Displays a list of ideas, allows posting new ideas, and provides
/// navigation to user profile. Supports pull-to-refresh functionality.
class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});
  final String title;

  @override
  State<MyHomePage> createState() => MyHomePageState();
}

class MyHomePageState extends State<MyHomePage> {
  late Future<List<Dashboard>> futureListIdeas;

  @override
  void initState() {
    super.initState();
    futureListIdeas = fetchDashboard(http.Client());
  }

  Future<void> refreshItems() async {
    setState(() {
      futureListIdeas = fetchDashboard(http.Client());
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Theme.of(context).primaryColor,

      // app bar with picture and title
      appBar: AppBar(
        title: Row(
          children: [
            SizedBox(
                width: 58.5,
                height: 50,
                child: Image.asset("lib/assets/dog.png")),
            const SizedBox(
              width: 78,
            ),
            Align(
                alignment: Alignment.centerRight,
                child: Text(
                  widget.title,
                  style: const TextStyle(fontSize: 28, color: Colors.pink),
                  key: const Key("app_title"),
                )),
              const SizedBox(
              width: 80,
            ),
            SizedBox(
                width: 40,
                height: 40,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  shape: const CircleBorder(),
                  padding: EdgeInsets.zero,
                ),
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => const ProfilePage(),
                    ),
                  );
                },
                child: const CircleAvatar(child: Icon(Icons.person)),
              ),
            ),
          ],
        ),
        backgroundColor: Theme.of(context).secondaryHeaderColor,
      ),

      body: Center(
        // onrefresh allows pull down to refresh list at any time
        child: RefreshIndicator(
          onRefresh: refreshItems,
          child: Column(
            children: [
              // idea list that displays all current ideas
              Expanded(
                child: IdeaList(
                  futureListIdeas: futureListIdeas,
                  refreshFunc: refreshItems,
                ),
              ),

              // bottom area that allows for posting new ideas
              SizedBox(
                  height: 200,
                  child: PostIdea(
                    refreshFunc: refreshItems,
                  )),
            ],
          ),
        ),
      ),
    );
  }
}
