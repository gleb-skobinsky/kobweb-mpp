pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "kobweb"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":common:kobweb-common",
    ":common:kobweb-serialization",
    ":common:kobwebx-serialization-kotlinx",
    ":common:client-server-internal",
    ":frontend:kobweb-core",
    ":frontend:kobweb-compose",
    ":frontend:kobweb-silk",
    ":frontend:kobweb-worker",
    ":frontend:kobweb-worker-interface",
    ":frontend:silk-foundation",
    ":frontend:silk-widgets",
    ":frontend:silk-widgets-kobweb",
    ":frontend:silk-icons-fa",
    ":frontend:silk-icons-mdi",
    ":frontend:kobwebx-markdown",
    ":frontend:compose-html-ext",
    ":frontend:browser-ext",
    ":frontend:test:compose-test-utils",
    ":backend:kobweb-api",
    ":backend:server",
    ":backend:server-plugin",
    ":tools:gradle-plugins:core",
    ":tools:gradle-plugins:library",
    ":tools:gradle-plugins:application",
    ":tools:gradle-plugins:worker",
    ":tools:gradle-plugins:extensions:markdown",
    ":tools:ksp:site-processors",
    ":tools:ksp:worker-processor",
    ":tools:ksp:ksp-ext",
    ":tools:processor-common",
    ":demo"
)
