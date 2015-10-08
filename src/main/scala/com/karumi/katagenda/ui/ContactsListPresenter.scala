/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.karumi.katagenda.ui

import com.karumi.katagenda.common.{ui => commonUi}
import com.karumi.katagenda.domain.Contact
import com.karumi.katagenda.usecase.{AddContact, GetContacts}

trait View extends commonUi.View {

  def showWelcomeMessage()

  def showGoodbyeMessage()

  def showContacts(contactList: Seq[Contact])

  def getNewContactFirstName: Option[String]

  def getNewContactLastName: Option[String]

  def getNewContactPhoneNumber: Option[String]
}

class ContactsListPresenter(
  view: View,
  getContacts: GetContacts,
  addContact: AddContact) extends commonUi.Presenter[View](view) {

  def onInitialize() = {
    view.showWelcomeMessage()
    loadContactsList()
  }

  def onStop() = {
    view.showGoodbyeMessage()
  }

  def onAddContactOptionSelected() = requestNewContact match {
    case Some(c) =>
      addContact.execute(c)
      loadContactsList()
    case _ =>
      view.showDefaultError()
  }


  private[this] def requestNewContact: Option[Contact] = {
    val maybeFirstName = view.getNewContactFirstName
    val maybeLastName = view.getNewContactLastName
    val maybePhoneNumber = view.getNewContactPhoneNumber
    (maybeFirstName, maybeLastName, maybePhoneNumber) match {
      case (Some(firstName), Some(lastName), Some(phoneNumber)) =>
        Some(new Contact(firstName, lastName, phoneNumber))
      case _ => None
    }
  }

  private[this] def loadContactsList() = {
    val contactList: Seq[Contact] = getContacts.execute
    if (contactList.isEmpty) {
      view.showEmptyCase()
    } else {
      view.showContacts(contactList)
    }
  }
}
