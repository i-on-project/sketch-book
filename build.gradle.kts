plugins {
    kotlin("jvm") version "1.3.71"
}

group = "org.ionproject"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("com.networknt:json-schema-validator:1.0.38");
    implementation("org.json:json:20171018")
    testImplementation("junit:junit:4.13")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
