import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    jvm()
    js(IR) {
        browser {
            runTask {
                mainOutputFileName = "composeApp.js"
            }
            webpackTask {
                mainOutputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.frontend.kobwebCompose)
            implementation(compose.ui)
        }
        jsMain.dependencies {
            implementation(compose.html.core)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.varabyte.compose.demo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.compose.bricks"
            packageVersion = "1.0.0"
        }
    }
}