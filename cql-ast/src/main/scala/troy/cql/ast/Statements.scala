package troy.cql.ast

import troy.cql.ast.dml._
import troy.cql.ast.ddl._

sealed trait Cql3Statement
sealed trait DataDefinition extends Cql3Statement
sealed trait DataManipulation extends Cql3Statement

final case class DeleteStatement(
  simpleSelection: Option[Seq[SimpleSelection]],
  from: TableName,
  using: Option[Seq[UpdateParam]],
  where: WhereClause,
  ifCondition: Option[IfCondition]
) extends DataManipulation

final case class InsertStatement(
  into: TableName,
  insertClause: Insert.InsertClause,
  ifNotExists: Boolean,
  using: Option[Seq[UpdateParam]]
) extends DataManipulation

final case class SelectStatement(
  mod: Option[Select.Mod],
  selection: Select.Selection,
  from: TableName,
  where: Option[WhereClause],
  orderBy: Option[Select.OrderBy],
  perPartitionLimit: Option[Select.LimitParam],
  limit: Option[Select.LimitParam],
  allowFiltering: Boolean
) extends DataManipulation

final case class CreateKeyspace(
  ifNotExists: Boolean,
  keyspaceName: KeyspaceName,
  properties: Seq[Keyspace.KeyspaceOption]
) extends DataDefinition

final case class UseStatement(keyspaceName: KeyspaceName) extends DataDefinition

final case class CreateTable(
  ifNotExists: Boolean,
  tableName: TableName,
  columns: Seq[Table.Column],
  primaryKey: Option[Table.PrimaryKey],
  options: Seq[Table.CreateTableOption]
) extends DataDefinition

final case class CreateIndex(
  isCustom: Boolean,
  ifNotExists: Boolean,
  indexName: Option[String],
  tableName: TableName,
  identifier: Index.IndexIdentifier,
  using: Option[Index.Using]
) extends DataDefinition