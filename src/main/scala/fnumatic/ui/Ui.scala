package fnumatic.ui

import fnumatic.ebc.{Platine, FuncUnit}
import fnumatic.contract.Types._
import fnumatic.contract._

case class Ui() extends Platine[Data, Message] {
  val print_data = Print_data()
  val read_interaction = Read_interaction()
  in >> print_data.in
  print_data.out >> read_interaction.in
  read_interaction.out_exit >> out

  case class Read_interaction() extends FuncUnit[Unit, Message] {
    val out_exit = out.takeWhile(_ != Exit())

    def process(i: Unit) = {
      val c = try Some(readChar()) catch {case _ => None}
      c.getOrElse('x').toLower match {
        case 'x' => Exit()
        case 'f' => First()
        case 'l' => Last()
        case 'n' => Next()
        case 'p' => Prev()
        case _ => Exit()
      }
    }
  }

  case class Print_data() extends FuncUnit[Data, Unit] {

    def process(l: Data) {
      println("csvviewer v0.1\n")
      l.foreach(println _)
      print("\nN(ext page, P(revious page, F(irst page, L(ast page, eX(it: ")
    }
  }

}


















