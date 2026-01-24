import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
  id("xyz.jpenilla.run-paper") version "3.0.2"
  id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.3.0"
}

group = "org.jufyer.plugin"
version = "1.0.0-SNAPSHOT"
description = "Aquatic Minecraft Plugin by Jufyer"

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://jitpack.io")

  maven("https://oss.sonatype.org/content/groups/public/")
  maven("https://maven.devs.beer/")
  maven("https://repo.oraxen.com/releases")
}

dependencies {
  paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")

  compileOnly("io.th0rgal:oraxen:1.181.0")
  compileOnly("dev.lone:api-itemsadder:4.0.10")
}

tasks {
  compileJava {
    options.release = 21
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
}

bukkitPluginYaml {
  main = "org.jufyer.plugin.aquatic.Main"
  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
  authors.add("Jufyer")
  apiVersion = "1.21"
  commands {
    register("spawnShark")
    register("spawnNibbler")
    register("spawnGoldfish")
    register("spawnWhale")
    register("spawnOyster")
  }
}
