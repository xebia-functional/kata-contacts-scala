/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.fortysevendeg.katagenda.common.repository

trait DataSource[T] {

  def getAll: Seq[T]

  def add(item: T): T

}

class InMemoryDataSource[T] extends DataSource[T] {

  private[this] val list = scala.collection.mutable.ArrayBuffer.empty[T]

  override def getAll: Seq[T] = list

  override def add(item: T): T = {
    list += item
    item
  }
}
