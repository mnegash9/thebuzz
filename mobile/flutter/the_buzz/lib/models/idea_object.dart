
class Idea {
  final int theIdeaId;
  final int theUserId;
  final String theIdeaStr;
  final String theActiveIdea;


  const Idea({
    required this.theIdeaId,
    required this.theUserId,
    required this.theIdeaStr,
    required this.theActiveIdea,
  });

  factory Idea.fromJson(Map<String, dynamic> json) {
    return Idea(
      theIdeaId: json['idea_id'],
      theUserId: json['user_id'],
      theIdeaStr: json['idea'],
      theActiveIdea: json['active_idea']
    );
  }

  Map<String, dynamic> toJson() => {
        'idea_id': theIdeaId,
        'user_id': theUserId,
        'idea': theIdeaStr,
        'active_idea': theActiveIdea,
  };
}