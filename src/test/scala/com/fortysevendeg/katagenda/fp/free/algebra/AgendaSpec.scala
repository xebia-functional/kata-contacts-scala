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

import com.fortysevendeg.katagenda.fp.free.algebra.datasources.{DataSource, GetAll, Add, DataOp}
import com.fortysevendeg.katagenda.fp.free.algebra.model._
import com.fortysevendeg.katagenda.fp.free.algebra.repositories.Repository
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Shapeless._
import org.specs2.ScalaCheck
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import scalaz._

import com.fortysevendeg.katagenda.fp.free.algebra.datasources.DataSource._
import com.fortysevendeg.katagenda.fp.free.algebra.repositories.Repository._
import Agenda._

trait AgendaSpecification
  extends Specification
  with ScalaCheck
  with Mockito {

  def validNameGen = Gen.alphaStr.filter(s => s.nonEmpty)

  def invalidNameGen = Gen.someOf("%", "$", "?", "!", "#") map (_.mkString(""))

  implicit val invalidFirstNames : Arbitrary[String] = Arbitrary(invalidNameGen)

  import Inject._

//  val agenda = implicitly[Agenda[Id.Id]]

  val agenda = implicitly[Agenda[Id.Id]]
  implicit val repository = new Repository[Id.Id]
  implicit val dataSource = new DataSource[Id.Id]
//  val agenda = new Agenda[Id.Id]

}

class AgendaSpec
  extends AgendaSpecification {

    "getContacts" should {

//      "delegate the call to repository" in
//        new AgendaScope {
//          agenda.getContacts shouldEqual Seq.empty
//        }

      "return the new contact after the creation using get contacts" in  {
        forall { c : Contact =>

          Agenda.agenda[Id.Id].addContact(c) foldMap (new (DataOp ~> Id.Id) {

            override def apply[A](fa: DataOp[A]): Id.Id[A] = fa match {
              case Add(ct) => Id.id.point(Some(ct))
              case GetAll() => Id.id.point(Nil)
            }
          }) shouldEqual Id.id.point(Some(c))
        }
      }

    }

  "validateFirstName" should {

    "return a success validation for a valid name" in {

      forall { first : FirstName =>
        agenda.validateFirstName(first) must beLike {
          case v: Success[FirstName] => v.a shouldEqual first
        }
      }


//      prop { (s: String) =>
//        agenda.validateFirstName(s) must beLike {
//          case v: Success[FirstName] => v.a shouldEqual s
//        }
//      }.setGen(validNameGen)
    }

//    "return a failed validation for a invalid name" in {
//
//      prop { (s: String) =>
//        agenda.validateFirstName(s) must beAnInstanceOf[Failure[NonEmptyList[InputError]]]
//      }.setGen(invalidNameGen)
//
//    }

  }

//  "validateLastName" should {
//
//    "return a success validation for a valid name" in {
//
//      prop { (s: String) =>
//        agenda.validateLastName(s) must beLike {
//          case v: Success[FirstName] => v.a shouldEqual s
//        }
//      }.setGen(validNameGen)
//    }
//
//    "return a failed validation for a invalid name" in {
//
//      prop { (s: String) =>
//        agenda.validateLastName(s) must beAnInstanceOf[Failure[NonEmptyList[InputError]]]
//      }.setGen(invalidNameGen)
//
//    }
//
//  }

}