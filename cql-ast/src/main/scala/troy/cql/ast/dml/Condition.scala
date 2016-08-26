package troy.cql.ast.dml

import troy.cql.ast.Term

final case class Condition(simpleSelection: SimpleSelection, operator: Operator, term: Term)

sealed trait IfCondition
final case class SimpleIfCondition(conditions: Seq[Condition]) extends IfCondition
final case class Exist(value: Boolean) extends IfCondition