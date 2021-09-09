import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
  id("org.flywaydb.flyway") version "7.14.0"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.1.2"
val junitJupiterVersion = "5.7.0"
val logbackVersion = "1.2.3"
val flywayVersion = "7.14.0"
val slf4jVersion = "1.7.30"
val testcontainersVersion = "1.15.0-rc2"

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
  implementation("org.flywaydb:flyway-core:$flywayVersion")
  implementation("io.vertx:vertx-rx-java3")
  implementation("io.vertx:vertx-codegen:$vertxVersion")
  implementation("io.vertx:vertx-service-proxy:$vertxVersion")
  implementation("org.slf4j:slf4j-api:$slf4jVersion")
  implementation("ch.qos.logback:logback-classic:$logbackVersion")
  implementation("io.vertx:vertx-config")
  implementation("io.vertx:vertx-web-sstore-redis:$vertxVersion")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")

  testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
  testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
  testImplementation("org.testcontainers:postgresql:1.16.0")

  implementation("org.postgresql:postgresql:42.2.23")

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  testImplementation("io.reactiverse:reactiverse-junit5-web-client:0.3.0")


  annotationProcessor("io.vertx:vertx-rx-java3-gen:$vertxVersion")
  annotationProcessor("io.vertx:vertx-codegen:$vertxVersion:processor")
  annotationProcessor("io.vertx:vertx-service-proxy:$vertxVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

flyway {
  url = "jdbc:postgresql://localhost:5432/fra_pizza"
  user = "postgres"
  password = "postgres"
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  environment("DATASOURCE_PORT", 5432)
  environment("DATASOURCE_URL", "jdbc:postgresql://localhost:5432/fra_pizza")
  environment("DATASOURCE_HOST", "localhost")
  environment("DATASOURCE_DBNAME", "fra_pizza")
  environment("DATASOURCE_USERNAME","postgres")
  environment("REDIS_URI","redis://:MxsB0SSrcKJ7QWbt6mhJ6ASHzj382RTS@redis-11295.c257.us-east-1-3.ec2.cloud.redislabs.com:11295/0")
  environment("DATASOURCE_PASSWORD","postgres")
  environment("DATASOURCE_USERNAME","postgres")

  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  environment("DATASOURCE_URL", "jdbc:postgresql://localhost:5432/fra_pizza")
  environment("DATASOURCE_PORT", 5432)
  environment("DATASOURCE_HOST", "localhost")
  environment("DATASOURCE_DBNAME", "fra_pizza")
  environment("DATASOURCE_USERNAME","postgres")
  environment("REDIS_URI","redis://:MxsB0SSrcKJ7QWbt6mhJ6ASHzj382RTS@redis-11295.c257.us-east-1-3.ec2.cloud.redislabs.com:11295/0")
  environment("DATASOURCE_PASSWORD","postgres")
  environment("DATASOURCE_USERNAME","postgres")
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}

tasks.getByName<JavaCompile>("compileJava") {
  options.annotationProcessorGeneratedSourcesDirectory = File("$projectDir/src/main/generated")
}
