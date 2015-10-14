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

import com.fortysevendeg.katagenda.fp.free.algebra.repositories.Repository

import scalaz.Free.FreeC
import scalaz.NonEmptyList
import scalaz.Validation._
import scalaz.syntax.validation._

/**
 * Model datata types composed by Contact and Agenda with the operations that can be performed on Contacts
 */
object model {

  /**
   * A type alias representing a validation error when entering bad inputs
   */
  type InputError = String

  type FirstName = String
  type LastName = String
  type PhoneNumber = String

  case class Contact(
      firstName: FirstName,
      lastName: LastName,
      phoneNumber: PhoneNumber)

  class Agenda[F[_]](implicit R : Repository[F]) {

    private[this] val validName = "^[\\p{L} .'-]+$".r

    private[this] val validPhoneNumber = "^\\+?[0-9. ()-]{10,25}$".r

    def getContacts: FreeC[F, List[Contact]] = R.getAll[Contact]

    def addContact(contact : Contact): FreeC[F, Option[Contact]] = R.add(contact)

    /**
     * Validates a firstName returning a ValidationNel of NonEmptyList[InputError] \?/ FirstName
     */
    def validateFirstName(firstName : FirstName) : NonEmptyList[InputError] \?/ FirstName =
      firstName match {
        case validName() => firstName.successNel
        case _ => s"Invalid first name : $firstName".failureNel
      }

    /**
     * Validates a lastName returning a ValidationNel of NonEmptyList[InputError] \?/ LastName
     */
    def validateLastName(lastName : LastName) : NonEmptyList[InputError] \?/ LastName =
      lastName match {
        case validName() => lastName.successNel
        case _ => s"Invalid last name : $lastName".failureNel
      }

    /**
     * Validates a phoneNumber returning a ValidationNel of NonEmptyList[InputError] \?/ PhoneNumber
     */
    def validatePhoneNumber(phoneNumber : PhoneNumber) : NonEmptyList[InputError] \?/ PhoneNumber =
      phoneNumber match {
        case validPhoneNumber() => phoneNumber.successNel
        case _ => s"Invalid phone number : $phoneNumber".failureNel
      }

  }

  /**
   * Implicits based DI
   */
  object Agenda {

    implicit def agenda[F[_]](implicit R: Repository[F]): Agenda[F] = new Agenda[F]

  }




}
