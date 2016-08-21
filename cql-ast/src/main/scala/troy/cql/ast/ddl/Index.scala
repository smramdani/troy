package troy.cql.ast.ddl

import troy.cql.ast.MapLiteral

object Index {
  case class Using(using: String, options: Option[MapLiteral])

  trait IndexIdentifier
  case class Identifier(value: String) extends IndexIdentifier
  case class Keys(of: String) extends IndexIdentifier
}
