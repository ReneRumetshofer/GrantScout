const express = require('express');
const { Client } = require('pg');
const app = express();
const port = 3000;

const client = new Client({
    host: process.env.DB_HOST,
    port: process.env.DB_PORT,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
});

client.connect();

app.get('/api/grants', async (req, res) => {
    try {
        const result = await client.query('SELECT * FROM grants');
        res.json(result.rows);
    } catch (error) {
        console.error('Fehler beim Abrufen der Daten:', error);
        res.status(500).send('Interner Serverfehler');
    }
});

app.get('/api/grant/{id}', async (req, res) => {
    try {
        const result = await client.query('SELECT * FROM grants WHERE id = ""');
        res.json(result.rows);
    } catch (error) {
        console.error('Fehler beim Abrufen der Daten:', error);
        res.status(500).send('Interner Serverfehler');
    }
});

app.listen(port, () => {
    console.log(`Server läuft auf http://localhost:${port}`);
});
