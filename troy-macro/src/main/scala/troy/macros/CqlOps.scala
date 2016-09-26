package troy.macros

import java.io.InputStream
import troy.cql.ast.CqlParser
import troy.schema.V.Success
import troy.schema._
import scala.io.Source

object CqlOps {
  import V.Implicits._
  val loadOrParseSchema = Memoize(parseSchemaFromPath)

  private def parseSchemaFromPath(path: String): Result[VersionedSchemaEngine] =
    Option(this.getClass.getResourceAsStream(path))
      .toV(Messages.SchemaNotFound(path))
      .map(scala.io.Source.fromInputStream)
      .map(_.getLines().toSeq)
      .flatMap { r =>
        V.merge(r.map { file =>
          val source = scala.io.Source.fromInputStream(this.getClass.getResourceAsStream(path + file)).getLines().mkString("\n")
          CqlParser
            .parseSchema(source)
            .toV(f => Messages.SchemaParseFailure(f.msg, f.next.pos.line, f.next.pos.column))
        })
      }
      .flatMap(VersionedSchemaEngine.apply)

  def parseQuery(queryString: String) =
    CqlParser
      .parseDML(queryString)
      .toV(f => Messages.QueryParseFailure(f.msg, f.next.pos.line, f.next.pos.column))

  implicit class VParseResultOps[T](val r: CqlParser.ParseResult[T]) extends AnyVal {
    def toV(e: CqlParser.Failure => Message): Result[T] =
      r match {
        case CqlParser.Success(result, _) =>
          V.success(result)
        case f: CqlParser.Failure =>
          V.error(e(f))
      }
  }
}

case class Memoize[T, R](f: T => R) extends (T => R) {
  private val cache = scala.collection.mutable.Map.empty[T, R]
  def apply(x: T) = cache.getOrElseUpdate(x, f(x))
}