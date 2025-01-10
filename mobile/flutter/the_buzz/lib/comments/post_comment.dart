import 'package:flutter/material.dart';
import 'package:the_buzz/net/web_requests.dart';
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';
import 'dart:convert';
import 'dart:io';
import 'dart:developer' as developer;

class PostComment extends StatefulWidget {
  final int uniqueIdeaId;
  final dynamic refreshFunc;

  const PostComment({
    super.key,
    required this.uniqueIdeaId,
    this.refreshFunc,
  });

  @override
  State<PostComment> createState() => _PostCommentState();
}

class _PostCommentState extends State<PostComment> {
  final controller = TextEditingController();
  final picker = ImagePicker();
  File? _image;
  String? _base64Image;
  bool _isUploading = false;

  Future<void> _getImage(ImageSource source) async {
    try {
      setState(() {
        _isUploading = true;
      });

      final pickedFile = await picker.pickImage(
        source: source,
        maxWidth: 1024,
        maxHeight: 1024,
        imageQuality: 70,
      );

      if (pickedFile != null) {
        setState(() {
          _image = File(pickedFile.path);
        });
        
        // Convert image to base64
        final bytes = await _image!.readAsBytes();
        _base64Image = base64Encode(bytes);
        
        if (_base64Image!.length > 500000) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Image is too large. Please choose a smaller image.')),
          );
          setState(() {
            _image = null;
            _base64Image = null;
          });
          return;
        }

        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Image attached successfully!')),
        );
      }
    } catch (e) {
      developer.log('Error processing image: $e');
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error processing image: $e')),
      );
      setState(() {
        _image = null;
        _base64Image = null;
      });
    } finally {
      setState(() {
        _isUploading = false;
      });
    }
  }

  void _showImageSourceDialog() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Select Image Source'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            ListTile(
              leading: const Icon(Icons.camera),
              title: const Text('Camera'),
              onTap: () {
                Navigator.pop(context);
                _getImage(ImageSource.camera);
              },
            ),
            ListTile(
              leading: const Icon(Icons.photo_library),
              title: const Text('Gallery'),
              onTap: () {
                Navigator.pop(context);
                _getImage(ImageSource.gallery);
              },
            ),
          ],
        ),
      ),
    );
  }

  Future<void> _handlePost() async {
    if (controller.text.trim().isEmpty && _base64Image == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter some text or add an image')),
      );
      return;
    }

    setState(() {
      _isUploading = true;
    });

    try {
      final success = await postComment(
        widget.uniqueIdeaId,
        controller.text,
        _base64Image,
        http.Client(),
      );
      
      if (success) {
        controller.clear();
        setState(() {
          _image = null;
          _base64Image = null;
        });
        widget.refreshFunc?.call();
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Comment posted successfully!')),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Failed to post comment. Please try again.')),
        );
      }
    } catch (e) {
      developer.log('Error posting comment: $e');
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Error posting comment. Please try again.')),
      );
    } finally {
      setState(() {
        _isUploading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.pink[50],
        borderRadius: BorderRadius.circular(20),
      ),
      child: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.end,
          mainAxisSize: MainAxisSize.min,
          children: [
            if (_image != null)
              Stack(
                alignment: Alignment.topRight,
                children: [
                  ClipRRect(
                    borderRadius: BorderRadius.circular(8),
                    child: Image.file(
                      _image!,
                      height: 100,
                      width: double.infinity,
                      fit: BoxFit.cover,
                    ),
                  ),
                  IconButton(
                    icon: const Icon(
                      Icons.close,
                      color: Colors.white,
                      shadows: [
                        Shadow(
                          color: Colors.black54,
                          blurRadius: 8,
                        ),
                      ],
                    ),
                    onPressed: () {
                      setState(() {
                        _image = null;
                        _base64Image = null;
                      });
                    },
                  ),
                ],
              ),
            
            const SizedBox(height: 8),
            
            Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const CircleAvatar(child: Icon(Icons.person)),
                const SizedBox(width: 8),
                Expanded(
                  child: TextField(
                    maxLength: 512,
                    minLines: 1,
                    maxLines: 3,
                    decoration: InputDecoration(
                      filled: true,
                      fillColor: Colors.grey[350],
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(25),
                      ),
                      hintText: "Type your comment here",
                      counterText: "",
                      suffixIcon: _isUploading
                          ? const SizedBox(
                              width: 20,
                              height: 20,
                              child: Padding(
                                padding: EdgeInsets.all(10),
                                child: CircularProgressIndicator(
                                  strokeWidth: 2,
                                ),
                              ),
                            )
                          : IconButton(
                              icon: const Icon(Icons.image),
                              onPressed: _showImageSourceDialog,
                            ),
                    ),
                    controller: controller,
                  ),
                ),
              ],
            ),
            
            const SizedBox(height: 8),
            
            ElevatedButton(
              onPressed: _isUploading ? null : _handlePost,
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.blue,
                padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
              ),
              key: const Key("post_button"),
              child: _isUploading
                  ? const SizedBox(
                      width: 20,
                      height: 20,
                      child: CircularProgressIndicator(
                        strokeWidth: 2,
                        valueColor: AlwaysStoppedAnimation<Color>(Colors.white),
                      ),
                    )
                  : const Text(
                      "Post",
                      style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
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
    controller.dispose();
    super.dispose();
  }
}