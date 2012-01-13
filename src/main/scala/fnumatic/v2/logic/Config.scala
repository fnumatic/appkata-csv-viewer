package fnumatic.v2.logic

import fnumatic.v2.contract.Types._
import reactive.EventSource
import scala.{Array, Int}
import fnumatic.v2.ebc.{FuncUnit, Platine}
import io.Source

case class Config() extends Platine[Array[String], Data] {
  val parse_parameters = Parse_parameters()
  val read_file = Read_file()
  val lines = new EventSource[Int] {}

  this |> parse_parameters
  parse_parameters.file >> read_file.in
  parse_parameters.lines >> lines
  read_file >| this

  case class Parse_parameters() extends FuncUnit[Array[String], (Source, Int)] {
    val file = out.map(_._1)
    val lines = out.map(_._2)

    def process(par: Array[String]) = par.toList match {
      case Nil => (Source.fromFile("persons.csv"), 4)
      case string :: int :: tail => (Source.fromFile(string), int.toInt)
      sys.exit(1)
    }
  }

  case class Read_file() extends FuncUnit[Source, Data] {
    def process(f: Source) = f.getLines().filterNot(_ == "").toList
  }

}





