package com.karumi.katagenda.ui

import com.karumi.katagenda.domain.Contact
import com.karumi.katagenda.usecase.{AddContact, GetContacts}
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

}

class ContactsListPresenterSpec
  extends ContactsListPresenterSpecification {

  "onInitialize" should {

    "show welcome message on initialize" in new ContactsListPresenterScope {

      getContacts.execute returns Seq.empty

      presenter.onInitialize()

      there was one(view).showWelcomeMessage()
    }

    "show empty case if the agenda is empty" in new ContactsListPresenterScope {

      getContacts.execute returns Seq.empty

      presenter.onInitialize()

      there was one(view).showEmptyCase()
    }

    "show contacts from the agenda on initialize" in new ContactsListPresenterScope {

      getContacts.execute returns contacts

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
      new ContactsListPresenterScope {

        getContacts.execute returns Seq.empty

        view.getNewContactFirstName returns contact.firstName
        view.getNewContactLastName returns contact.lastName
        view.getNewContactPhoneNumber returns contact.phoneNumber

        addContact.execute(contact) returns contact

        getContacts.execute returns Seq(contact)

        presenter.onInitialize()
        presenter.onAddContactOptionSelected()

        val captor = capture[Seq[Contact]]
        there was two(view).showContacts(captor)
        captor.value shouldEqual Seq(contact)
      }

    "show an error if the first name of the new contact is empty" in
      new ContactsListPresenterScope {

        getContacts.execute returns Seq.empty

        view.getNewContactFirstName returns ""
        view.getNewContactLastName returns contact.lastName
        view.getNewContactPhoneNumber returns contact.phoneNumber

        presenter.onInitialize()
        presenter.onAddContactOptionSelected()

        there was one(view).showDefaultError()
      }

    "show an error if the last name of the new contact is empty" in
      new ContactsListPresenterScope {

        getContacts.execute returns Seq.empty

        view.getNewContactFirstName returns contact.firstName
        view.getNewContactLastName returns ""
        view.getNewContactPhoneNumber returns contact.phoneNumber

        presenter.onInitialize()
        presenter.onAddContactOptionSelected()

        there was one(view).showDefaultError()
      }

    "show an error if the phone number of the new contact is empty" in
      new ContactsListPresenterScope {

        getContacts.execute returns Seq.empty

        view.getNewContactFirstName returns contact.firstName
        view.getNewContactLastName returns contact.lastName
        view.getNewContactPhoneNumber returns ""

        presenter.onInitialize()
        presenter.onAddContactOptionSelected()

        there was one(view).showDefaultError()
      }

  }

}
