package troy.driver

import java.math.BigInteger
import java.math.BigDecimal
import java.net.InetAddress
import java.nio.ByteBuffer
import java.util.{ UUID, Date }

import com.datastax.driver.core.utils.UUIDs
import com.datastax.driver.core.{ BoundStatement, LocalDate, GettableByIndexData }

import InternalDsl._
import troy.driver.codecs.PrimitivesCodecs._

object CodecsSpec {
  implicit def gettable: GettableByIndexData = ???
  def settable: BoundStatement = ???

  column[Int](0).as[CDT.Int]
  param(55).as[CDT.Int].set(settable, 0)

  column[Long](0).as[CDT.BigInt]
  param(55555L).as[CDT.BigInt].set(settable, 0)

  column[Long](0).as[CDT.Counter]
  param(55555L).as[CDT.Counter].set(settable, 0)

  column[Long](0).as[CDT.Time]
  param(55555L).as[CDT.Time].set(settable, 0)

  column[Short](0).as[CDT.SmallInt]
  param(5.toShort).as[CDT.SmallInt].set(settable, 0)

  column[Byte](0).as[CDT.TinyInt]
  param(5.toByte).as[CDT.TinyInt].set(settable, 0)

  column[Double](0).as[CDT.Double]
  param(5.5D).as[CDT.Double].set(settable, 0)

  column[Float](0).as[CDT.Float]
  param(5.5F).as[CDT.Float].set(settable, 0)

  column[Boolean](0).as[CDT.Boolean]
  param(true).as[CDT.Boolean].set(settable, 0)

  column[String](0).as[CDT.Ascii]
  param("").as[CDT.Ascii].set(settable, 0)

  column[String](0).as[CDT.Text]
  param("").as[CDT.Text].set(settable, 0)

  column[String](0).as[CDT.VarChar]
  param("").as[CDT.VarChar].set(settable, 0)

  column[Date](0).as[CDT.Timestamp]
  param(new Date).as[CDT.Timestamp].set(settable, 0)

  column[BigDecimal](0).as[CDT.Decimal]
  param(BigDecimal.ZERO).as[CDT.Decimal].set(settable, 0)

  column[InetAddress](0).as[CDT.Inet]
  param(InetAddress.getLocalHost).as[CDT.Inet].set(settable, 0)

  column[BigInteger](0).as[CDT.VarInt]
  param(BigInteger.ZERO).as[CDT.VarInt].set(settable, 0)

  column[ByteBuffer](0).as[CDT.Blob]
  param(ByteBuffer.allocate(1)).as[CDT.Blob].set(settable, 0)

  column[UUID](0).as[CDT.TimeUuid]
  param(UUIDs.timeBased).as[CDT.TimeUuid].set(settable, 0)

  column[UUID](0).as[CDT.Uuid]
  param(UUID.randomUUID()).as[CDT.Uuid].set(settable, 0)

  column[LocalDate](0).as[CDT.Date]
  param(LocalDate.fromMillisSinceEpoch(0)).as[CDT.Date].set(settable, 0)

  column[Seq[Int]](0).as[CDT.List[CDT.Int]]
  param(Seq(55)).as[CDT.List[CDT.Int]].set(settable, 0)

  column[Seq[Long]](0).as[CDT.List[CDT.BigInt]]
  param(Seq(55555L)).as[CDT.List[CDT.BigInt]].set(settable, 0)

  column[Seq[Long]](0).as[CDT.List[CDT.Counter]]
  param(Seq(55555L)).as[CDT.List[CDT.Counter]].set(settable, 0)

  column[Seq[Long]](0).as[CDT.List[CDT.Time]]
  param(Seq(55555L)).as[CDT.List[CDT.Time]].set(settable, 0)

  column[Seq[Short]](0).as[CDT.List[CDT.SmallInt]]
  param(Seq(5.toShort)).as[CDT.List[CDT.SmallInt]].set(settable, 0)

  column[Seq[Byte]](0).as[CDT.List[CDT.TinyInt]]
  param(Seq(5.toByte)).as[CDT.List[CDT.TinyInt]].set(settable, 0)

