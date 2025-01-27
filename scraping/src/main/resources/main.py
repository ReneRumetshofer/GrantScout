import json
import os
import sys
from concurrent.futures import ThreadPoolExecutor

import openai
from openai import OpenAIError

# API-Schlüssel festlegen
openai.api_key = "123"

def standardize_grant_data(raw_text):
    """
    Verarbeitet unstrukturierte Förderinformationen und gibt diese in standardisierter Form zurück.

    :param raw_text: Unstrukturierter Text über Förderprogramme
    :return: Ein standardisiertes Dictionary mit Förderinformationen
    """
    messages = [
        {
            "role": "system", "content": "You are a helpful assistant for standardizing grant information."
        },
        {
            "role": "user",
            "content": f"""
            Bitte standardisiere die folgenden Förderinformationen in reinem JSON-Format, ohne Formatierungselemente wie Markdown oder zusätzliche Kommentare.
            Die Optional- sowie Kontakt-Felder sollen nur befüllt werden, wenn diese auch eindeutig festgestellt werden können. 
            Erzeuge keine Werte mit null. Falls kein Datum ermittelt werden kann, schreibe "unbekannt", oder "laufend".
            Falls ein anderes Feld außer Datum nicht ermittelt werden kann, lasse es komplett weg.

            Beispiel:
            {{
                "Name": "Beispiel Förderprogramm",
                "Themenbereich": "Beispiel Kategorie:
                    Digitalisierung & Breitband
                    Energiewende
                    Europa & Internationales
                    Gesellschaft & Sicherheit
                    Holzforschung & -wissenstransfer
                    Innovative & wettbewerbsfähige Unternehmen
                    Klimaneutrale Stadt
                    Kooperation & Forschungsinfrastruktur
                    Kreislaufwirtschaft
                    Lebenswissenschaften & Gesundheit
                    Menschen, Qualifikation & Gender
                    Mobilitätswende
                    Produktion & Material
                    Quantenforschung & -technologie
                    Weltraum & Luftfahrt,
                    etc.",
                "Kurzbeschreibung": "Ausschreibungszweck, Beschreibung der Ziele, maximal 100 Zeichen, obligatorisch",
                "Langbeschreibung": "Ausführliche Beschreibung der Ausschreibung, maximal 500 Zeichen",
                "Organisation": "Beispiel Stiftung oder Institut",
                "Förderbetrag": "Bis zu 50.000 Euro, Ausnahmen bis 20.000 Euro",
                "Bewerbungsfrist": {{
                    "Von": "TT.MM.JJJJ HH:MM"
                    "Bis": "TT.MM.JJJJ HH:MM"
                }},
                "Regionen": ["Deutschland", "Europa", etc.],
                "Zielgruppe": ["Startups", "Non-Profits", "Forschung", "KMU", "öffentliche Infrastruktur", etc.],
                "Webseite": "https://beispiel.foerderung.de",
                "Kontakt": [
                    {{
                        "Ansprechpartner": "Tom Turbo",
                        "E-Mail": "tom.turbo@gmail.com",
                        "Telefon": "+43 1 234 56 789"
                    }}
                ],
                "Optional": {{
                    "Status": "Aktiv, geschlossen, verlängert, etc.",
                    "Häufigkeit": "Einmalig, jährlich, laufend, etc.",
                    "Voraussetzungen": "Mindestalter, Umsatzgrenzen, etc.",
                    "Notwendige Dokumente": "Finanzberichte, Lebenslauf, etc.",
                    "Bewertungskriterien": "Innovation, soziale Wirkung, Nachhaltigkeit, etc.",
                    "Bewerbungsplattform": "Spezielles Portal zum Einreichen, wenn möglich mit Namen",
                    "Kosten": "Bewerbungsgebühren",
                    "Fördertyp": "Zuschuss, Darlehen, Beteiligungskapital, Sachleistung, etc.",
                    "Förderdauer": "Zeitraum",
                    "Förderquote": "Prozentsatz der geförderten Kosten",
                    "Mögliche Aktivitäten": "Welche Maßnahmen werden gefördert (Personal, Material, Marketing, etc.)"
                }}
            }}
        
            Text:
            {raw_text}
            """
        }
    ]

    try:
        response = openai.chat.completions.create(
            model="gpt-4o-mini",
            messages=messages,
            temperature=0.5
        )
        # Extrahiere den Text aus der Antwort
        standardized_data = response.choices[0].message.content.strip()
        return standardized_data
    except OpenAIError as e:
        print(f"Fehler bei der Verarbeitung: {e}")
        return None

def process_file(file_path, target_folder):
    try:
        print(f"Starte Verarbeitung von: {file_path}")
        # Lese den Inhalt der Datei
        with open(file_path, "r", encoding="utf-8") as file:
            raw_text = file.read()

        # Verarbeite die Daten
        standardized_data = standardize_grant_data(raw_text)

        if standardized_data:
            target_file_path = os.path.join(
                target_folder,
                f"{os.path.splitext(os.path.basename(file_path))[0]}.json"
            )
            # Speichere die Daten in einer JSON-Datei
            with open(target_file_path, "w", encoding="utf-8") as json_file:
                json.dump(eval(standardized_data), json_file, ensure_ascii=False, indent=4)
            print(f"Erfolgreich verarbeitet: {file_path}")
        else:
            print(f"Keine standardisierten Daten für Datei: {file_path}")
    except Exception as e:
        print(f"Fehler beim Verarbeiten der Datei '{file_path}': {e}")

def process_files_in_folder_parallel(source_folder, target_folder, max_workers=5):
    os.makedirs(target_folder, exist_ok=True)
    files = [os.path.join(source_folder, f) for f in os.listdir(source_folder) if f.endswith(".txt")]

    # Parallelisiere die Verarbeitung
    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        futures = [executor.submit(process_file, file, target_folder) for file in files]
        for future in futures:
            future.result()  # Auf Fehler prüfen

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Bitte Pfad zum Quell- und Zielordner angeben!")
        sys.exit(1)

    source_folder = sys.argv[1]
    target_folder = sys.argv[2]
    process_files_in_folder_parallel(source_folder, target_folder)