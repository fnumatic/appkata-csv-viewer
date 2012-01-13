package fnumatic.v1.ebc

import reactive.{EventSource, Observing}

trait Fu[T] extends Observing {
  val in = new EventSource[T] {}
}
trait Platine[T, U] extends Fu[T] {
  val out = new EventSource[U] {}
}
trait FuncUnit[T, U] extends Fu[T] {
  val out = in map process _

  def process(i: T): U
}