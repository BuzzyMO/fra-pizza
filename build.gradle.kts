import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.1.2"
val junitJupiterVersion = "5.7.0"
val logbackVersion = "1.2.3"
val hibernateReactVersion = "1.0.0.CR9"
val flywayVersion = "7.14.0"
val slf4jVersion = "1.7.30"

val mainVerticleName = "com.example.frapizza.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-pg-client")
  implementation("org.hibernate.reactive:hibernate-reactive-core:$hibernateReactVersion")
  implementation("org.flywaydb:flyway-core:$flywayVersion")
  implementation("io.vertx:vertx-rx-java3")
  implementation("io.vertx:vertx-codegen:$vertxVersion")
  implementation("io.vertx:vertx-service-proxy:$vertxVersion")
  implementation("org.slf4j:slf4j-api:$slf4jVersion")
  implementation("ch.qos.logback:logback-classic:$logbackVersion")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")

  annotationProcessor("io.vertx:vertx-rx-java3-gen:$vertxVersion")
  annotationProcessor("io.vertx:vertx-codegen:$vertxVersion:processor")
  annotationProcessor("io.vertx:vertx-service-proxy:$vertxVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}

tasks.getByName<JavaCompile>("compileJava") {
  options.annotationProcessorGeneratedSourcesDirectory = File("$projectDir/src/main/generated")
}
