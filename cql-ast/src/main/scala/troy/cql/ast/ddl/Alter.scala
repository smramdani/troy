package troy.cql.ast.ddl

import troy.cql.ast.{ DataType, OptionInstruction }
import troy.cql.ast.ddl.Index.Identifier

object Alter {
  sealed trait AlterTableInstruction

  final case class Alter(columnName: Identifier, cqlType: DataType) extends AlterTableInstruction

  final case class Add(instruction: Seq[AddInstruction]) extends AlterTableInstruction
  final case class AddInstruction(columnName: Identifier, cqlType: DataType)

  final case class Drop(columnName: Seq[Identifier]) extends AlterTableInstruction
  final case class With(options: Seq[OptionInstruction]) extends AlterTableInstruction

}
