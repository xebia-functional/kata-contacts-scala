package com.karumi.katagenda.common.repository

trait DataSource[T] {

  def getAll: Seq[T]

  def add(item: T): T

}

class InMemoryDataSource[T] extends DataSource[T] {

  private[this] val list = scala.collection.mutable.ArrayBuffer.empty[T]

  override def getAll: Seq[T] = list

  override def add(item: T): T = {
    list += item
    item
  }
}
