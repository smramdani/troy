package troy.cql.ast

import troy.cql.ast.dml._

sealed trait DataDefinition
sealed trait DataManipulation
sealed trait Cql3Statement

case class DeleteStatement(
  simpleSelection: Option[Seq[SimpleSelection]],
  from: TableName,
  using: Option[Seq[UpdateParam]],
  where: WhereClause,
  ifCondition: Option[Delete.IfCondition]
) extends DataManipulation

case class InsertStatement(
  into: TableName,
  insertClause: Insert.InsertClause,
  ifNotExists: Boolean,
  using: Option[Seq[UpdateParam]]
) extends DataManipulation

case class SelectStatement(
  mod: Option[Select.Mod],
  selection: Select.Selection,
  from: TableName,
  where: Option[WhereClause],
  orderBy: Option[Select.OrderBy],
  perPartitionLimit: Option[Select.LimitParam],
  limit: Option[Select.LimitParam],
  allowFiltering: Boolean
) extends DataManipulation

case class CreateKeyspace(
  ifNotExists: Boolean,
  keyspaceName: KeyspaceName,
  properties: Seq[Keyspace.KeyspaceOption]
) extends DataDefinition

case class UseStatement(keyspaceName: KeyspaceName) extends DataDefinition

case class CreateTable(
  ifNotExists: Boolean,
  tableName: TableName,
  columns: Seq[Table.Column],
  primaryKey: Option[Table.PrimaryKey],
  options: Seq[Table.CreateTableOption]
) extends DataDefinition

case class CreateIndex(
  isCustom: Boolean,
  ifNotExists: Boolean,
  indexName: Option[String],
  tableName: TableName,
  identifier: Index.IndexIdentifier,
  using: Option[Index.Using]
) extends DataDefinition