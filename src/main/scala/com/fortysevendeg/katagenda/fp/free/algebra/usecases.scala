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


import model.{Agenda, Contact}

import scalaz.Free.FreeC

/**
 * Application use cases similar to those seen in the clean architecture that delegate ops to the Agenda
 */
object usecases {

  class UseCases[F[_]](implicit A: Agenda[F]) {

    def addContact(contact: Contact): FreeC[F, Option[Contact]] = A.addContact(contact)

    def getContacts: FreeC[F, List[Contact]] = A.getContacts

  }

  /**
   * Implicits based DI
   */
  object UseCases {

    implicit def useCases[F[_]](implicit A: Agenda[F]): UseCases[F] = new UseCases[F]

  }

}