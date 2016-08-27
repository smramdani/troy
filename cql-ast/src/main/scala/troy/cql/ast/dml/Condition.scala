package troy.cql.ast.dml

import troy.cql.ast.Term

final case class Condition(simpleSelection: SimpleSelection, operator: Operator, term: Term)

sealed trait IfExistsOrCondition
final case class IfCondition(conditions: Seq[Condition]) extends IfExistsOrCondition
final case class IfExist(value: Boolean) extends IfExistsOrCondition