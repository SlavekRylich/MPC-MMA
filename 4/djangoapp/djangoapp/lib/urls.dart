// lib/urls.dart

// Pokud testuješ na Android emulátoru, použij 10.0.2.2
// Pokud na fyzickém zařízení, musíš použít lokální IP svého PC (např. 192.168.x.x)
const String baseUrl = "http://10.0.2.2:8000";

// URL pro získání všech poznámek a vytvoření nové
// Django endpoint: /notes/
Uri retrieveUrl = Uri.parse("$baseUrl/notes/");

// Funkce pro URL konkrétní poznámky (Update)
// Django endpoint: /notes/<id>/update/
Uri updateUrl(int id) {
  return Uri.parse("$baseUrl/notes/$id/update/");
}

// Funkce pro URL smazání poznámky
// Django endpoint: /notes/<id>/delete/
Uri deleteUrl(int id) {
  return Uri.parse("$baseUrl/notes/$id/delete/");
}

Uri createUrl = Uri.parse("$baseUrl/notes/create/");