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

package com.fortysevendeg.katagenda.fp.ui

import com.fortysevendeg.katagenda.fp.Application._
import com.fortysevendeg.katagenda.fp.free.algebra.model
import model.Agenda
import com.fortysevendeg.katagenda.fp.free.algebra.interaction.Interacts
import com.fortysevendeg.katagenda.fp.free.algebra.model
import com.fortysevendeg.katagenda.fp.free.algebra.usecases.UseCases

import scalaz.syntax.applicative._

/**
 * In clean they use presenters to represent the interaction. Like a glorified controller that delegates
 * to a view and interacts with the user input
 */
class ContactsListPresenter[F[_]](implicit
    I: Interacts[AgendaApp],
    U : UseCases[AgendaApp],
    A : Agenda[AgendaApp]) extends View {

  import A._
  import model._

  def onInitialize =
    for {
      _ <- showWelcomeMessage
      _ <- loadContactsList
    } yield ()

  def onStop =
    showGoodbyeMessage

  def onAddContactOptionSelected =
    for {
      newContactValidation <- requestNewContact
      _ <- newContactValidation fold (l => showErrors(l), c => U.addContact(c))
      _ <- loadContactsList
    } yield ()


  /**
   * When requesting a new contact we read its first, last and phone number and run their validations results
   * accumulating errors on a NonEmptyList and use |@| to build applicative expressions since Validation it's in itself
   * an applicative. This allows us to accumulate all errors and return either all errors or the hidrated Contact
   */
  private[this] def requestNewContact = {
    for {
      firstName <- getNewContactFirstName map validateFirstName
      lastName <- getNewContactLastName map validateLastName
      phoneNumber <- getNewContactPhoneNumber map validatePhoneNumber
      validated = (firstName |@| lastName |@| phoneNumber)(Contact.apply)
    } yield validated
  }

  private[this] def loadContactsList = {
    for {
      contacts <- U.getContacts
      _ <- contacts match {
        case Nil => showEmptyCase
        case allContacts => showContacts(allContacts)
      }
    } yield ()
  }

}

/**
 * Implicits based DI
 */
object ContactsListPresenter {
  implicit def contactsListPresenter[F[_]](implicit
      I: Interacts[AgendaApp],
      U : UseCases[AgendaApp],
      A : Agenda[AgendaApp]): ContactsListPresenter[F] = new ContactsListPresenter[F]
}
