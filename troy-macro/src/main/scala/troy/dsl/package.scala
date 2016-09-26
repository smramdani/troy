package troy

import com.datastax.driver.core._
import troy.macros._
import troy.schema.VersionedSchemaEngine

import scala.annotation.compileTimeOnly
import scala.concurrent.Future

package object dsl {

  // $COVERAGE-OFF$
  implicit class RichStringContext(val context: StringContext) extends AnyVal {
    @compileTimeOnly("cql Strings can be used only inside troy.dsl.withSchema block")
    def cql(args: Any*): MacroDSL.TroyCql = ???
  }

  trait WithSchema {
    def minVersion(v: VersionedSchemaEngine.Version) = new WithSchema {
      def maxVersion(v: VersionedSchemaEngine.Version) = withSchema
    }
    def apply[F](code: F): F = macro troyImpl[F]
  }
  // $COVERAGE-ON$
  object withSchema extends WithSchema

  implicit class MacroDsl_RichStatement(val statement: Statement) extends ParsingOps {
    type ParseAs[R] = Future[Seq[R]]
  }

  implicit class MacroDsl_RichFutureBoundStatement(val xxx: Future[Statement]) extends ParsingOps {
    type ParseAs[R] = Future[Seq[R]]
  }

  implicit class MacroDsl_RichResultSet(val xxx: ResultSet) extends ParsingOps {
    type ParseAs[R] = Seq[R]
  }

  implicit class MacroDsl_RichFutureOfResultSet(val xxx: Future[ResultSet]) extends ParsingOps {
    type ParseAs[R] = Future[Seq[R]]
  }

  implicit class MacroDsl_RichFutureOfSeqOfRow(val xxx: Future[Seq[Row]]) extends ParsingOps {
    type ParseAs[R] = Future[Seq[R]]
  }

  implicit class MacroDsl_RichFutureOfOptionOfRow(val xxx: Future[Option[Row]]) extends ParsingOps {
    type ParseAs[R] = Future[Option[R]]
  }

  implicit class MacroDsl_RichSeqOfRow(val xxx: Seq[Row]) extends ParsingOps {
    type ParseAs[R] = Seq[R]
  }

  implicit class MacroDsl_RichJavaListOfRow(val xxx: java.util.List[Row]) extends ParsingOps {
    type ParseAs[R] = Seq[R]
  }

  implicit class MacroDsl_RichOptionOfRow(val xxx: Option[Row]) extends ParsingOps {
    type ParseAs[R] = Option[R]
  }
}
