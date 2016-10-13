package troy.driver.codecs

import com.datastax.driver.core.{ SettableByIndexData, GettableByIndexData }
import troy.driver.{ CassandraDataType => CT }

class PrimitivesConverter[J, S](val toScala: J => S, val toJava: S => J)

object PrimitivesConverter {
  implicit object IntegerToIntConverter extends PrimitivesConverter[java.lang.Integer, Int](j => j, s => s)
  implicit object LongToLongConverter extends PrimitivesConverter[java.lang.Long, Long](j => j, s => s)
  implicit object DoubleToDoubleConverter extends PrimitivesConverter[java.lang.Double, Double](j => j, s => s)
  implicit object BooleanToBooleanConverter extends PrimitivesConverter[java.lang.Boolean, Boolean](j => j, s => s)
  implicit object ShortToShortConverter extends PrimitivesConverter[java.lang.Short, Short](j => j, s => s)
  implicit object ByteToByteConverter extends PrimitivesConverter[java.lang.Byte, Byte](j => j, s => s)
  implicit object FloatToFloatConverter extends PrimitivesConverter[java.lang.Float, Float](j => j, s => s)

  implicit def bothTupleMembersConverter[J1, J2, S1 <: AnyVal, S2 <: AnyVal](implicit c1: PrimitivesConverter[J1, S1], c2: PrimitivesConverter[J2, S2]) =
    new PrimitivesConverter[(J1, J2), (S1, S2)](
      { case (j1, j2) => (c1.toScala(j1), c2.toScala(j2)) },
      { case (s1, s2) => (c1.toJava(s1), c2.toJava(s2)) }
    )

  implicit def firstTupleMemberConverter[J1, S1 <: AnyVal, S2 <: AnyRef](implicit c1: PrimitivesConverter[J1, S1]) =
    new PrimitivesConverter[(J1, S2), (S1, S2)](
      { case (j1, s2) => (c1.toScala(j1), s2) },
      { case (s1, s2) => (c1.toJava(s1), s2) }
    )

  implicit def secondTupleMemberConverter[J2, S1 <: AnyRef, S2 <: AnyVal](implicit c2: PrimitivesConverter[J2, S2]) =
    new PrimitivesConverter[(S1, J2), (S1, S2)](
      { case (s1, j2) => (s1, c2.toScala(j2)) },
      { case (s1, s2) => (s1, c2.toJava(s2)) }
    )
}

object PrimitivesCodecs {

  implicit val intAsInt = new TroyCodec[Int, CT.Int] {
    override def get(gettable: GettableByIndexData, i: Int): Int = gettable.getInt(i)
    override def set[T <: SettableByIndexData[T]](settable: T, i: Int, v: Int): T = settable.setInt(i, v)
  }

  implicit val bigIntAsLong = new TroyCodec[Long, CT.BigInt] {
    override def get(gettable: GettableByIndexData, i: Int): Long = gettable.getLong(i)
    override def set[T <: SettableByIndexData[T]](settable: T, i: Int, v: Long): T = settable.setLong(i, v)
  }

  implicit val counterAsLong = new TroyCodec[Long, CT.Counter] {
    override def get(gettable: GettableByIndexData, i: Int): Long = gettable.getLong(i)
    override def set[T <: SettableByIndexData[T]](settable: T, i: Int, v: Long): T = settable.setLong(i, v)
  }

  implicit val timeAsLong = new TroyCodec[Long, CT.Time] {
    override def get(gettable: GettableByIndexData, i: Int): Long = gettable.getLong(i)
    override def set[T <: SettableByIndexData[T]](settable: T, i: Int, v: Long): T = settable.setLong(i, v)
  }

  implicit val doubleAsDouble = new TroyCodec[Double, CT.Double] {
    override def get(gettable: GettableByIndexData, i: Int): Double = gettable.getDouble(i)
    override def set[T <: SettableByIndexData[T]](settable: T, i: Int, v: Double): T = settable.setDouble(i, v)
  }

  implicit val booleanAsBoolean = new TroyCodec[Boolean, CT.Boolean] {
    override def get(gettable: GettableByIndexData, i: Int): Boolean = gettable.getBool(i)
    override def set[T <: SettableByIndexData[T]](settable: T, i: Int, v: Boolean): T = settable.setBool(i, v)
  }

  implicit val smallIntAsShort = new TroyCodec[Short, CT.SmallInt] {
    override def get(gettable: GettableByIndexData, i: Int): Short = gettable.getShort(i)
    override def set[T <: SettableByIndexData[T]](settable: T, i: Int, v: Short): T = settable.setShort(i, v)
  }

  implicit val tinyIntAsByte = new TroyCodec[Byte, CT.TinyInt] {
    override def get(gettable: GettableByIndexData, i: Int): Byte = gettable.getByte(i)
    override def set[T <: SettableByIndexData[T]](settable: T, i: Int, v: Byte): T = settable.setByte(i, v)
  }

  implicit val floatAsFloat = new TroyCodec[Float, CT.Float] {
    override def get(gettable: GettableByIndexData, i: Int): Float = gettable.getFloat(i)
    override def set[T <: SettableByIndexData[T]](settable: T, i: Int, v: Float): T = settable.setFloat(i, v)
  }

  implicit def troyOptionalPrimitiveTypeCodec[S <: AnyVal, C <: CT](implicit inner: TroyCodec[S, C]) =
    new TroyCodec[Option[S], C] {
      override def set[T <: SettableByIndexData[T]](settable: T, i: Int, value: Option[S]) =
        value.map(inner.set(settable, i, _)).getOrElse(settable.setToNull(i))

      override def get(gettable: GettableByIndexData, i: Int) =
        if (gettable.isNull(i))
          None
        else
          Some(inner.get(gettable, i))
    }
}
