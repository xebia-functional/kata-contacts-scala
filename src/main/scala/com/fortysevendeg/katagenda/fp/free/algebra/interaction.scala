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

import scalaz.{Free, Inject}

/**
 * Interaction Algebra defining all interactions that we can perform with a user
 */
object interaction {

  sealed trait Interact[A]

  case class Ask(prompt: String) extends Interact[String]

  case class Tell(msg: String) extends Interact[Unit]

  class Interacts[F[_]](implicit I: Inject[Interact, F]) {

    def tell(msg: String): Free.FreeC[F, Unit] = lift(Tell(msg))

    def ask(prompt: String): Free.FreeC[F, String] = lift(Ask(prompt))

  }

  /**
   * Implicits based DI
   */
  object Interacts {

    implicit def interacts[F[_]](implicit ev: Inject[Interact, F]): Interacts[F] = new Interacts

  }

}

