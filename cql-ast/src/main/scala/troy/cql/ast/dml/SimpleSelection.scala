package troy.cql.ast.dml

import troy.cql.ast.{ Term, _ }

sealed trait SimpleSelection
final case class ColumnNameSelection(columnName: Identifier) extends SimpleSelection
final case class ColumnNameSelectionWithTerm(columnName: Identifier, term: Term) extends SimpleSelection
final case class ColumnNameSelectionWithFieldName(columnName: Identifier, fieldName: String) extends SimpleSelection
