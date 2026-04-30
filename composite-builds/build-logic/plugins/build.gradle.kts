/*
 *  This file is part of AndroidIDE Ultra.
 *
 *  AndroidIDE Ultra is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE Ultra is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE Ultra.  If not, see <https://www.gnu.org/licenses/>.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

repositories {
  google()
  gradlePluginPortal()
  mavenCentral()
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}

dependencies {
  implementation(projects.buildLogic.common)
  implementation(projects.buildLogic.desugaring)
  implementation(projects.buildLogic.propertiesParser)

  implementation("com.android.tools.build:gradle:${libs.versions.agp.asProvider().get()}")
  implementation(libs.maven.publish)

  implementation(libs.common.jkotlin)
  implementation(libs.common.antlr4)
  implementation(libs.google.gson)
  implementation(libs.google.java.format)
}

gradlePlugin {
  plugins {
    create("com.willow.androidide.ultra.build") {
      id = "com.willow.androidide.ultra.build"
      implementationClass = "com.willow.androidide.ultra.plugins.AndroidIDE UltraPlugin"
    }
    create("com.willow.androidide.ultra.core-app") {
      id = "com.willow.androidide.ultra.core-app"
      implementationClass = "com.willow.androidide.ultra.plugins.AndroidIDE UltraCoreAppPlugin"
    }
    create("com.willow.androidide.ultra.build.propsparser") {
      id = "com.willow.androidide.ultra.build.propsparser"
      implementationClass = "com.willow.androidide.ultra.plugins.PropertiesParserPlugin"
    }
    create("com.willow.androidide.ultra.build.lexergenerator") {
      id = "com.willow.androidide.ultra.build.lexergenerator"
      implementationClass = "com.willow.androidide.ultra.plugins.LexerGeneratorPlugin"
    }
  }
}
