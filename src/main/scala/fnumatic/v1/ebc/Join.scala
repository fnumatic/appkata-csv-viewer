package fnumatic.v1.ebc

import reactive._

case class Join[T, X](in_1: EventStream[T], in_2: EventStream[X]) extends Observing {
  val joined1 = in_1.map(_ => true).hold(false)
  val joined2 = in_2.map(_ => true).hold(false)
  val v1 = in_1.hold(null.asInstanceOf[T])
  val v2 = in_2.hold(null.asInstanceOf[X])


  val out = (v1 zip v2).change.filter(_ => joined1.now && joined2.now)
}




