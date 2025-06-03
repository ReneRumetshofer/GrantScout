let data = [];
//let jsonFiles = ["input.json", "input2.json", "input3.json"];

document.addEventListener("DOMContentLoaded", function () {
    /*let promises = jsonFiles.map(file =>
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
            console.log(data)
            populateFilters();
            renderTable();
        })
        .catch(error => console.error("Fehler beim Laden der Daten:", error));*/

    fetch("https://localhost/api/calls?type=PARSED")
        .then(res => res.json())
        .then(json => {
            data.push(...json.flat())
            console.log(data)
            populateFilters();
            renderTable();
        })
});

function populateFilters() {
    let themenbereiche = new Set();
    data.forEach(item => themenbereiche.add(item.parsedData.topic));
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
        let parsedData = item.parsedData
        let matchesSearch = Object.values(item).some(value =>
            (typeof value === "string" && value.toLowerCase().includes(searchQuery)) ||
            (typeof value === "object" && Object.values(value).some(v => v.toString().toLowerCase().includes(searchQuery)))
        );
        let matchesThemenbereich = selectedThemenbereich === "" || parsedData.topic === selectedThemenbereich;
        let matchesDate = selectedDate === "" || parsedData.applicationDeadlines.from >= selectedDate;
        return matchesSearch && matchesThemenbereich && matchesDate;
    });

    let listings = document.getElementById("listings");
    listings.innerHTML = "";
    filteredData.forEach(item => {
        let parsedData = item.parsedData
        let row = document.createElement("tr");
        row.innerHTML = `
                <td><a href="${parsedData.website}" target="_blank">${parsedData.name}</a></td>
                <td>${parsedData.topic}</td>
                <td>${parsedData.shortDescription}</td>
                <td>${parsedData.grantCallSum}</td>
                <td>${parsedData.applicationDeadlines.from}</td>
                <td>
                    <button class="btn btn-primary" onclick="createProposal('${item.call.id}')">Antrag erstellen</button>
                    <button class="btn btn-info" onclick="showDetails('${item.call.id}')">Details anzeigen</button>
                </td>
            `;
        listings.appendChild(row);
    });
}

document.getElementById("search").addEventListener("input", renderTable);
document.getElementById("filterThemenbereich").addEventListener("change", renderTable);
document.getElementById("filterBewerbungsfrist").addEventListener("input", renderTable);

function showDetails(jsonFileName) {
    window.location.href = 'html/details.html?id=' + jsonFileName;
}

function createProposal(jsonFileName) {
    window.location.href = 'html/proposal.html?id=' + jsonFileName;
}