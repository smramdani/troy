package troy.cql.parser

import org.scalatest.{ FlatSpec, Matchers }
import troy.cql.ast.CqlParser

object ParserTestUtils extends FlatSpec with Matchers {
  def parseSchema(statement: String) =
    CqlParser.parseSchema(statement) match {
      case CqlParser.Success(res, _)    => res
      case CqlParser.Failure(msg, next) => fail(s"Parse Failure: $msg, line = ${next.pos.line}, column = ${next.pos.column}")
    }

  def parseSchemaAs[T](statement: String) =
    parseSchema(statement)
      .head
      .asInstanceOf[T]

  def parseQuery(statement: String) =
    CqlParser
      .parseDML(statement) match {
        case CqlParser.Success(res, _)    => res
        case CqlParser.Failure(msg, next) => fail(s"Parse Failure: $msg, line = ${next.pos.line}, column = ${next.pos.column}")
      }
}
