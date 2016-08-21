package troy.cql.ast.ddl

import troy.cql.ast.DataType

object Table {
  final case class Column(name: String, dataType: DataType, isStatic: Boolean, isPrimaryKey: Boolean)
  final case class PrimaryKey(partitionKeys: Seq[String], clusteringColumns: Seq[String])

  sealed trait CreateTableOption
  final case class Property() extends CreateTableOption
  case object CompactStorage extends CreateTableOption
  case object ClusteringOrder extends CreateTableOption
}
