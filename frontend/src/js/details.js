const params = new URLSearchParams(window.location.search);
const id = params.get('id');

let currentAusschreibung = null;

if (!id) {
    console.error('Keine ID in der URL angegeben.');
} else {
    fetch("http://localhost:8080/calls?type=PARSED")
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
            currentAusschreibung = parsedData;

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
            
            // Enable the start chat button
            setupStartChatButton();
        })
        .catch(error => console.error('Fehler beim Laden der Details:', error));
}

function setupStartChatButton() {
    const startChatBtn = document.getElementById('startChatBtn');
    if (!startChatBtn || !currentAusschreibung) return;
    
    startChatBtn.addEventListener('click', () => {
        // Format the grant call information for the initial message
        const grantCallInfo = formatGrantCallInfo(currentAusschreibung);
        
        // Store the initial message in sessionStorage
        sessionStorage.setItem('initialChatMessage', grantCallInfo);
        
        // Navigate to chat page
        window.location.href = 'chat.html';
    });
}

function formatGrantCallInfo(parsedData) {
    let info = `Bitte starte den Antrags-Flow für die folgende Ausschreibung:\n\n`;
    info += `**Name:** ${parsedData.name}\n\n`;
    info += `**Themenbereich:** ${parsedData.topic}\n\n`;
    info += `**Kurzbeschreibung:** ${parsedData.shortDescription}\n\n`;
    info += `**Langbeschreibung:** ${parsedData.longDescription}\n\n`;
    info += `**Förderbetrag:** ${parsedData.grantCallSum}\n\n`;
    info += `**Bewerbungsfrist:** ${parsedData.applicationDeadlines.from} bis ${parsedData.applicationDeadlines.to}\n\n`;
    info += `**Regionen:** ${parsedData.regions}\n\n`;
    info += `**Zielgruppe:** ${parsedData.targetGroup}\n\n`;
    
    if (parsedData.contact && parsedData.contact.length > 0) {
        info += `**Kontakt:**\n`;
        parsedData.contact.forEach(contact => {
            info += `- ${contact.contactPerson}: ${contact.email}, Tel: ${contact.telephone}\n`;
        });
        info += `\n`;
    }
    
    info += `**Webseite:** ${parsedData.website}\n\n`;
    
    if (parsedData.optional && Object.keys(parsedData.optional).length > 0) {
        info += `**Optionale Informationen:**\n`;
        for (const key in parsedData.optional) {
            info += `- ${key}: ${parsedData.optional[key]}\n`;
        }
    }
    
    return info;
}
