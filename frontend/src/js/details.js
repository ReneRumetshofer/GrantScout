const params = new URLSearchParams(window.location.search);
const jsonFileName = params.get('json');

fetch("../mock/" + jsonFileName)
    .then(response => response.json())
    .then(ausschreibung => {
        document.getElementById("ausschreibungName").textContent = ausschreibung.Name;
        document.getElementById("themenbereich").textContent = ausschreibung.Themenbereich;
        document.getElementById("kurzbeschreibung").textContent = ausschreibung.Kurzbeschreibung;
        document.getElementById("langbeschreibung").textContent = ausschreibung.Langbeschreibung;
        document.getElementById("foerderbetrag").textContent = ausschreibung.Förderbetrag;
        document.getElementById("bewerbungsfrist").textContent = `${ausschreibung.Bewerbungsfrist.Von} bis ${ausschreibung.Bewerbungsfrist.Bis}`;

        const regionen = document.getElementById("regionen");
        ausschreibung.Regionen.forEach(region => {
            let li = document.createElement("li");
            li.textContent = region;
            regionen.appendChild(li);
        });

        const zielgruppe = document.getElementById("zielgruppe");
        ausschreibung.Zielgruppe.forEach(ziel => {
            let li = document.createElement("li");
            li.textContent = ziel;
            zielgruppe.appendChild(li);
        });

        const kontakte = document.getElementById("kontakte");
        ausschreibung.Kontakt.forEach(contact => {
            let li = document.createElement("li");
            li.innerHTML = `${contact.Ansprechpartner}: <a href="mailto:${contact['E-Mail']}">${contact['E-Mail']}</a>, Telefon: ${contact.Telefon}`;
            kontakte.appendChild(li);
        });

        const optional = document.getElementById("optional");
        for (const key in ausschreibung.Optional) {
            let dt = document.createElement("dt");
            dt.textContent = key;
            let dd = document.createElement("dd");
            dd.textContent = ausschreibung.Optional[key];
            optional.appendChild(dt);
            optional.appendChild(dd);
        }

        document.getElementById("webseite").setAttribute("href", ausschreibung.Webseite);
    })
    .catch(error => console.error('Fehler beim Laden der Details:', error));