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

package com.fortysevendeg.katagenda.domain

import com.fortysevendeg.katagenda.common.repository.InMemoryDataSource
import com.fortysevendeg.katagenda.domain.repository.ContactsRepository
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
