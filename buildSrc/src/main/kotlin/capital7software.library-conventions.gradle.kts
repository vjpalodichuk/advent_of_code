import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention

// Define Java Library conventions for this organization.
// Projects need to use the organization's Java conventions and publish using Maven Publish

plugins {
  `java-library`
  `maven-publish`
  id("capital7software.java-conventions")
}

// Projects have the 'com.capital7software.aoc' group by convention
group = "com.capital7software.aoc"

val artifactoryContextUrl: String by project
val artifactoryRepoKeyReadRelease: String by project
val artifactoryRepoKeyPublishRelease: String by project
val artifactoryUser: String by project
val artifactoryPassword: String by project

val javadocJar by tasks.named<Jar>("dokkaJavadocJar")
val htmlJar by tasks.named<Jar>("dokkaHtmlJar")

configure<PublishingExtension> {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      artifact(javadocJar)
      artifact(htmlJar)
    }
  }

  repositories {
    maven {
      name = "artifactory-publish"
      url = uri("${artifactoryContextUrl}/${artifactoryRepoKeyPublishRelease}/")
      credentials {
        username = artifactoryUser
        password = artifactoryPassword
      }
    }
  }
}

configure<ArtifactoryPluginConvention> {
  setContextUrl(artifactoryContextUrl)

  publish {
    contextUrl = artifactoryContextUrl
    repository {
      repoKey = artifactoryRepoKeyPublishRelease
      username = artifactoryUser
      password = artifactoryPassword
    }
    defaults {
      setPublishPom(true)
      setPublishArtifacts(true)
      publications("ALL_PUBLICATIONS")
    }
  }
}

// The project requires libraries to have a README containing sections configured below
val readmeCheck by tasks.registering(com.capital7software.ReadmeVerificationTask::class) {
  readme = layout.projectDirectory.file("README.md")
  readmePatterns = listOf("^## API$", "^## Changelog$")
}

tasks.named("check") { dependsOn(readmeCheck) }
