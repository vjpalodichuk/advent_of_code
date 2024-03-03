rootProject.name = "aoc"
plugins {
  id("com.mooltiverse.oss.nyx") version "2.5.1"
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("aoc-library")
include("aoc-2015")
include("aoc-2016")
include("aoc-2017")
include("aoc-2023")

configure<com.mooltiverse.oss.nyx.gradle.NyxExtension> {
  preset = "extended"

  initialVersion = "1.0.0"

  changelog {
    path = "CHANGELOG.md"
    append = "head"
  }

  commitMessageConventions {
    enabled.set(mutableListOf("conventionalCommits", "gitmoji", "conventionalCommitsForMerge"))
  }

  releaseTypes {
    publicationServices.set(listOf("github"))
  }

  services.create("github") {
    type = "GITHUB"
    options.apply {
      // The authentication token is read from the GH_TOKEN environment variable.
      put("AUTHENTICATION_TOKEN", "{{#environmentVariable}}GH_TOKEN{{/environmentVariable}}")
      put("REPOSITORY_NAME", "advent-of-code")
      put("REPOSITORY_OWNER", "vjpalodichuk")
    }
  }

  resume = false
  summary = true
  summaryFile = ".nyx-summary.txt"
  stateFile = ".nyx-state.yml"
}
