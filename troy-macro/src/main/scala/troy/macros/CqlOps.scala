package troy.macros

import java.io.InputStream
import troy.cql.ast.CqlParser
import troy.schema._
import scala.io.Source

object CqlOps {
  import V.Implicits._
  val loadOrParseSchema = Memoize(parseSchemaFromFileName)

  private def parseSchemaFromFileName(path: String): Result[SchemaEngine] =
    Option(this.getClass.getResourceAsStream(path))
      .toV(Messages.SchemaNotFound(path))
      .flatMap(parseSchemaFromInputStream)

  private def parseSchemaFromInputStream(schemaFile: InputStream): Result[SchemaEngine] =
    parseSchemaFromSource(scala.io.Source.fromInputStream(schemaFile))

  private def parseSchemaFromSource(schema: Source) = {
    val lines = schema.getLines()
    val str = lines.mkString("\n")
    parseSchemaFromString(str)
  }

  private def parseSchemaFromString(schema: String): Result[SchemaEngine] =
    CqlParser.parseSchema(schema) match {
      case CqlParser.Success(result, _) =>
        SchemaEngine(result)
      case CqlParser.Failure(msg, next) =>
        V.error(Messages.SchemaParseFailure(msg, next.pos.line, next.pos.column))
    }

  def parseQuery(queryString: String) =
    CqlParser.parseDML(queryString) match {
      case CqlParser.Success(result, _) =>
        V.success(result)
      case CqlParser.Failure(msg, next) =>
        V.error(Messages.QueryParseFailure(msg, next.pos.line, next.pos.column))
    }
}

case class Memoize[T, R](f: T => R) extends (T => R) {
  private val cache = scala.collection.mutable.Map.empty[T, R]
  def apply(x: T) = cache.getOrElseUpdate(x, f(x))
}