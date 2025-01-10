class Comment {
  final int commentId;
  final String commentBody;
  final int ideaId;
  final int ideaUserId;
  final String? commentFile;

  const Comment({
    required this.commentId,
    required this.commentBody,
    required this.ideaId,
    required this.ideaUserId,
    this.commentFile,
  });

  factory Comment.fromJson(Map<String, dynamic> json) {
    return Comment(
      commentId: json['comment_id'],
      commentBody: json['body'],
      ideaId: json['idea_id'],
      ideaUserId: json['user_id'],
      commentFile: json['comment_file'],  
    );
  }

  Map<String, dynamic> toJson() => {
    'comment_id': commentId,
    'body': commentBody,
    'idea_id': ideaId,
    'user_id': ideaUserId,
    'comment_file': commentFile, 
  };
}