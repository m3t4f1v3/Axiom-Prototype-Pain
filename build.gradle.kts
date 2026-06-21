import org.spongepowered.asm.gradle.plugins.MixinExtension
import org.spongepowered.asm.gradle.plugins.struct.DynamicProperties
import java.text.SimpleDateFormat
import java.util.*

buildscript {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.21")
        classpath("org.spongepowered:mixingradle:0.7.+")
    }
}

apply(plugin = "kotlin")
apply(plugin = "org.spongepowered.mixin")

plugins {
    eclipse
    idea
    `maven-publish`
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    id("org.jetbrains.kotlin.jvm") version "2.2.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.21"
}

group = "com.axiom"
version = "1.20-0.1.9"

val modid = "axiom_pain"
val vendor = "axiom"

val minecraftVersion = "1.20.1"
val forgeVersion = "47.4.10"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

println(
    "Java: ${System.getProperty("java.version")} JVM: ${System.getProperty("java.vm.version")}(${
        System.getProperty(
            "java.vendor"
        )
    }) Arch: ${System.getProperty("os.arch")}"
)

minecraft {
    mappings("official", minecraftVersion)
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs.all {
        mods {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", modid)
            property("terminal.jline", "true")
            mods {
                create(modid) {
                    source(sourceSets.main.get())
                }
            }
        }
    }

    runs.run {
        create("client") {
            property("log4j.configurationFile", "log4j2.xml")
            args("--username", "Player")
        }

        create("server") {}
        create("gameTestServer") {}
        create("data") {
            workingDirectory(project.file("run"))
            args(
                "--mod",
                modid,
                "--all",

                "--output",
                file("src/generated/resources/"),
                "--existing",
                file("src/main/resources")
            )
        }
    }
}

sourceSets.main.configure { resources.srcDirs("src/generated/resources/") }

repositories {
    mavenCentral()
    maven("https://jitpack.io") // MixinExtras is often hosted here or via LLibrary
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
    maven {
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    maven {
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven {
        name = "Valkyrien Skies Internal"
        url = uri("https://maven.valkyrienskies.org")
        content {
            includeGroup("org.valkyrienskies")
            includeGroup("org.valkyrienskies.core")
        }
    }

}

fun getProperty(name: String): String {
    return project.findProperty(name)?.toString() ?: System.getProperty(name)
}

dependencies {
    minecraft("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    implementation("thedarkcolour:kotlinforforge:4.12.0")
    api(fg.deobf("curse.maven:blood-bits-984445:7353388"))
    api(fg.deobf("curse.maven:casualties-cubed-1539563:8188152"))
    api(fg.deobf("maven.modrinth:homeostatic:1.20.1-2.9.16.2-FORGE"))
    api(fg.deobf("maven.modrinth:glitchcore:0.0.1.1-forge"))
    api(fg.deobf("maven.modrinth:crackers-wither-storm-mod:4.2.1"))
    api(fg.deobf("maven.modrinth:epic-fight:20.14.17-mc1.20.1-forge"))

    // compileOnly(fg.deobf("com.github.thermodynamica:thermodynamica:0.4.1"))
    // runtimeOnly(fg.deobf("com.github.thermodynamica:thermodynamica:0.4.1"))

    implementation("io.github.llamalad7:mixinextras-common:0.5.2")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.2")
    implementation("io.github.llamalad7:mixinextras-forge:0.5.2")

    api(fg.deobf("curse.maven:valkyrien-skies-258371:7418457"))

    api("org.valkyrienskies.core:api:1.1.0+a5203f1d01") {
        isTransitive = false
        exclude(group = "org.joml", module = "")
    }
    api("org.valkyrienskies.core:util:1.1.0+a5203f1d01") {
        isTransitive = false
        exclude(group = "org.joml", module = "")
    }
    api("org.valkyrienskies.core:internal:1.1.0+a5203f1d01") {
        isTransitive = false
        exclude(group = "org.joml", module = "")
    }


}

val Project.mixin: MixinExtension
    get() = extensions.getByType()

mixin.run {
    add(sourceSets.main.get(), "axiom_pain.mixins.refmap.json")
    config("axiom_pain.mixins.json")
    val debug = this.debug as DynamicProperties
    debug.setProperty("verbose", true)
    debug.setProperty("export", true)
    setDebug(debug)
}

tasks.withType<Jar> {
    archiveBaseName.set(modid)
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to modid,
                "Specification-Vendor" to vendor,
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version.toString(),
                "Implementation-Vendor" to vendor,
                "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
            )
        )
    }
    finalizedBy("reobfJar")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${project.projectDir}/mcmodsrepo")
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}