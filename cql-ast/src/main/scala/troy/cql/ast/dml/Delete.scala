package troy.cql.ast.dml

object Delete {
  sealed trait IfCondition
  final case class SimpleIfCondition(conditions: Seq[Condition]) extends IfCondition
  final case class Exist(value: Boolean) extends IfCondition
}
