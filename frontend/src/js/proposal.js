const params = new URLSearchParams(window.location.search);
const id = params.get('id');

if (!id) {
    console.error('Keine ID in der URL angegeben.');
} else {
    fetch("https://localhost/api/calls?type=PARSED")
        .then(response => response.json())
        .then(data => {
            const ausschreibungen = data.flat();
            const ausschreibung = ausschreibungen.find(item => item.call.id === Number(id));

            if (!ausschreibung) {
                console.error('Ausschreibung mit ID ' + id + ' nicht gefunden.');
                document.querySelector('body').innerHTML = '<div class="container mt-4"><h1>Ausschreibung nicht gefunden</h1></div>';
                return;
            }

            let parsedData = ausschreibung.parsedData

            document.getElementById("ausschreibungName").textContent = parsedData.name;
        })
        .catch(error => console.error('Fehler beim Laden der Details:', error));
}
