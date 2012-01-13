package fnumatic.v2.ebc

import reactive.{EventSource, Observing}


trait Fu[T, U] extends Observing {
  val in  = new EventSource[T] {}
  val out = new EventSource[U] {}

  def >>[S](target: Fu[U, S]) = {
    out.foreach(target.in.fire _)
    target
  }

  /**
   * out to out
   */
  def >|[S](target: Fu[S, U]) = {
    out.foreach(target.out.fire _)
    target
  }

  /**
   * in to in
   */
  def |>[Z](target: Fu[T, Z]) = {
    in.foreach(target.in.fire _)
    target
  }
}

trait Platine[T, U] extends Fu[T, U]

trait FuncUnit[T, U] extends Fu[T, U] {
  in =>> {v => out.fire(process(v))}

  def process(i: T): U
}

case class Sink[T](zero: T) extends Fu[T, T] {
  var v = zero
  in =>> {
    ss =>
      v = ss
      out.fire(ss)
  }

  def value = v
}

abstract class Exit[T, U](exit: T) extends Fu[T, U] {
  in.takeWhile(_ != exit).foreach(v => out.fire(process(v)))

  def process(i: T): U
}