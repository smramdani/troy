package troy.cql.ast.dml

object Delete {
  sealed trait IfCondition
  case class SimpleIfCondition(conditions: Seq[Condition]) extends IfCondition
  case class Exist(value: Boolean) extends IfCondition
}
