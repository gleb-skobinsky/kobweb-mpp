plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
}

group = "com.varabyte.kobweb"
version = libs.versions.kobweb.libs.get()

group = "com.varabyte.kobweb"
version = libs.versions.kobweb.libs.get()

kotlin {
    js {
        browser()
    }
    androidTarget()
    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
        }
        val jbMain by creating {
            dependsOn(commonMain.get())
        }
        iosMain { dependsOn(jbMain) }
        iosX64Main { dependsOn(jbMain) }
        iosArm64Main { dependsOn(jbMain) }
        iosSimulatorArm64Main { dependsOn(jbMain) }
        androidMain { dependsOn(jbMain) }
        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
            dependsOn(jbMain)
        }
        jsMain.dependencies {
            implementation(libs.compose.html.core)
            api(projects.frontend.composeHtmlExt)
        }

        jsTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.truthish)
        }
    }
}

android {
    namespace = "com.varabyte.kobweb.compose.material3"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
}