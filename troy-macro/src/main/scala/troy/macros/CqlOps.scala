package troy.macros

import java.io.InputStream
import troy.cql.ast.CqlParser
import troy.schema._
import scala.io.Source

object CqlOps {
  import V.Implicits._
  val loadOrParseSchema = Memoize(parseSchemaFromPath)

  private def parseSchemaFromPath(path: String): Result[SchemaEngine] =
    Option(this.getClass.getResourceAsStream(path))
      .toV(Messages.SchemaNotFound(path))
      .map(scala.io.Source.fromInputStream)
      .map(_.getLines().toSeq)
      .flatMap { r =>
        r.foldLeft(Result(SchemaEngine.empty)) {
          case (previous, file) => previous.flatMap(parseSchemaFromFileName(path + file, _))
        }
      }

  private def parseSchemaFromFileName(path: String, previous: SchemaEngine): Result[SchemaEngine] =
    Option(this.getClass.getResourceAsStream(path))
      .toV(Messages.SchemaNotFound(path))
      .flatMap(parseSchemaFromInputStream(_, previous))

  private def parseSchemaFromInputStream(schemaFile: InputStream, previous: SchemaEngine): Result[SchemaEngine] =
    parseSchemaFromSource(scala.io.Source.fromInputStream(schemaFile), previous)

  private def parseSchemaFromSource(schema: Source, previous: SchemaEngine) = {
    val lines = schema.getLines()
    val str = lines.mkString("\n")
    parseSchemaFromString(str, previous)
  }

  private def parseSchemaFromString(schema: String, previous: SchemaEngine): Result[SchemaEngine] =
    CqlParser.parseSchema(schema) match {
      case CqlParser.Success(result, _) =>
        SchemaEngine(result, previous)
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