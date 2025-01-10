import 'package:flutter/material.dart';
import 'package:the_buzz/models/comment_object.dart';
import 'package:the_buzz/utils/link_utils.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:flutter/gestures.dart';
import 'dart:convert';
import 'dart:developer' as developer;

class Comments extends StatefulWidget {
  final Comment ourComment;
  final dynamic refreshFunc;
  const Comments({super.key, required this.ourComment, this.refreshFunc});

  @override
  State<Comments> createState() => _CommentsState();
}

class _CommentsState extends State<Comments> {
  final _fontsize = 20.0;

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

    final urlRecognizer = TapGestureRecognizer()..onTap = () => _launchURL(url);

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
            recognizer: urlRecognizer,
          ),
          if (urlEnd < text.length)
            TextSpan(
              text: text.substring(urlEnd),
            ),
        ],
      ),
    );
  }

  Widget _buildImage(String base64String) {
    try {
      return ClipRRect(
        borderRadius: BorderRadius.circular(8),
        child: Image.memory(
          base64Decode(base64String),
          width: double.infinity,
          fit: BoxFit.cover,
          errorBuilder: (context, error, stackTrace) {
            developer.log('Error loading image: $error');
            return Container(
              width: double.infinity,
              height: 200,
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
      );
    } catch (e) {
      developer.log('Error decoding image: $e');
      return Container(
        width: double.infinity,
        height: 200,
        color: Colors.grey[300],
        child: const Center(
          child: Text(
            'Invalid image data',
            style: TextStyle(color: Colors.grey),
          ),
        ),
      );
    }
  }

  Widget _buildContent() {
    List<Widget> contentWidgets = [];

    // Add text content if present
    if (widget.ourComment.commentBody.trim().isNotEmpty) {
      contentWidgets.add(
        Padding(
          padding: const EdgeInsets.only(bottom: 12),
          child: _buildTextWithLinks(widget.ourComment.commentBody.trim()),
        ),
      );
    }

    // Add image if present
    if (widget.ourComment.commentFile != null && 
        widget.ourComment.commentFile!.isNotEmpty) {
      contentWidgets.add(
        Padding(
          padding: const EdgeInsets.only(top: 8),
          child: _buildImage(widget.ourComment.commentFile!),
        ),
      );
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
        borderRadius: BorderRadius.circular(0),
      ),
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.only(top: 12, left: 12),
            child: Row(
              children: [
                const CircleAvatar(child: Icon(Icons.person)),
                Text(
                  ' Anonymous',
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: _fontsize,
                  ),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
            child: Container(
              constraints: const BoxConstraints(maxWidth: 325),
              child: _buildContent(),
            ),
          ),
        ],
      ),
    );
  }
}