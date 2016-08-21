package troy.schema

import troy.cql.ast.DataType
import troy.cql.ast.ddl.Table
import troy.cql.ast.dml.Operator

object ColumnOps {
  implicit class Operations(val column: Table.Column) extends AnyVal {
    import Operator._
    import DataType._

    // TODO: Add NotEquals, In
    def operandType(operator: Operator): Option[DataType] = operator match {
      case Equals | LessThan | GreaterThan | LessThanOrEqual | GreaterThanOrEqual =>
        Some(column.dataType).collect {
          case n: Native => n
        }
      case Contains =>
        Some(column.dataType).collect {
          case list(t)   => t
          case set(t)    => t
          case map(_, v) => v
        }
      case ContainsKey =>
        Some(column.dataType).collect {
          case map(k, _) => k
        }
    }
  }
}

