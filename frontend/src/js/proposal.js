const params = new URLSearchParams(window.location.search);
const jsonFileName = params.get('json');

fetch("../mock/" + jsonFileName)
    .then(response => response.json())
    .then(ausschreibung => {
        document.getElementById("ausschreibungName").textContent = ausschreibung.Name;
    })
    .catch(error => console.error('Fehler beim Laden der Details:', error));