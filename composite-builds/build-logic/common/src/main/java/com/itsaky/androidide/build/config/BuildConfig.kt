package com.willow.androidide.ultra.build.config/*
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

import org.gradle.api.JavaVersion

/**
 * Build configuration for the IDE.
 *
 * @author Akash Yadav
 */
object BuildConfig {

  /** AndroidIDE Ultra's package name, used as the Java namespace and Maven group ID. */
  const val packageName = "com.willow.androidide.ultra"

  /**
   * The application ID the IDE installs under, which fixes the Termux prefix at
   * "/data/data/$applicationId/files/usr".
   *
   * This is deliberately shorter than [packageName]. The prebuilt Termux packages have
   * "/data/data/com.itsaky.androidide/files/usr" compiled into their binaries, and that
   * path can only be rewritten in place if the replacement is no longer than the 42 bytes
   * it occupies. That leaves a budget of 21 characters for the application ID, which
   * "com.willow.androidide" matches exactly. Lengthening it past 21 characters breaks
   * every prebuilt binary, so keep it in sync with CURRENT_PREFIX in
   * scripts/publish-apt-mirror.py.
   */
  const val applicationId = "com.willow.androidide"

  /** The compile SDK version. */
  const val compileSdk = 34

  /** The minimum SDK version. */
  const val minSdk = 26

  /** The target SDK version. */
  const val targetSdk = 28

  const val ndkVersion = "26.1.10909125"

  /** The source and target Java compatibility. */
  val javaVersion = JavaVersion.VERSION_11
}
