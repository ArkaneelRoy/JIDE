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

package com.willow.androidide.ultra.plugins.conf

import com.willow.androidide.ultra.build.config.BuildConfig
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

fun Project.configureJavaModule() {
  assert(plugins.hasPlugin("java-library")) {
    "${javaClass.simpleName} can only be applied to Java library projects."
  }

  configure<JavaPluginExtension> {
    sourceCompatibility = BuildConfig.javaVersion
    targetCompatibility = BuildConfig.javaVersion
  }
}