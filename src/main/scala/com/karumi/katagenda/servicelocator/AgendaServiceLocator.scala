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
package com.karumi.katagenda.servicelocator

import com.karumi.katagenda.common.repository.{InMemoryDataSource, DataSource, Repository}
import com.karumi.katagenda.domain.Agenda
import com.karumi.katagenda.domain.Contact
import com.karumi.katagenda.domain.repository.ContactsRepository
import com.karumi.katagenda.ui.ContactsListPresenter
import com.karumi.katagenda.ui.SysOutContactsListView
import com.karumi.katagenda.usecase.AddContact
import com.karumi.katagenda.usecase.GetContacts

object AgendaServiceLocator {
  
  val inMemoryDataSource: DataSource[Contact] = new InMemoryDataSource[Contact]

  def getContactsListPresenter: ContactsListPresenter = {
    val sysOutView: SysOutContactsListView = getSysOutView
    new ContactsListPresenter(sysOutView, getContacts, addContact())
  }

  private def getSysOutView: SysOutContactsListView = {
    new SysOutContactsListView
  }

  private[this] def getContacts: GetContacts = {
    val agenda: Agenda = getAgenda
    new GetContacts(agenda)
  }

  private def addContact(): AddContact = {
    val agenda: Agenda = getAgenda
    new AddContact(agenda)
  }

  private def getAgenda: Agenda = {
    val contactsRepository: Repository[Contact] = getContactsRepository
    new Agenda(contactsRepository)
  }

  private def getContactsRepository: Repository[Contact] = {
    new ContactsRepository(inMemoryDataSource)
  }
}

