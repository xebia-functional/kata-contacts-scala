package com.karumi.katagenda.domain

import com.karumi.katagenda.common.repository.InMemoryDataSource
import com.karumi.katagenda.domain.repository.ContactsRepository
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

trait AgendaSpecification
  extends Specification {

  val anyFirstName = "Pedro Vicente"
  val anyLastName = "Gomez Sanchez"
  val anyPhoneNumber = "666666666"

  trait AgendaScope
    extends Scope {

    val contact = Contact(anyFirstName, anyLastName, anyPhoneNumber)

    val dataSource = new InMemoryDataSource[Contact]

    val contactRepository = new ContactsRepository(dataSource)

    val agenda = new Agenda(contactRepository)

  }

}

class AgendaSpec
  extends AgendaSpecification {

  "getContacts" should {

    "return an empty sequence of contacts if the agenda is empty" in
      new AgendaScope {
        agenda.getContacts must beEmpty
      }

    "return the new contact after the creation using get contacts" in
      new AgendaScope {
        agenda.addContact(contact)
        agenda.getContacts shouldEqual Seq(contact)
      }

  }

  "addContact" should {

    "return the contact created on contact added" in
      new AgendaScope {
        agenda.addContact(contact) shouldEqual contact
      }

  }

}
