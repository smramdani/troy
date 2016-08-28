package troy.schema

import troy.cql.ast.{ Identifier, KeyspaceName, TableName, CreateKeyspace, CreateTable, CreateIndex, DataType }
import V.Implicits._
import troy.cql.ast.ddl.Table.PrimaryKey

case class Schema(keyspaces: Map[KeyspaceName, Keyspace]) extends AnyVal {
  def getKeyspace(kn: Option[KeyspaceName]): Result[Keyspace] =
    kn.toV(Messages.KeyspaceNotSpecified).flatMap(getKeyspace)

  def getKeyspace(kn: KeyspaceName): Result[Keyspace] =
    keyspaces.get(kn).toV(Messages.KeyspaceNotFound(kn))

  def getTable(tn: TableName): Result[Table] =
    getKeyspace(tn.keyspace).flatMap(_.getTable(tn))

  def getColumns(tn: TableName): Result[Iterable[Column]] =
    getTable(tn).map(_.columns.values)

  def getColumns(tn: TableName, cs: Seq[Identifier]): Result[Seq[Column]] =
    getTable(tn).flatMap(table => V.merge(cs.map(table.getColumn)))

  def apply(ck: CreateKeyspace): Result[Schema] =
    if (keyspaces.contains(ck.keyspaceName))
      if (ck.ifNotExists)
        V.success(this, Messages.KeyspaceAlreadyExists(ck.keyspaceName))
      else
        V.error(Messages.KeyspaceAlreadyExists(ck.keyspaceName))
    else
      V.success(setKeyspace(Keyspace.empty(ck.keyspaceName)))

  protected[this] def setKeyspace(k: Keyspace) =
    copy(keyspaces + (k.name -> k))

  def apply(ct: CreateTable): Result[Schema] =
    getKeyspace(ct.tableName.keyspace).flatMap(_.apply(ct)).map(setKeyspace)

  def apply(ci: CreateIndex): Result[Schema] =
    getKeyspace(ci.tableName.keyspace).flatMap(_.apply(ci)).map(setKeyspace)
}

case class Keyspace(name: KeyspaceName, tables: Map[TableName, Table]) {
  def getTable(table: TableName): Result[Table] =
    tables.get(table).toV(Messages.TableNotFound(table))

  def apply(ct: CreateTable): Result[Keyspace] =
    if (tables.contains(ct.tableName))
      if (ct.ifNotExists)
        V.success(this, Messages.TableAlreadyExists(ct.tableName))
      else
        V.error(Messages.TableAlreadyExists(ct.tableName))
    else
      Table.fromStatement(ct).map(setTable)

  def apply(ci: CreateIndex): Result[Keyspace] =
    getTable(ci.tableName).flatMap(_.apply(ci)).map(setTable)

  def setTable(t: Table) =
    copy(tables = tables + (t.name -> t))
}
object Keyspace {
  def empty(name: KeyspaceName) = Keyspace(name, Map.empty)
}

case class Table(name: TableName, columns: Map[Identifier, Column], primaryKey: PrimaryKey) {
  def getColumn(c: Identifier): Result[Column] =
    columns.get(c).toV(Messages.ColumnNotFound(c, name))

  def apply(ci: CreateIndex): Result[Table] =
    getColumn(ci.identifier.columnName).flatMap(_.apply(ci)).map(setColumn)

  def setColumn(c: Column) = copy(columns = columns + (c.name -> c))
}
object Table {
  def fromStatement(t: CreateTable): Result[Table] =
    t.primaryKey
      .orElse(t.columns.find(_.isPrimaryKey).map(c => PrimaryKey.simple(c.name)))
      .toV(Messages.PrimaryKeyNotDefined(t.tableName))
      .map { pk =>
        val columnsMap = t.columns.map(c => c.name -> Column(c.name, c.dataType, c.isStatic)).toMap
        Table(t.tableName, columnsMap, pk)
      }
}
case class Column(name: String, dataType: DataType, isStatic: Boolean, valueIndexes: Set[Index] = Set.empty, keyIndexes: Set[Index] = Set.empty) {
  def apply(ci: CreateIndex): Result[Column] =
    ci.identifier match {
      case _: troy.cql.ast.ddl.Index.Identifier => addValueIndex(ci)
      case _: troy.cql.ast.ddl.Index.Keys       => addKeyIndex(ci)
    }

  def addValueIndex(ci: CreateIndex): Result[Column] =
    validateIndex(ci, valueIndexes) { index =>
      copy(valueIndexes = valueIndexes + index)
    }

  def addKeyIndex(ci: CreateIndex): Result[Column] =
    validateIndex(ci, keyIndexes) { index =>
      copy(keyIndexes = keyIndexes + index)
    }

  def validateIndex(ci: CreateIndex, indexes: Set[Index])(onSuccess: Index => Column): Result[Column] = {
    val index = Index.fromStatement(ci)
    if (indexes.contains(index))
      if (ci.ifNotExists)
        V.success(this, Messages.IndexAlreadyExists(ci.indexName, index, name))
      else
        V.error(Messages.IndexAlreadyExists(ci.indexName, index, name))
    else
      V.success(onSuccess(index))
  }
}

sealed trait Index
object Index {
  case object Native extends Index
  case object SASI extends Index
  case object Custom extends Index

  def fromStatement(ct: CreateIndex): Index = ct.using.map(_.using match {
    case "org.apache.cassandra.index.sasi.SASIIndex" => SASI
    case _                                           => Custom
  }).getOrElse(Native)
}
