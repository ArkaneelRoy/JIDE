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
package com.willow.androidide.ultra.tooling.impl.sync

import com.willow.androidide.ultra.tooling.api.IGradleProject
import com.willow.androidide.ultra.tooling.api.messages.InitializeProjectParams
import com.willow.androidide.ultra.tooling.impl.internal.GradleProjectImpl
import org.gradle.tooling.model.GradleProject

/**
 * Builds model for root Gradle project (represented with [IGradleProject].
 *
 * @author Akash Yadav
 */
class GradleProjectModelBuilder(initializationParams: InitializeProjectParams) :
  AbstractModelBuilder<GradleProject, IGradleProject>(
    initializationParams) {

  @Throws(ModelBuilderException::class)
  override fun build(param: GradleProject): IGradleProject {
    return GradleProjectImpl(param)
  }
}