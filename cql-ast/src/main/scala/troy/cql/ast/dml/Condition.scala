package troy.cql.ast.dml

import troy.cql.ast.Term

case class Condition(simpleSelection: SimpleSelection, operator: Operator, term: Term)
