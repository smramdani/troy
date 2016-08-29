package troy.schema

/**
 * V for Validation، similar to Either but also includes Warnings
 * Designed to be used with Macros to hold compiler warn/error messages.
 * S = Success
 * E = Error
 * W = Warn
 */
sealed trait V[+W, +E, +S] extends Product with Serializable {
  def flatMap[WW >: W, EE >: E, SS](f: S => V[WW, EE, SS]): V[WW, EE, SS]
  def map[SS](f: S => SS): V[W, E, SS]
  def collect[EE >: E, SS](default: => EE)(pf: PartialFunction[S, SS]): V[W, EE, SS]
  def addWarns[WW >: W](ws2: Iterable[WW]): V[WW, E, S]
  def get: S // Only for test cases
}
object V {
  import Implicits._
  final case class Success[+W, +S](value: S, ws: Seq[W] = Seq.empty) extends V[W, Nothing, S] {
    override def flatMap[WW >: W, EE, SS](f: S => V[WW, EE, SS]): V[WW, EE, SS] = f(value).addWarns(ws)
    override def map[SS](f: S => SS): V[W, Nothing, SS] = copy(f(value), ws)
    override def collect[EE, SS](default: => EE)(pf: PartialFunction[S, SS]): V[W, EE, SS] =
      pf.lift(value).toV(default)
    override def addWarns[WW >: W](ws2: Iterable[WW]): V[WW, Nothing, S] = copy(ws = ws ++ ws2)
    override def get: S = value
  }
  final case class Error[+W, +E](es: Seq[E], ws: Seq[W] = Seq.empty) extends V[W, E, Nothing] {
    override def flatMap[WW >: W, EE >: E, SS](f: Nothing => V[WW, EE, SS]): V[WW, EE, SS] = this
    override def map[SS](f: Nothing => SS): V[W, E, SS] = this
    override def collect[EE >: E, SS](default: => EE)(pf: PartialFunction[Nothing, SS]): V[W, EE, SS] = this
    override def addWarns[WW >: W](ws2: Iterable[WW]): V[WW, E, Nothing] = copy(ws = ws ++ ws2)
    override def get: Nothing = throw new NoSuchElementException(s"Error.get ${ws.mkString(", ")}")
  }
  def success[W, S](s: S, ws: W*) = Success(s, ws)
  def error[W, E](e: E, warnings: Seq[W] = Seq.empty) = Error(Seq(e), warnings)

  def merge[W, E, S](vs: Seq[V[W, E, S]]) = vs.foldLeft[V[W, E, Seq[S]]](Success(Seq.empty)) {
    case (Success(vs, wss), Success(v, ws)) => Success(vs :+ v, wss ++ ws)
    case (Error(ess, wss), Error(es, ws))   => Error(ess ++ es, wss ++ ws)
    case (Success(vs, wss), Error(es, ws))  => Error(es, wss ++ ws)
    case (Error(ess, wss), Success(v, ws))  => Error(ess, wss ++ ws)
  }

  object Implicits {
    implicit class VOptionOps[S](val o: Option[S]) extends AnyVal {
      def toV[E](e: => E) = o.map(s => Success(s)).getOrElse(error(e))
    }
  }
}