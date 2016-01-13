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

package com.fortysevendeg.katagenda.fp.free.algebra

import com.fortysevendeg.katagenda.fp.free.algebra.datasources.{Add, DataOp, DataSource, GetAll}
import com.fortysevendeg.katagenda.fp.free.algebra.model.Contact
import org.specs2.mutable.Specification

import scalaz.{Free, Id, ~>}

import scalaz._

class DatasourceSpec
  extends Specification {

  val contact = Contact("First name", "Last name", "000000000")

  def withEmpty = new (DataOp ~> Id.Id) {
    override def apply[A](fa: DataOp[A]): Id.Id[A] = fa match {
      case Add(a: A) => None
      case GetAll() => List.empty[A]
    }
  }

  def withItems[A](a: A) = new (DataOp ~> Id.Id) {
    override def apply[A](fa: DataOp[A]): Id.Id[A] = fa match {
      case Add(a: A) => Some(a)
      case GetAll() => List(a)
    }
  }

  def interpretAdd(contact: Contact, interp: DataOp ~> Id.Id): Option[Contact] =
    Free.runFC(DataSource.dataSource[DataOp].add(contact))(interp)

  def interpretGetAll(interp: DataOp ~> Id.Id): List[Contact] =
    Free.runFC(DataSource.dataSource[DataOp].getAll[Contact])(interp)

  "add" should {

    "return a None for the empty interpreter" in {

      interpretAdd(contact, withEmpty) must beNone
    }

    "return the Contact for the withItems interpreter" in {

      interpretAdd(contact, withItems(contact)) must beSome(contact)
    }

  }

  "getAll" should {

    "return an empty list for the empty interpreter" in {

      val result = interpretGetAll(withEmpty)

      result must beEmpty
    }

    "return a list with one element for the withItems interpreter" in {

      val result = interpretGetAll(withItems(contact))

      result shouldEqual List(contact)
    }

  }
}