  column[Seq[Double]](0).as[CDT.List[CDT.Double]]
  param(Seq(5.5D)).as[CDT.List[CDT.Double]].set(settable, 0)

  column[Seq[Float]](0).as[CDT.List[CDT.Float]]
  param(Seq(5.5F)).as[CDT.List[CDT.Float]].set(settable, 0)

  column[Seq[Boolean]](0).as[CDT.List[CDT.Boolean]]
  param(Seq(true)).as[CDT.List[CDT.Boolean]].set(settable, 0)

  column[Seq[String]](0).as[CDT.List[CDT.Ascii]]
  param(Seq("")).as[CDT.List[CDT.Ascii]].set(settable, 0)

  column[Seq[String]](0).as[CDT.List[CDT.Text]]
  param(Seq("")).as[CDT.List[CDT.Text]].set(settable, 0)

  column[Seq[String]](0).as[CDT.List[CDT.VarChar]]
  param(Seq("")).as[CDT.List[CDT.VarChar]].set(settable, 0)

  column[Seq[Date]](0).as[CDT.List[CDT.Timestamp]]
  param(Seq(new Date)).as[CDT.List[CDT.Timestamp]].set(settable, 0)

  column[Seq[BigDecimal]](0).as[CDT.List[CDT.Decimal]]
  param(Seq(BigDecimal.ZERO)).as[CDT.List[CDT.Decimal]].set(settable, 0)

  column[Seq[InetAddress]](0).as[CDT.List[CDT.Inet]]
  param(Seq(InetAddress.getLocalHost)).as[CDT.List[CDT.Inet]].set(settable, 0)

  column[Seq[BigInteger]](0).as[CDT.List[CDT.VarInt]]
  param(Seq(BigInteger.ZERO)).as[CDT.List[CDT.VarInt]].set(settable, 0)

  column[Seq[ByteBuffer]](0).as[CDT.List[CDT.Blob]]
  param(Seq(ByteBuffer.allocate(1))).as[CDT.List[CDT.Blob]].set(settable, 0)

  column[Seq[UUID]](0).as[CDT.List[CDT.TimeUuid]]
  param(Seq(UUIDs.timeBased)).as[CDT.List[CDT.TimeUuid]].set(settable, 0)

  column[Seq[UUID]](0).as[CDT.List[CDT.Uuid]]
  param(Seq(UUID.randomUUID())).as[CDT.List[CDT.Uuid]].set(settable, 0)

  column[Seq[LocalDate]](0).as[CDT.List[CDT.Date]]
  param(Seq(LocalDate.fromMillisSinceEpoch(0))).as[CDT.List[CDT.Date]].set(settable, 0)

  column[Set[Int]](0).as[CDT.Set[CDT.Int]]
  param(Set(55)).as[CDT.Set[CDT.Int]].set(settable, 0)

  column[Set[Long]](0).as[CDT.Set[CDT.BigInt]]
  param(Set(55555L)).as[CDT.Set[CDT.BigInt]].set(settable, 0)

  column[Set[Long]](0).as[CDT.Set[CDT.Counter]]
  param(Set(55555L)).as[CDT.Set[CDT.Counter]].set(settable, 0)

  column[Set[Long]](0).as[CDT.Set[CDT.Time]]
  param(Set(55555L)).as[CDT.Set[CDT.Time]].set(settable, 0)

  column[Set[Short]](0).as[CDT.Set[CDT.SmallInt]]
  param(Set(5.toShort)).as[CDT.Set[CDT.SmallInt]].set(settable, 0)

  column[Set[Byte]](0).as[CDT.Set[CDT.TinyInt]]
  param(Set(5.toByte)).as[CDT.Set[CDT.TinyInt]].set(settable, 0)

  column[Set[Double]](0).as[CDT.Set[CDT.Double]]
  param(Set(5.5D)).as[CDT.Set[CDT.Double]].set(settable, 0)

  column[Set[Float]](0).as[CDT.Set[CDT.Float]]
  param(Set(5.5F)).as[CDT.Set[CDT.Float]].set(settable, 0)

