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
package com.fortysevendeg.katagenda.servicelocator

import com.fortysevendeg.katagenda.common.repository.{InMemoryDataSource, Repository, DataSource}
import com.fortysevendeg.katagenda.domain.{Agenda, Contact}
import com.fortysevendeg.katagenda.domain.repository.ContactsRepository
import com.fortysevendeg.katagenda.ui.{ContactsListPresenter, SysOutContactsListView}
import com.fortysevendeg.katagenda.usecase.{GetContacts, AddContact}

object AgendaServiceLocator {
  
  val inMemoryDataSource: DataSource[Contact] = new InMemoryDataSource[Contact]

  def getContactsListPresenter: ContactsListPresenter =
    new ContactsListPresenter(
      view = getSysOutView,
      getContacts = getContacts,
      addContact = addContact())

  private[this] def getSysOutView: SysOutContactsListView =
    new SysOutContactsListView

  private[this] def getContacts: GetContacts =
    new GetContacts(getAgenda)

  private[this] def addContact(): AddContact =
    new AddContact(getAgenda)

  private[this] def getAgenda: Agenda =
    new Agenda(getContactsRepository)

  private[this] def getContactsRepository: Repository[Contact] =
    new ContactsRepository(inMemoryDataSource)
}

