package troy.schema

import troy.cql.ast._
import troy.cql.ast.dml.Operator

/**
 * Expresses an issue, can be interpreted as warn or error based on configured levels
 */
sealed abstract class Message(val message: String) extends Product
object Messages {
  case object KeyspaceNotSpecified extends Message("Keyspace not specified")
  case class KeyspaceNotFound(k: KeyspaceName) extends Message(s"Keyspace '${k.name}' not found")
  case class TableNotFound(t: TableName) extends Message(s"Table '$t' not found")
  case class ColumnNotFound(c: Identifier, t: TableName) extends Message(s"Column '$c' not found in table '$t'")
  case class OperatorNotSupported(op: Operator, dt: DataType) extends Message(s"Operator '$op' doesn't support column type '$dt'")

  case class KeyspaceAlreadyExists(k: KeyspaceName) extends Message(s"Keyspace $k already exists")
  case class TableAlreadyExists(t: TableName) extends Message(s"Table $t already exists")
  case class IndexAlreadyExists(name: Option[String], index: Index, on: String) extends Message(s"Index ${name.getOrElse("")} of type $index already exists on $on")

  case class PrimaryKeyNotDefined(t: TableName) extends Message(s"CREATE TABLE $t statement has no Primary key defined")

  // Macro
  case class SchemaNotFound(path: String) extends Message(s"Can't find schema file $path")
  case class SchemaParseFailure(msg: String, line: Int, column: Int) extends Message(s"Failure during parsing the schema. Error ($msg) near line $line, column $column")
  case class QueryParseFailure(msg: String, line: Int, column: Int) extends Message(s"Failure during parsing query. Error ($msg) near line $line, column $column")

  // Validations
  case class SelectedDistinctNonStaticColumn(c: Identifier) extends Message(s"SELECT DISTINCT queries must only request partition key columns and/or static columns (not $c)")
}
