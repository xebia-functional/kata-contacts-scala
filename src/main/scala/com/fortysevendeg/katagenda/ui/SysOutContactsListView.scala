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
import java.util.Scanner

class SysOutContactsListView extends View {

  def showWelcomeMessage() {
    print("Welcome to your awesome agenda!")
    print("I'm going to ask you about some of your contacts information :)")
  }

  def showGoodbyeMessage() {
    print("\nSee you soon!")
  }

  def showContacts(contactList: Seq[Contact]) {
    for (contact <- contactList) {
      val firstName: String = contact.firstName
      val lastName: String = contact.lastName
      val phoneNumber: String = contact.phoneNumber
      println(firstName + " - " + lastName + " - " + phoneNumber)
    }
  }

  def getNewContactFirstName: Option[String] = {
    println("First name:")
    readLine
  }

  def getNewContactLastName: Option[String] = {
    println("Last name:")
    readLine
  }

  def getNewContactPhoneNumber: Option[String] = {
    println("Phone number:")
    readLine
  }

  def showDefaultError() {
    println("Ups, something went wrong :( Try again!")
  }

  def showEmptyCase() {
    println("Your agenda is empty!")
  }

  private def readLine: Option[String] =
    new Scanner(System.in).nextLine match {
      case s if s.isEmpty => None
      case s => Some(s)
    }

}
