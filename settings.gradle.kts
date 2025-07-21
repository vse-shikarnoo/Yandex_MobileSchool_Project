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

rootProject.name = "Project"
include(":app")
include(":feature:settings")
include(":feature:main")
include(":core")
include(":feature:splash")
include(":feature:category")
include(":feature:income")
include(":feature:expenses")
include(":feature:account")
include(":feature:expenses:sample")
include(":feature:charts")
include(":feature:charts")
