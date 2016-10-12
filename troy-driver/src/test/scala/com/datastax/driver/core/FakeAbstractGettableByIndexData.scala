package com.datastax.driver.core

import java.net.InetAddress
import java.nio.ByteBuffer
import java.util.Date

class FakeAbstractGettableByIndexData(pv: ProtocolVersion, data: (ByteBuffer, DataType)*) extends AbstractGettableByIndexData(pv) {
  override def getType(i: Int): DataType = data(i)._2

  override def getValue(i: Int): ByteBuffer = data(i)._1

  override def getName(i: Int): String = ""

  override def getCodecRegistry: CodecRegistry = ???
}
object FakeAbstractGettableByIndexData {
  def ascii(value: String, pv: ProtocolVersion = ProtocolVersion.V4) = new FakeAbstractGettableByIndexData(
    pv, TypeCodec.ascii.serialize(value, pv) -> DataType.ascii()
  )

  def timestamp(value: Date, pv: ProtocolVersion = ProtocolVersion.V4) = new FakeAbstractGettableByIndexData(
    pv, TypeCodec.timestamp.serialize(value, pv) -> DataType.timestamp()
  )

  def decimal(value: java.math.BigDecimal, pv: ProtocolVersion = ProtocolVersion.V4) = new FakeAbstractGettableByIndexData(
    pv, TypeCodec.decimal.serialize(value, pv) -> DataType.decimal()
  )

  def inet(value: InetAddress, pv: ProtocolVersion = ProtocolVersion.V4) = new FakeAbstractGettableByIndexData(
    pv, TypeCodec.inet.serialize(value, pv) -> DataType.inet()
  )

  def date(value: LocalDate, pv: ProtocolVersion = ProtocolVersion.V4) = new FakeAbstractGettableByIndexData(
    pv, TypeCodec.date.serialize(value, pv) -> DataType.date()
  )
}