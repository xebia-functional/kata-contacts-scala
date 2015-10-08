/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.fortysevendeg.katagenda.ui

import com.fortysevendeg.katagenda.domain.Contact
import com.fortysevendeg.katagenda.usecase.{GetContacts, AddContact}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait ContactsListPresenterSpecification
  extends Specification
  with Mockito {

  val anyNumberOfContacts = 7
  val anyFirstName = "Pedro Vicente"
  val anyLastName = "Gomez Sanchez"
  val anyPhoneNumber = "666666666"

  val contact = Contact(anyFirstName, anyLastName, anyPhoneNumber)

  val contacts: Seq[Contact] = 1 to anyNumberOfContacts map { _ =>
    Contact(anyFirstName, anyLastName, anyPhoneNumber)
  }

  trait ContactsListPresenterScope
    extends Scope {

    val view = mock[View]

    val getContacts = mock[GetContacts]

    val addContact = mock[AddContact]

    val presenter = new ContactsListPresenter(view, getContacts, addContact)

  }

  trait EmptyContactList {

    self: ContactsListPresenterScope =>

    getContacts.execute returns Seq.empty

  }

  trait PopulatedContactList {

    self: ContactsListPresenterScope =>

    getContacts.execute returns contacts

  }

  trait ViewMocked {

    self: ContactsListPresenterScope =>

    view.getNewContactFirstName returns Some(contact.firstName)
    view.getNewContactLastName returns Some(contact.lastName)
    view.getNewContactPhoneNumber returns Some(contact.phoneNumber)

  }

}

class ContactsListPresenterSpec
  extends ContactsListPresenterSpecification {

  "onInitialize" should {

    "show welcome message on initialize" in
      new ContactsListPresenterScope with EmptyContactList {

      presenter.onInitialize()

      there was one(view).showWelcomeMessage()
    }

    "show empty case if the agenda is empty" in
      new ContactsListPresenterScope with EmptyContactList {

      presenter.onInitialize()

      there was one(view).showEmptyCase()
    }

    "show contacts from the agenda on initialize" in
      new ContactsListPresenterScope with PopulatedContactList {

      presenter.onInitialize()

      there was one(view).showContacts(contacts)
    }

  }

  "onStop" should {

    "show goodbye message on stop" in new ContactsListPresenterScope {

      presenter.onStop()

      there was one(view).showGoodbyeMessage()
    }
  }

  "onAddContactOptionSelected" should {

    "show the contacts list with the new contact on contact added" in
      new ContactsListPresenterScope with ViewMocked {

        getContacts.execute returns (Seq.empty, Seq(contact))

        addContact.execute(contact) returns contact

        presenter.onInitialize()
        presenter.onAddContactOptionSelected()

        val captor = capture[Seq[Contact]]
        there was one(view).showContacts(captor)
        captor.value shouldEqual Seq(contact)
      }

    "show an error if the first name of the new contact is empty" in
      new ContactsListPresenterScope with ViewMocked with EmptyContactList {

        view.getNewContactFirstName returns None

        presenter.onInitialize()
        presenter.onAddContactOptionSelected()

        there was one(view).showDefaultError()
      }

    "show an error if the last name of the new contact is empty" in
      new ContactsListPresenterScope with ViewMocked with EmptyContactList {

        view.getNewContactLastName returns None

        presenter.onInitialize()
        presenter.onAddContactOptionSelected()

        there was one(view).showDefaultError()
      }

    "show an error if the phone number of the new contact is empty" in
      new ContactsListPresenterScope with ViewMocked with EmptyContactList {

        view.getNewContactPhoneNumber returns None

        presenter.onInitialize()
        presenter.onAddContactOptionSelected()

        there was one(view).showDefaultError()
      }

  }

}
