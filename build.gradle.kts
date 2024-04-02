import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("application")
    id("jacoco")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.13.2")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

jacoco {
    toolVersion = "0.8.7"
}

tasks.withType<JacocoReport> {
    reports {
        html.isEnabled = true
        xml.isEnabled = true
        csv.isEnabled = false
        html.destination = file("$buildDir/reports/jacoco/html")
        xml.destination = file("$buildDir/reports/jacoco/xml")
    }
}
