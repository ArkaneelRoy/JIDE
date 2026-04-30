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

package com.willow.androidide.ultra.tooling.events.task

import com.willow.androidide.ultra.tooling.events.FinishEvent

/** @author Akash Yadav */
class TaskFinishEvent(
  displayName: String,
  eventTime: Long,
  descriptor: TaskOperationDescriptor,
  override val result: TaskOperationResult
) : TaskProgressEvent(displayName, eventTime, descriptor), FinishEvent
