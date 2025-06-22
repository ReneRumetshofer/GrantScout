# GrantScout

## Project structure (services)

1. Frontend (HTML/CSS)
2. Backend (Java)
    1. Scraping
    2. Parser
    3. API for Frontend
3. Database (Postgres)

## Local setup

### Install Software

* Docker (e.g. [Docker Desktop](https://www.docker.com/get-started/) for Windows and Linux
  or [Docker Engine](https://docs.docker.com/engine/install/) for Linux)
* [Java 21 SDK](https://www.oracle.com/de/java/technologies/downloads/#java21)
* [Python 3 (optional)](https://www.python.org/downloads/)

### Start GrantScout (one service)

* Backend (without Docker)
    * IntelliJ IDEA: open Gradle view and execute `application > bootRun`. Make sure to select the correct profile
      (e.g. `local` or `prod`) in the _Run/Debug Configurations_.
    * VS Code: ?
    * Terminal: go to folder _[backend](backend/)_ and enter `./gradlew bootRun`
* Frontend (only with Docker)
    * IntelliJ IDEA: open _[docker-compose.yml](docker-compose.yml)_ and press on the play button in the line with
      `frontend`
    * VS Code: ?
    * Terminal: enter `docker compose up -d frontend`
* Database (only with Docker)
    * IntelliJ IDEA: open _[docker-compose.yml](docker-compose.yml)_ and press on the play button in the line with
      `postgres`
    * VS Code: ?
    * Terminal: enter `docker compose up -d postgres`

**Note**: To start the backend, the database needs to be up and reachable.
Therefore, the path to the database must be adjusted if the backend is not started within Docker from
`jdbc:postgresql://postgres:5432/<database>` to `jdbc:postgresql://localhost:5432/<database>` in the following files:

* [backend/src/main/resources/application.yaml](backend/src/main/resources/application.yaml)
* [backend/build.gradle.kts](backend/build.gradle.kts)

### Start GrantScout (all services)

1. create _.env_ file in root directory with following content and replace fields with `<...>`-annotations (do NOT push
   to repository). See `example.env` for local setup:
    ```properties
    DB_NAME=<database>      # choose whatever you want
    DB_USER=<username>      # choose whatever you want
    DB_PASSWORD=<password>  # choose whatever you want
    DB_HOST=postgres
    DB_PORT=5432
    
    OPENAI_API_KEY=<key>    # generate api-key in openai
    ```
2. Build Java Jar (currently not done by Docker):
    * IntelliJ IDEA: open Gradle view and execute `build > bootJar`
    * VS Code: ?
    * Terminal: go to folder _[backend](backend/)_ and enter `./gradlew bootJar`
3. Run Docker
    * IntelliJ IDEA: open _[docker-compose.yml](docker-compose.yml)_ and press on the play button in the first line
    * VS Code: ?
    * Terminal: enter `docker compose up --build -d`

## Working with Docker

### With Docker Desktop:

* Start or stop containers
* See logs
* Execute commands within the containers shell

### Within the terminal:

* `docker ps -a`... show all container
* `docker start <id>`... start container
* `docker stop <id>`... stop container
* `docker logs <id>`... show container logs
* `docker exec -it <id> <command>`... execute command within a container

Example to access database:

1. `docker ps`... look out for _database_-container and its id
2. `docker exec -it <id> psql -U <username> <database>`... open terminal in _database_-container
3. `SELECT * FROM <table>;`... start using SQL commands within database
4. `exit`... leave _database_-container