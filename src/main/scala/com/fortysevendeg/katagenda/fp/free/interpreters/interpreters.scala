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

package com.fortysevendeg.katagenda.fp.free.interpreters

import com.fortysevendeg.katagenda.fp.free.algebra.datasources._
import com.fortysevendeg.katagenda.fp.free.algebra.interaction._

import scala.collection.mutable.ListBuffer
import scala.io.StdIn
import scalaz._

/**
 * The actual application runtime that transforms a free monad structure into actual concrete types and deals
 * with each case presented in the algebra.
 */
object interpreters {

  /**
   * Allows logical branching of the coproducts resulting from combining different algebras selecting an
   * interpreter f or g based on the algebra getting evaluated
   */
  def or[F[_], G[_], H[_]](f: F ~> H, g: G ~> H): ({type cp[α] = Coproduct[F, G, α]})#cp ~> H =
    new NaturalTransformation[({type cp[α] = Coproduct[F, G, α]})#cp, H] {
      def apply[A](fa: Coproduct[F, G, A]): H[A] = fa.run match {
        case -\/(ff) => f(ff)
        case \/-(gg) => g(gg)
      }
    }

  /**
   * An implementation of interaction based on the command line
   */
  object ConsoleContactReader extends (Interact ~> Id.Id) {
    def apply[A](i: Interact[A]) = i match {
      case Ask(prompt) =>
        println(prompt)
        StdIn.readLine()
      case Tell(msg) =>
        println(msg)
    }
  }

  /**
   * An implementation of data operations based on an in memory storage
   */
  object InMemoryDatasourceInterpreter extends (DataOp ~> Id.Id) {

    private[this] val memDataSet = new ListBuffer[Id.Id[_]]

    override def apply[A](fa: DataOp[A]) = fa match {
      case Add(a : A) => memDataSet.append(a); a
      case GetAll() => memDataSet.toList
    }
  }

}
