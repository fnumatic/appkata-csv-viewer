package fnumatic.logic

import fnumatic.ebc.{Platine, FuncUnit}
import fnumatic.contract.Types._
import fnumatic.contract._
import reactive.Signal

case class Getpagefrombuffer(data: Signal[Data], lines: Signal[Int]) extends Platine[Message, DataPos] {
  val calc_slices = Calc_slices()
  val slice_table = Slice_table()

  in >> calc_slices.in
  calc_slices.out >> slice_table.in
  slice_table.out >> out

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



















