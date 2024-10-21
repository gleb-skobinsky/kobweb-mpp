import com.varabyte.kobweb.gradle.publish.FILTER_OUT_MULTIPLATFORM_PUBLICATIONS
import com.varabyte.kobweb.gradle.publish.set

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
}

group = "com.varabyte.kobweb"
version = libs.versions.kobweb.libs.get()

kotlin {
    js {
        browser()
    }
    androidTarget()
    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

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
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)

            api(projects.frontend.composeHtmlExt)
        }

        jsTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.truthish)
        }
    }
}

/*
kobwebPublication {
    artifactId.set("kobweb-compose")
    description.set("Additions to Web Compose that attempt to mimic Jetpack Compose as much as possible")
    filter.set(FILTER_OUT_MULTIPLATFORM_PUBLICATIONS)
}
 */

android {
    namespace = "com.varabyte.kobweb.compose"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
}