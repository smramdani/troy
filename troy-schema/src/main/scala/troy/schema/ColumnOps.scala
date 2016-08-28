package troy.schema

import troy.cql.ast.DataType
import troy.cql.ast.dml.Operator

object ColumnOps {
  implicit class Operations(val column: Column) extends AnyVal {
    import Operator._
    import DataType._

    // TODO: Add NotEquals, In
    def operandType(operator: Operator): Result[DataType] = operator match {
      case Equals | LessThan | GreaterThan | LessThanOrEqual | GreaterThanOrEqual =>
        V.Success(column.dataType).collect(operatorNotSupported(operator)) {
          case n: Native => n
        }
      case Contains =>
        V.Success(column.dataType).collect(operatorNotSupported(operator)) {
          case list(t)   => t
          case set(t)    => t
          case map(_, v) => v
        }
      case ContainsKey =>
        V.Success(column.dataType).collect(operatorNotSupported(operator)) {
          case map(k, _) => k
        }
    }

    private def operatorNotSupported(op: Operator) =
      Messages.OperatorNotSupported(op, column.dataType)
  }
}