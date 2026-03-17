import 'dart:convert';

class Note {
  final int id;      // Doporučuji 'final', pokud se id nemění
  final String note;

  Note({
    required this.id,
    required this.note,
  });

  // Pomocná metoda pro vytváření kopie s jednou změněnou hodnotou
  Note copyWith({
    int? id,
    String? note,
  }) {
    return Note(
      id: id ?? this.id,
      note: note ?? this.note,
    );
  }

  // Převod objektu na Mapu (pro odesílání do Django API)
  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'body': note, // Django vrací 'body', tak to mapujeme správně
    };
  }

  // Vytvoření objektu z Mapy (při příjmu z Django API)
  factory Note.fromMap(Map<String, dynamic> map) {
    return Note(
      id: map['id'],
      note: map['body'],
    );
  }

  String toJson() => json.encode(toMap());

  factory Note.fromJson(String source) => Note.fromMap(json.decode(source) as Map<String, dynamic>);

  @override
  String toString() => 'Note(id: $id, note: $note)';
}