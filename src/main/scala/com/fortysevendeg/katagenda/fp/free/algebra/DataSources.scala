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

import com.fortysevendeg.katagenda.fp.Application._

import scalaz.Free._
import scalaz.Inject

/**
 * Datasource Algebra defining all possible generic operations that can be performed in a data store
 */
object datasources {

  sealed trait DataOp[A]

  case class Add[A](a: A) extends DataOp[Option[A]]

  case class GetAll[A]() extends DataOp[List[A]]

  class DataSource[F[_]](implicit I: Inject[DataOp, F]) {

    def add[A](a: A): FreeC[F, Option[A]] = lift[DataOp, F, Option[A]](Add[A](a))

    def getAll[A]: FreeC[F, List[A]] = lift[DataOp, F, List[A]](GetAll[A]())

  }

  /**
   * Implicits based DI
   */
  object DataSource {

    implicit def dataSource[F[_]](implicit I: Inject[DataOp, F]): DataSource[F] = new DataSource[F]

  }

}
