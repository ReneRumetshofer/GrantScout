const params = new URLSearchParams(window.location.search);
const id = params.get('id');

if (!id) {
    console.error('Keine ID in der URL angegeben.');
} else {
    fetch("https://localhost/api/calls?type=PARSED")
        .then(response => response.json())
        .then(data => {
            const ausschreibungen = data.flat();
            console.log(ausschreibungen);
            const ausschreibung = ausschreibungen.find(item => item.call.id === Number(id));
            console.log(ausschreibung);

            if (!ausschreibung) {
                console.error('Ausschreibung mit ID ' + id + ' nicht gefunden.');
                document.querySelector('body').innerHTML = '<div class="container mt-4"><h1>Ausschreibung nicht gefunden</h1></div>';
                return;
            }

            let parsedData = ausschreibung.parsedData

            document.getElementById("ausschreibungName").textContent = parsedData.name;
            document.getElementById("themenbereich").textContent = parsedData.topic;
            document.getElementById("kurzbeschreibung").textContent = parsedData.shortDescription;
            document.getElementById("langbeschreibung").textContent = parsedData.longDescription;
            document.getElementById("foerderbetrag").textContent = parsedData.grantCallSum;
            document.getElementById("bewerbungsfrist").textContent = `${parsedData.applicationDeadlines.from} bis ${parsedData.applicationDeadlines.to}`;

            document.getElementById("regionen").textContent = parsedData.regions;
            /*const regionen = document.getElementById("regionen");
            parsedData.region.forEach(region => {
                let li = document.createElement("li");
                li.textContent = region;
                regionen.appendChild(li);
            });*/

            document.getElementById("zielgruppe").textContent = parsedData.targetGroup;
            /*const zielgruppe = document.getElementById("zielgruppe");
            parsedData.targetGroup.forEach(ziel => {
                let li = document.createElement("li");
                li.textContent = ziel;
                zielgruppe.appendChild(li);
            });*/

            const kontakte = document.getElementById("kontakte");
            parsedData.contact.forEach(contact => {
                let li = document.createElement("li");
                li.innerHTML = `${contact.contactPerson}: <a href="mailto:${contact['email']}">${contact['email']}</a>, Telefon: ${contact.telephone}`;
                kontakte.appendChild(li);
            });

            const optional = document.getElementById("optional");
            for (const key in parsedData.optional) {
                let dt = document.createElement("dt");
                dt.textContent = key;
                let dd = document.createElement("dd");
                dd.textContent = parsedData.optional[key];
                optional.appendChild(dt);
                optional.appendChild(dd);
            }

            document.getElementById("webseite").setAttribute("href", parsedData.website);
        })
        .catch(error => console.error('Fehler beim Laden der Details:', error));
}
