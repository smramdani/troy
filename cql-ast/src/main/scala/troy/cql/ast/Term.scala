package troy.cql.ast

sealed trait Term

final case class Constant(raw: String) extends Term
sealed trait Literal extends Term
sealed trait CollectionLiteral extends Literal
final case class MapLiteral(pairs: Seq[(Term, Term)]) extends CollectionLiteral
final case class SetLiteral(values: Seq[Term]) extends CollectionLiteral
final case class ListLiteral(values: Seq[Term]) extends CollectionLiteral

final case class UdtLiteral(members: Seq[(Identifier, Term)]) extends Literal
final case class TupleLiteral(values: Seq[Term]) extends Literal

final case class FunctionCall(functionName: Identifier, params: Seq[Term]) extends Term
final case class TypeHint(cqlType: DataType, term: Term) extends Term
sealed trait BindMarker extends Term
object BindMarker {
  case object Anonymous extends BindMarker
  final case class Named(name: Identifier) extends BindMarker
}