import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("java-library")
    id("signing")
    id("com.vanniktech.maven.publish") version "0.32.0"
}

group = "io.github.nowipi"
version = "1.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

signing {
    useInMemoryPgpKeys(
        project.findProperty("signing.keyId") as String?,
        project.findProperty("signing.secretKey") as String?,
        project.findProperty("signing.password") as String?
    )
}


mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates("io.github.nowipi.jio", "jio", project.version.toString())

    pom {
        name.set("JIO")
        description.set("An extension of Java NIO")
        url.set("https://github.com/Nowipi/JIO/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("Nowipi")
                name.set("Noah Uyttebroeck")
                url.set("https://github.com/Nowipi/")
            }
        }
        scm {
            url.set("https://github.com/Nowipi/JIO/")
            connection.set("scm:git:git://github.com/Nowipi/jio.git")
            developerConnection.set("scm:git:ssh://git@github.com/Nowipi/jio.git")
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("io.github.nowipi:ffmprocessor:1.0-SNAPSHOT")
    annotationProcessor("io.github.nowipi:ffmprocessor:1.0-SNAPSHOT")
}