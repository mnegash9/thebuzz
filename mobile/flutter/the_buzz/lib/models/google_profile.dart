
class Profile {
  final int googleUserId;
  final String googlePictureUrl;
  final int googleSessionId;
  final String googleGivenName;
  final String googleFamilyName;
  final String googleEmail;


  const Profile({
    required this.googleUserId,
    required this.googlePictureUrl,
    required this.googleSessionId,
    required this.googleGivenName,
    required this.googleFamilyName,
    required this.googleEmail,
  });

  factory Profile.fromJson(Map<String, dynamic> json) {
    return Profile(
      googleUserId: json['user_id'],
      googlePictureUrl: json['pictureUrl'],
      googleSessionId: json['sessionId'],
      googleGivenName: json['given_name'],
      googleFamilyName: json['family_name'],
      googleEmail: json['email']
    );
  }

  Map<String, dynamic> toJson() => {
    'user_id': googleUserId,
    'pictureUrl': googlePictureUrl,
    'sessionId': googleSessionId,
    'given_name': googleGivenName,
    'family_name': googleFamilyName,
    'email': googleEmail,
  };

  @override
  String toString() {
    return 'Profile(googleUserId: $googleUserId, googlePictureUrl: $googlePictureUrl, googleSessionId: $googleSessionId, googleGivenName: $googleGivenName, googleFamilyName: $googleFamilyName, googleEmail: $googleEmail)';
  }
 
}