  column[Set[Boolean]](0).as[CDT.Set[CDT.Boolean]]
  param(Set(true)).as[CDT.Set[CDT.Boolean]].set(settable, 0)

  column[Set[String]](0).as[CDT.Set[CDT.Ascii]]
  param(Set("")).as[CDT.Set[CDT.Ascii]].set(settable, 0)

  column[Set[String]](0).as[CDT.Set[CDT.Text]]
  param(Set("")).as[CDT.Set[CDT.Text]].set(settable, 0)

  column[Set[String]](0).as[CDT.Set[CDT.VarChar]]
  param(Set("")).as[CDT.Set[CDT.VarChar]].set(settable, 0)

  column[Set[Date]](0).as[CDT.Set[CDT.Timestamp]]
  param(Set(new Date)).as[CDT.Set[CDT.Timestamp]].set(settable, 0)

  column[Set[BigDecimal]](0).as[CDT.Set[CDT.Decimal]]
  param(Set(BigDecimal.ZERO)).as[CDT.Set[CDT.Decimal]].set(settable, 0)

  column[Set[InetAddress]](0).as[CDT.Set[CDT.Inet]]
  param(Set(InetAddress.getLocalHost)).as[CDT.Set[CDT.Inet]].set(settable, 0)

  column[Set[BigInteger]](0).as[CDT.Set[CDT.VarInt]]
  param(Set(BigInteger.ZERO)).as[CDT.Set[CDT.VarInt]].set(settable, 0)

  column[Set[ByteBuffer]](0).as[CDT.Set[CDT.Blob]]
  param(Set(ByteBuffer.allocate(1))).as[CDT.Set[CDT.Blob]].set(settable, 0)

  column[Set[UUID]](0).as[CDT.Set[CDT.TimeUuid]]
  param(Set(UUIDs.timeBased)).as[CDT.Set[CDT.TimeUuid]].set(settable, 0)

  column[Set[UUID]](0).as[CDT.Set[CDT.Uuid]]
  param(Set(UUID.randomUUID())).as[CDT.Set[CDT.Uuid]].set(settable, 0)

  column[Set[LocalDate]](0).as[CDT.Set[CDT.Date]]
  param(Set(LocalDate.fromMillisSinceEpoch(0))).as[CDT.Set[CDT.Date]].set(settable, 0)

  column[Map[Int, Int]](0).as[CDT.Map[CDT.Int, CDT.Int]]
  param(Map(55 -> 55)).as[CDT.Map[CDT.Int, CDT.Int]].set(settable, 0)

  column[Map[Long, Long]](0).as[CDT.Map[CDT.BigInt, CDT.BigInt]]
  param(Map(55555L -> 55555L)).as[CDT.Map[CDT.BigInt, CDT.BigInt]].set(settable, 0)

  column[Map[Long, Long]](0).as[CDT.Map[CDT.Counter, CDT.Counter]]
  param(Map(55555L -> 55555L)).as[CDT.Map[CDT.Counter, CDT.Counter]].set(settable, 0)

  column[Map[Long, Long]](0).as[CDT.Map[CDT.Time, CDT.Time]]
  param(Map(55555L -> 55555L)).as[CDT.Map[CDT.Time, CDT.Time]].set(settable, 0)

  column[Map[Short, Short]](0).as[CDT.Map[CDT.SmallInt, CDT.SmallInt]]
  param(Map(5.toShort -> 5.toShort)).as[CDT.Map[CDT.SmallInt, CDT.SmallInt]].set(settable, 0)

  column[Map[Byte, Byte]](0).as[CDT.Map[CDT.TinyInt, CDT.TinyInt]]
  param(Map(5.toByte -> 5.toByte)).as[CDT.Map[CDT.TinyInt, CDT.TinyInt]].set(settable, 0)

  column[Map[Double, Double]](0).as[CDT.Map[CDT.Double, CDT.Double]]
  param(Map(5.5D -> 5.5D)).as[CDT.Map[CDT.Double, CDT.Double]].set(settable, 0)

  column[Map[Float, Float]](0).as[CDT.Map[CDT.Float, CDT.Float]]
  param(Map(5.5F -> 5.5F)).as[CDT.Map[CDT.Float, CDT.Float]].set(settable, 0)

