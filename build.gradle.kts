val detektVersion = "1.15.0"

plugins {
    kotlin("multiplatform") version "1.4.21" apply false
    id("com.vanniktech.maven.publish") version "0.13.0" apply false
    id("org.jetbrains.dokka") version "1.4.10.2"
    id("io.gitlab.arturbosch.detekt") version "1.15.0"
    id("binary-compatibility-validator") version "0.3.0"
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

tasks.dokkaHtmlMultiModule.configure {
    val apiDir = rootDir.resolve("docs/api")
    outputDirectory.set(apiDir)
    doLast {
        apiDir.resolve("-modules.html").renameTo(apiDir.resolve("index.html"))
    }
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        input = files("src/")
        failFast = true
        config = files("${project.rootDir}/detekt.yml")
        buildUponDefaultConfig = true
        reports {
            html.destination = file("${project.buildDir}/reports/detekt/${project.name}.html")
        }
    }
    dependencies.add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:${detektVersion}")
}
