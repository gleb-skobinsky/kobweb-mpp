import com.varabyte.kobweb.gradle.publish.FILTER_OUT_MULTIPLATFORM_PUBLICATIONS
import com.varabyte.kobweb.gradle.publish.set

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("com.varabyte.kobweb.internal.publish")
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

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            api(projects.frontend.kobwebCompose)
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

            implementation(projects.frontend.composeHtmlExt)
        }
        jsTest.dependencies {
            implementation(kotlin("test"))
            implementation(projects.frontend.test.composeTestUtils)
            implementation(libs.truthish)
        }
    }
}

/*
kobwebPublication {
    artifactId.set("silk-foundation")
    description.set("The foundational layer of Silk that provides general purpose styling functionality like component styles, keyframes, and .")
    filter.set(FILTER_OUT_MULTIPLATFORM_PUBLICATIONS)
}

 */

android {
    namespace = "com.varabyte.kobweb.silk"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
}