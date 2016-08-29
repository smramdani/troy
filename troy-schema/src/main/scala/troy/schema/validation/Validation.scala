package troy.schema.validation

import troy.cql.ast.DataManipulation
import troy.schema._

trait Validation {
  def validate(statement: DataManipulation): Result[Iterable[Message]] =
    rules.applyOrElse(statement, (_: DataManipulation) => noMessages)

  protected[this] def rules: PartialFunction[DataManipulation, Result[Iterable[Message]]]

  protected[this] val noMessages: Result[Seq[Message]] = V.success(Seq.empty)
}

class Validations(schema: Schema, levelConfig: Message => Validations.Level) {
  def validate(statement: DataManipulation): Result[_] =
    V.merge(all.map(_.validate(statement).flatMap(adjustMessageLevels)))

  def adjustMessageLevels(msgs: Iterable[Message]) = {
    val groupedMsgs = groupByLevel(msgs)
    val warns = groupedMsgs.getOrElse(Validations.Warn, Iterable.empty)
    val errors = groupedMsgs.getOrElse(Validations.Error, Iterable.empty)

    V.merge(emptyResponse.addWarns(warns) +: errors.map(e => V.error(e)).toSeq)
  }

  def groupByLevel(msgs: Iterable[Message]) =
    msgs.map(pairWithLevel).groupBy(_._1).mapValues(_.map(_._2))

  def pairWithLevel(m: Message) = levelConfig(m) -> m

  val all = Seq(
    new SelectDistinctNonStaticColumns(schema),
    new WhereNonPrimaryNoIndex(schema)
  )

  protected[this] val emptyResponse: Result[Unit] = V.success(())
}

object Validations {
  def apply(schema: Schema) = new Validations(schema, m => levelConfig(m.productPrefix))

  sealed trait Level
  case object Warn extends Level
  case object Error extends Level
  case object Off extends Level

  val levelConfig: Map[String, Level] = Map( // TODO: Read from config file
    "SelectedDistinctNonStaticColumn" -> Error
  ).withDefaultValue(Error)
}
