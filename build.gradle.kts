import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.apache.tools.ant.filters.ReplaceTokens
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    application
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
}

apply(from = "aliyunRepo.gradle.kts")

group = "io.github.mainyf"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.7")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.7")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.mainyf:common_lib:0.1")
    implementation("commons-io:commons-io:2.6")
    implementation("commons-codec:commons-codec:1.10")
    implementation("commons-beanutils:commons-beanutils:1.9.3")
    implementation("com.esotericsoftware:reflectasm:1.11.9")
    implementation("io.jsonwebtoken:jjwt-api:0.10.7")
    implementation("io.jsonwebtoken:jjwt-impl:0.10.7")
    implementation("io.jsonwebtoken:jjwt-jackson:0.10.7")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

val ENV_KEY = "SPRING_PROFILES_ACTIVE"

tasks.withType<ProcessResources> {
    from("src/main/resources") {
        include("application.yml")
        filter<ReplaceTokens>("tokens" to mapOf("env_file" to if(project.hasProperty(ENV_KEY)) project.property(ENV_KEY) else "dev"))
    }
}

//application {
//    applicationDefaultJvmArgs = listOf("-Dfile.encoding=UTF-8")
//}

tasks.withType<BootRun> {
    workingDir = project.file("runDir")
}