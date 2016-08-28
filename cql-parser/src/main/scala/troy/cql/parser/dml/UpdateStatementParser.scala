package troy.cql.parser.dml

import troy.cql.ast.CqlParser._
import troy.cql.ast.UpdateStatement
import troy.cql.ast.dml.Update._

trait UpdateStatementParser {
  def updateStatement: Parser[UpdateStatement] = {
    def assignment: Parser[Assignment] = {

      def simpleSelectionAssignment =
        simpleSelection ~ ("=".i ~> term) ^^^^ SimpleSelectionAssignment

      def termAssignment = {
        val column1 = identifier <~ "=".i
        val column2 = identifier <~ ("+".i | "-".i)
        column1 ~ column2 ~ term ^^^^ TermAssignment
      }

      def listLiteralAssignment = {
        val column1 = identifier <~ "=".i
        val column2 = "+".i ~> identifier
        column1 ~ listLiteral ~ column2 ^^^^ ListLiteralAssignment
      }

      simpleSelectionAssignment | termAssignment | listLiteralAssignment
    }

    def set = "SET".i ~> rep1sep(assignment, ",")

    "UPDATE".i ~>
      tableName ~
      using ~
      set ~
      where ~
      ifExistsOrCondition.? ^^^^ UpdateStatement.apply
  }
}