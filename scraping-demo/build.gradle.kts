plugins {
    id("java")
    id("org.flywaydb.flyway") version "11.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.1.0")
    }
}
dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.microsoft.playwright:playwright:1.43.0")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("org.flywaydb:flyway-core:11.1.0")
    implementation("org.flywaydb:flyway-database-postgresql:11.1.0")
    runtimeOnly("org.postgresql:postgresql:42.7.4")
}

flyway {
    url = "jdbc:postgresql://localhost:5432/grantscout-scraping"
    user = "gs"
    password = "Password12345678"
}

tasks.test {
    useJUnitPlatform()
}