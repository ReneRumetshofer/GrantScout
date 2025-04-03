let data = [];
let jsonFiles = ["input.json", "input2.json", "input3.json"];

document.addEventListener("DOMContentLoaded", function () {
    let promises = jsonFiles.map(file =>
        fetch("mock/" + file)
            .then(res => res.json())
            .then(e => {
                e.JsonDatei = file;
                return e
            })
    );

    Promise.all(promises)
        .then(jsonDataArray => {
            data = jsonDataArray.flat();
            populateFilters();
            renderTable();
        })
        .catch(error => console.error("Fehler beim Laden der Daten:", error));
});

function populateFilters() {
    let themenbereiche = new Set();
    data.forEach(item => themenbereiche.add(item.Themenbereich));
    let filterDropdown = document.getElementById("filterThemenbereich");
    themenbereiche.forEach(thema => {
        let option = document.createElement("option");
        option.value = thema;
        option.textContent = thema;
        filterDropdown.appendChild(option);
    });
}

function renderTable() {
    let searchQuery = document.getElementById("search").value.toLowerCase();
    let selectedThemenbereich = document.getElementById("filterThemenbereich").value;
    let selectedDate = document.getElementById("filterBewerbungsfrist").value;

    let filteredData = data.filter(item => {
        let matchesSearch = Object.values(item).some(value =>
            (typeof value === "string" && value.toLowerCase().includes(searchQuery)) ||
            (typeof value === "object" && Object.values(value).some(v => v.toString().toLowerCase().includes(searchQuery)))
        );
        let matchesThemenbereich = selectedThemenbereich === "" || item.Themenbereich === selectedThemenbereich;
        let matchesDate = selectedDate === "" || item.Bewerbungsfrist.Bis >= selectedDate;
        return matchesSearch && matchesThemenbereich && matchesDate;
    });

    let listings = document.getElementById("listings");
    listings.innerHTML = "";
    filteredData.forEach(item => {
        let row = document.createElement("tr");
        row.innerHTML = `
                <td><a href="${item.Webseite}" target="_blank">${item.Name}</a></td>
                <td>${item.Themenbereich}</td>
                <td>${item.Kurzbeschreibung}</td>
                <td>${item.Förderbetrag}</td>
                <td>${item.Bewerbungsfrist.Bis}</td>
                <td>
                    <button class="btn btn-primary" onclick="createProposal('${item.JsonDatei}')">Antrag erstellen</button>
                    <button class="btn btn-info" onclick="showDetails('${item.JsonDatei}')">Details anzeigen</button>
                </td>
            `;
        listings.appendChild(row);
    });
}

document.getElementById("search").addEventListener("input", renderTable);
document.getElementById("filterThemenbereich").addEventListener("change", renderTable);
document.getElementById("filterBewerbungsfrist").addEventListener("input", renderTable);

function showDetails(jsonFileName) {
    window.location.href = 'details.html?json=' + jsonFileName;
}

function createProposal(jsonFileName) {
    window.location.href = 'proposal.html?json=' + jsonFileName;
}