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

package com.fortysevendeg.katagenda.fp

import com.fortysevendeg.katagenda.fp.free.algebra.datasources.DataOp
import com.fortysevendeg.katagenda.fp.free.algebra.interaction.Interact
import com.fortysevendeg.katagenda.fp.free.interpreters.interpreters._
import com.fortysevendeg.katagenda.fp.ui.ContactsListPresenter

import scala.language.reflectiveCalls
import scalaz.Free._
import scalaz._

/**
 * Core types, definition and Free monads support
 */
object Application {

  /**
   * An application is nothing but the Coproduct of the Free Monadic Algebras that composes it
   */
  type AgendaApp[A] = Coproduct[DataOp, Interact, A]

  /**
   * Coyoneda gives us a free Functor on AgendaApp
   */
  type ACoyo[A] = Coyoneda[AgendaApp,A]

  /**
   * The Free monad on our application
   */
  type AFree[A] = Free[ACoyo,A]

  /**
   * Point any value to operate monadically in our application
   * @return
   */
  def point[A](a: => A): FreeC[AgendaApp, A] = Monad[AFree].point(a)

  /**
   * Lifts Algebra to a Free monad + Coyoneda with Injection capabilities
   */
  def lift[F[_], G[_], A](fa: F[A])(implicit I: Inject[F, G]): FreeC[G, A] = Free.liftFC(I.inj(fa))

  /**
   * A natural transformation that converts our suspended application folding over it's structure and
   * selectively delegates to interpreters addressing each of it's algebras
   */
  val interpreters: AgendaApp ~> Id.Id = or(InMemoryDatasourceInterpreter, ConsoleContactReader)

  /**
   * Lifts the natural transformation to coyoneda
   */
  val coyoint: ({type f[x] = Coyoneda[AgendaApp, x]})#f ~> Id.Id = Coyoneda.liftTF(interpreters)

  /**
   * The structure of our program free of interpretation
   */
  def prg(implicit CP : ContactsListPresenter[AgendaApp]) = for {
    _ <- CP.onInitialize
    _ <- Monad[AFree].replicateM(2, CP.onAddContactOptionSelected)
    _ <- CP.onStop
  } yield ()

  /**
   * Runs our program through the provided interpreters applying meaning to our datasource and interaction algebras
   */
  def runApp = prg.mapSuspension(coyoint)

}
