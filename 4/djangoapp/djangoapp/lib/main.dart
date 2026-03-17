import 'dart:convert';

import 'package:djangoapp/urls.dart';
import 'package:djangoapp/create.dart';
import 'package:djangoapp/update.dart';
import 'package:djangoapp/note.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() {
  runApp(MainApp());
}

class MainApp extends StatelessWidget {
  // const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primaryColor: Colors.blue,
      ),
      home: MyHomePage(title:'Django Demo 1'),
    );
  }
}


class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  http.Client client = http.Client();
  List<Note> notes = [];


  int _counter = 0;

  void _increementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  void initState() {
    _retrieveNotes();
    super.initState();
  }

  _retrieveNotes() async {
    notes = [];
    var  resp = await client.get(retrieveUrl);
    print("Odpověď ze serveru: ${resp.body}");
    List response = json.decode(resp.body);

    response.forEach((element) {
      notes.add(Note.fromMap(element));
    });
    setState(() {
      
    });
  }
  void _deleteNote(int id) async {
    client.delete(deleteUrl(id));
    _retrieveNotes();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
    body:RefreshIndicator(
      onRefresh: () async {
          _retrieveNotes();
        },
      child: ListView.builder(
        itemCount: notes.length,
        itemBuilder: (BuildContext context, int index) {
          return ListTile(
            title: Text(notes[index].note),
            onTap: () async {
              await Navigator.of(context).push(
              MaterialPageRoute(builder: (context) => UpdatePage(
                client: client,
                id: notes[index].id,
                note: notes[index].note,
              )),
              );
            },
            trailing: IconButton(
              icon: Icon(Icons.delete),
              onPressed: () => _deleteNote(notes[index].id), 
              ),
          );
        },
      ),
    ),
    floatingActionButton: FloatingActionButton(
      onPressed: () async => await Navigator.of(context).push(
        MaterialPageRoute(builder: (context) => CreatePage(client: client,
        )),
        ),
      tooltip: 'Increment',
      child: Icon(Icons.add),
    ),
    );
  }
}