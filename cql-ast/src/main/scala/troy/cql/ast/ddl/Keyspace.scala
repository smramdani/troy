package troy.cql.ast.ddl

object Keyspace {
  trait KeyspaceOption
  case class Replication(options: Seq[(String, String)]) extends KeyspaceOption // TODO
}
case class KeyspaceName(name: String)
