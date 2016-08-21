package troy.cql.parser.ddl

import org.scalatest._
import troy.cql.ast.{ CqlParser, CreateKeyspace }
import troy.cql.ast.ddl.Keyspace

class CreateKeyspaceParserTest extends FlatSpec with Matchers {

  "Create Keyspace Parser" should "parse simple create keyspace" in {
    val statement = parseSchemaAs[CreateKeyspace]("create KEYSPACE test WITH replication = {'class': 'SimpleStrategy' , 'replication_factor': '1'}; ")

    statement.ifNotExists shouldBe false
    statement.keyspaceName.name shouldBe "test"
    statement.properties shouldBe Seq(Keyspace.Replication(Seq(("class", "SimpleStrategy"), ("replication_factor", "1"))))
  }

  def parseSchema(statement: String) =
    CqlParser.parseSchema(statement) match {
      case CqlParser.Success(res, _)    => res
      case CqlParser.Failure(msg, next) => fail(s"Parse Failure: $msg, line = ${next.pos.line}, column = ${next.pos.column}")
    }

  def parseSchemaAs[T](statement: String) =
    parseSchema(statement)
      .head
      .asInstanceOf[T]
}
