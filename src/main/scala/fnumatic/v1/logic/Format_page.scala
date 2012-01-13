package fnumatic.v1.logic

import fnumatic.v1.ebc.{Platine, FuncUnit}
import fnumatic.v1.contract.Types._
import fnumatic.v1.contract._
import reactive.Signal

case class Format_page(data: Signal[Data]) extends Platine[DataPos, Data] {
  val div = ";"
  val prepend_indices = Prepend_indices()
  val parse_lines = Parse_lines()
  val normalize_table = Normalize_table()
  val format_table = Format_table()

  in >> prepend_indices.in
  prepend_indices.out >> parse_lines.in
  parse_lines.out >> normalize_table.in
  normalize_table.out >> format_table.in
  format_table.out >> out

  case class Parse_lines() extends FuncUnit[Data, PData] {
    def process(d: Data) = d.map(_.split(div).toList)
  }

  case class Normalize_table() extends FuncUnit[PData, PData] {
    def process(d: PData) = {
      val sizes = d.map(_.map(_.size))
      val colsizes = sizes.transpose.map(_.max)
      def normalize(ll: List[String]) = ll.zipWithIndex.map {case (v, i) => v.padTo(colsizes(i), ' ')}

      d.map(normalize _)
    }
  }

  case class Format_table() extends FuncUnit[PData, Data] {
    def process(d: PData) = {

      def line(l: List[String]) = l.mkString("|") + '|'
      def underline(l: Data) = "".padTo(line(l).size, '-')
      def header(l: List[String]) = line(l) + '\n' + underline(l)

      d.zipWithIndex.map {
        case (v, 0) => header(v)
        case (v, _) => line(v)
      }
    }
  }

  case class Prepend_indices() extends FuncUnit[DataPos, Data] {
    def process(dp: DataPos) = {
      val pluspos = dp.data.zipWithIndex.map {case (value, index) => (dp.pos.begin + index + 1) + div + value}

      ("Pos" + div ++ data.now.head) +: pluspos
    }
  }

}




















