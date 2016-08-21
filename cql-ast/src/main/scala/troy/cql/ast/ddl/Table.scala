package troy.cql.ast.ddl

import troy.cql.ast.DataType

object Table {
  case class Column(name: String, dataType: DataType, isStatic: Boolean, isPrimaryKey: Boolean)
  case class PrimaryKey(partitionKeys: Seq[String], clusteringColumns: Seq[String])

  trait CreateTableOption
  case class Property() extends CreateTableOption
  case object CompactStorage extends CreateTableOption
  case object ClusteringOrder extends CreateTableOption
}
