pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Words Per Minute Counter"
include(":app")
include(":core:analytics:api")
include(":core:analytics:impl")
include(":core:user:api")
include(":core:user:impl")
include(":feature:login:api")
include(":feature:login:impl")
include(":feature:typing:api")
include(":feature:typing:impl")
include(":core:ui-common")