  column[Map[Boolean, Boolean]](0).as[CDT.Map[CDT.Boolean, CDT.Boolean]]
  param(Map(true -> true)).as[CDT.Map[CDT.Boolean, CDT.Boolean]].set(settable, 0)

  column[Map[String, String]](0).as[CDT.Map[CDT.Ascii, CDT.Ascii]]
  param(Map("" -> "")).as[CDT.Map[CDT.Ascii, CDT.Ascii]].set(settable, 0)

  column[Map[String, String]](0).as[CDT.Map[CDT.Text, CDT.Text]]
  param(Map("" -> "")).as[CDT.Map[CDT.Text, CDT.Text]].set(settable, 0)

  column[Map[String, String]](0).as[CDT.Map[CDT.VarChar, CDT.VarChar]]
  param(Map("" -> "")).as[CDT.Map[CDT.VarChar, CDT.VarChar]].set(settable, 0)

  column[Map[Date, Date]](0).as[CDT.Map[CDT.Timestamp, CDT.Timestamp]]
  param(Map(new Date -> new Date)).as[CDT.Map[CDT.Timestamp, CDT.Timestamp]].set(settable, 0)

  column[Map[BigDecimal, BigDecimal]](0).as[CDT.Map[CDT.Decimal, CDT.Decimal]]
  param(Map(BigDecimal.ZERO -> BigDecimal.ZERO)).as[CDT.Map[CDT.Decimal, CDT.Decimal]].set(settable, 0)

  column[Map[InetAddress, InetAddress]](0).as[CDT.Map[CDT.Inet, CDT.Inet]]
  param(Map(InetAddress.getLocalHost -> InetAddress.getLocalHost)).as[CDT.Map[CDT.Inet, CDT.Inet]].set(settable, 0)

  column[Map[BigInteger, BigInteger]](0).as[CDT.Map[CDT.VarInt, CDT.VarInt]]
  param(Map(BigInteger.ZERO -> BigInteger.ZERO)).as[CDT.Map[CDT.VarInt, CDT.VarInt]].set(settable, 0)

  column[Map[ByteBuffer, ByteBuffer]](0).as[CDT.Map[CDT.Blob, CDT.Blob]]
  param(Map(ByteBuffer.allocate(1) -> ByteBuffer.allocate(1))).as[CDT.Map[CDT.Blob, CDT.Blob]].set(settable, 0)

  column[Map[UUID, UUID]](0).as[CDT.Map[CDT.TimeUuid, CDT.TimeUuid]]
  param(Map(UUIDs.timeBased -> UUIDs.timeBased)).as[CDT.Map[CDT.TimeUuid, CDT.TimeUuid]].set(settable, 0)

  column[Map[UUID, UUID]](0).as[CDT.Map[CDT.Uuid, CDT.Uuid]]
  param(Map(UUID.randomUUID() -> UUID.randomUUID())).as[CDT.Map[CDT.Uuid, CDT.Uuid]].set(settable, 0)

  column[Map[LocalDate, LocalDate]](0).as[CDT.Map[CDT.Date, CDT.Date]]
  param(Map(LocalDate.fromMillisSinceEpoch(0) -> LocalDate.fromMillisSinceEpoch(0))).as[CDT.Map[CDT.Date, CDT.Date]].set(settable, 0)

  column[Map[Int, String]](0).as[CDT.Map[CDT.Int, CDT.Text]]
  param(Map(55 -> "")).as[CDT.Map[CDT.Int, CDT.Text]].set(settable, 0)

  column[Map[String, Int]](0).as[CDT.Map[CDT.Text, CDT.Int]]
  param(Map("" -> 55)).as[CDT.Map[CDT.Text, CDT.Int]].set(settable, 0)

  // TODO
  //  column[TupleValue](0).as[CDT.Tuple]
  //  param(???).as[CDT.Tuple].set(settable, 0)

  //  column[UDTValue](0).as[CDT.User-defined types]
  //  param(???).as[CDT.User-defined types].set(settable, 0)

}
