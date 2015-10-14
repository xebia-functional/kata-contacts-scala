/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may
 *   not use this file except in compliance with the License. You may obtain
 *   a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.fortysevendeg.katagenda.fp.free.algebra

import com.fortysevendeg.katagenda.fp.free.algebra.datasources.DataSource

import scalaz.Free._

/**
 * Delegates to datasource for persistence related ops
 */
object repositories {

  class Repository[F[_]](implicit D: DataSource[F]) {

    def add[A](a: A): FreeC[F, Option[A]] = D.add(a)

    def getAll[A]: FreeC[F, List[A]] = D.getAll

  }

  /**
   * Implicits based DI
   */
  object Repository {

    implicit def repository[F[_]](implicit D: DataSource[F]): Repository[F] = new Repository[F]

  }

}


