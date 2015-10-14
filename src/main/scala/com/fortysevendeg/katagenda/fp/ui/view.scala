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

import com.fortysevendeg.katagenda.fp.Application.AgendaApp
import com.fortysevendeg.katagenda.fp.free.algebra.model
import model.{Contact, InputError}
import com.fortysevendeg.katagenda.fp.free.algebra.interaction.Interacts

import scalaz.NonEmptyList
import scalaz.syntax.foldable._

/**
 * Notice that in this approach based on free monads we don't need a view implementation tailored to the command
 * line because the behavior is provided by the interpreter and not the implementation
 */
class View(implicit I: Interacts[AgendaApp]) {

  import I._

  def showWelcomeMessage =
    tell(
      """Welcome to your awesome agenda!
        |I'm going to ask you about some of your contacts information :)""".stripMargin)

  def showErrors(errors: NonEmptyList[InputError]) =
    tell(errors.toList mkString "\n")

  def showGoodbyeMessage =
    tell("See you soon!")

  def showEmptyCase =
    tell("Your agenda is empty!")

  def showContacts(contactList: List[Contact]) =
    tell(contactList.toList map { contact =>
      s"${contact.firstName} - ${contact.lastName} - ${contact.phoneNumber}"
    } mkString "\n")

  def getNewContactFirstName =
    ask("First Name:")

  def getNewContactLastName =
    ask("LastName:")

  def getNewContactPhoneNumber =
    ask("Phone Number:")

}