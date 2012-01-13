package fnumatic.v2.logic

import fnumatic.v2.contract.Types._
import reactive.Signal
import scala.Int
import fnumatic.v2.ebc.{FuncUnit, Platine}
import fnumatic.v2.contract._

case class Getpagefrombuffer(data: Signal[Data], lines: Signal[Int]) extends Platine[Message, DataPos] {
  val calc_slices = Calc_slices()
  val slice_table = Slice_table()

  (this |> calc_slices) >> slice_table >| this

  case class Calc_slices() extends FuncUnit[Message, Position] {
    val old_slice = out.hold(Position(0, 0, 0))

    def process(direction: Message): Position = {
      val lines_count = lines.now
      val slide = old_slice.now.slide
      val max = data.now.tail.sliding(lines_count, lines_count).length

      def first = 0
      def next = if (slide == max - 1) max - 1 else slide + 1
      def previous = if (slide == 0) 0 else slide - 1
      def last = max - 1

      def calc(l: Int) = Position(l * lines_count, l * lines_count + lines_count, l)

      direction match {
        case First() => calc(first)
        case Next() => calc(next)
        case Prev() => calc(previous)
        case Last() => calc(last)
        case _ => calc(first)
      }
    }
  }

  case class Slice_table() extends FuncUnit[Position, DataPos] {
    def process(pos: Position) = DataPos(data.now.tail.slice(pos.begin, pos.end), pos)
  }

}



















