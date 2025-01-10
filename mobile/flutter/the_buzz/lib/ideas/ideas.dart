import 'package:flutter/material.dart';
import 'package:the_buzz/models/dashboard.dart';
import 'package:the_buzz/pages/comment_page.dart';
import 'package:the_buzz/net/web_requests.dart';
import 'package:the_buzz/utils/link_utils.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:flutter/gestures.dart';
import 'package:http/http.dart' as http;
import 'dart:developer' as developer;
import 'dart:convert';

class Ideas extends StatefulWidget {
  final Dashboard ourIdea;
  final dynamic refreshFunc;
  const Ideas({super.key, required this.ourIdea, this.refreshFunc});

  @override
  State<Ideas> createState() => _IdeasState();
}

class _IdeasState extends State<Ideas> {
  final _fontsize = 20.0;
  late final TapGestureRecognizer _tapRecognizer;

  @override
  void initState() {
    super.initState();
    _tapRecognizer = TapGestureRecognizer();
  }

  @override
  void dispose() {
    _tapRecognizer.dispose();
    super.dispose();
  }

  Future<void> _launchURL(String urlString) async {
    final Uri? url = Uri.tryParse(urlString);
    if (url != null) {
      if (await canLaunchUrl(url)) {
        await launchUrl(url, mode: LaunchMode.externalApplication);
      }
    }
  }

  Widget _buildTextWithLinks(String text) {
    final String? url = extractUrl(text);
    if (url == null) {
      return Text(
        text,
        style: TextStyle(fontSize: _fontsize),
      );
    }

    final int urlStart = text.indexOf(url);
    final int urlEnd = urlStart + url.length;

    return RichText(
      text: TextSpan(
        style: TextStyle(fontSize: _fontsize, color: Colors.black),
        children: [
          if (urlStart > 0)
            TextSpan(
              text: text.substring(0, urlStart),
            ),
          TextSpan(
            text: url,
            style: const TextStyle(
              color: Colors.blue,
              decoration: TextDecoration.underline,
            ),
            recognizer: _tapRecognizer..onTap = () => _launchURL(url),
          ),
          if (urlEnd < text.length)
            TextSpan(
              text: text.substring(urlEnd),
            ),
        ],
      ),
    );
  }

  Widget _buildContent() {
    List<Widget> contentWidgets = [];

    // Add text content if present
    if (widget.ourIdea.ideaStr.trim().isNotEmpty) {
      contentWidgets.add(
        Padding(
          padding: const EdgeInsets.only(bottom: 12),
          child: _buildTextWithLinks(widget.ourIdea.ideaStr.trim()),
        ),
      );
    }

    // Add image if present
    if (widget.ourIdea.ideaFile != null && widget.ourIdea.ideaFile!.isNotEmpty) {
      try {
        contentWidgets.add(
          ClipRRect(
            borderRadius: BorderRadius.circular(8),
            child: Image.memory(
              base64Decode(widget.ourIdea.ideaFile!),
              width: double.infinity,
              height: 200,
              fit: BoxFit.cover,
              errorBuilder: (context, error, stackTrace) {
                developer.log('Error loading image: $error');
                return Container(
                  width: double.infinity,
                  height: 100,
                  color: Colors.grey[300],
                  child: const Center(
                    child: Text(
                      'Failed to load image',
                      style: TextStyle(color: Colors.grey),
                    ),
                  ),
                );
              },
            ),
          ),
        );
      } catch (e) {
        developer.log('Error decoding image: $e');
        contentWidgets.add(
          Container(
            width: double.infinity,
            height: 100,
            color: Colors.grey[300],
            child: const Center(
              child: Text(
                'Invalid image data',
                style: TextStyle(color: Colors.grey),
              ),
            ),
          ),
        );
      }
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: contentWidgets,
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.pink[50],
        borderRadius: BorderRadius.circular(20),
      ),
      margin: const EdgeInsets.symmetric(vertical: 8, horizontal: 12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // User Info Section
          Padding(
            padding: const EdgeInsets.all(12),
            child: Row(
              children: [
                const CircleAvatar(child: Icon(Icons.person)),
                const SizedBox(width: 8),
                Text(
                  '${widget.ourIdea.firstName} ${widget.ourIdea.lastName}',
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: _fontsize,
                  ),
                ),
              ],
            ),
          ),

          // Content Section (Text and Image)
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            child: _buildContent(),
          ),

          const Divider(height: 1.0),

          // Voting Section
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 8),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                _buildVoteButton(
                  icon: Icons.keyboard_arrow_up,
                  count: widget.ourIdea.upvoteCount,
                  color: Colors.green,
                  onTap: () async {
                    await upVote(widget.ourIdea.ideaId, http.Client());
                    widget.refreshFunc.call();
                  },
                ),
                const SizedBox(width: 24),
                _buildVoteButton(
                  icon: Icons.keyboard_arrow_down,
                  count: widget.ourIdea.downvoteCount,
                  color: Colors.red,
                  onTap: () async {
                    await downVote(widget.ourIdea.ideaId, http.Client());
                    widget.refreshFunc.call();
                  },
                ),
              ],
            ),
          ),

          // Comments Button
          Center(
            child: Padding(
              padding: const EdgeInsets.only(bottom: 12),
              child: ElevatedButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => MyCommentPage(ideaId: widget.ourIdea.ideaId),
                    ),
                  );
                },
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                ),
                child: const Text('View Comments'),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildVoteButton({
    required IconData icon,
    required int count,
    required Color color,
    required VoidCallback onTap,
  }) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(20),
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Row(
          children: [
            Icon(
              icon,
              color: color,
              size: 30,
            ),
            const SizedBox(width: 4),
            Text(
              count.toString(),
              style: TextStyle(
                fontSize: _fontsize,
                fontWeight: FontWeight.bold,
              ),
            ),
          ],
        ),
      ),
    );
  }
}