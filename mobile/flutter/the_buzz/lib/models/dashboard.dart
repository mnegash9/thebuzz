class Dashboard {
  final int userId;
  final String firstName;
  final String lastName;
  final int ideaId;
  final String ideaStr;
  final int commentId;
  final int commenterUserId;
  final int upvoteCount;
  final int downvoteCount;
  final String? ideaFile;  // Add this for the image

  const Dashboard({
    required this.userId,
    required this.firstName,
    required this.lastName,
    required this.ideaId,
    required this.ideaStr,
    required this.commentId,
    required this.commenterUserId,
    required this.upvoteCount,
    required this.downvoteCount,
    this.ideaFile,  // Add this to constructor
  });

  factory Dashboard.fromJson(Map<String, dynamic> json) {
    return Dashboard(
      userId: json['user_id'] ?? 0,
      firstName: json['first_name'] ?? '',  // Make sure this matches backend field name
      lastName: json['last_name'] ?? '',    // Make sure this matches backend field name
      ideaId: json['idea_id'] ?? 0,
      ideaStr: json['idea'] ?? '',
      commentId: json['comment_id'] ?? 0,
      commenterUserId: json['commenter_id'] ?? 0,
      upvoteCount: json['upvote_count'] ?? 0,    // Match backend field name
      downvoteCount: json['downvote_count'] ?? 0, // Match backend field name
      ideaFile: json['idea_file'],  // Add this to parse image data
    );
  }

  Map<String, dynamic> toJson() => {
        'user_id': userId,
        'first_name': firstName,  // Update field names to match
        'last_name': lastName,    // Update field names to match
        'idea_id': ideaId,
        'idea': ideaStr,
        'comment_id': commentId,
        'commenter_id': commenterUserId,
        'upvote_count': upvoteCount,    // Update field names to match
        'downvote_count': downvoteCount, // Update field names to match
        'idea_file': ideaFile,  // Add this for the image
      };
}