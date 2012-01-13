package fnumatic.v2.ui

import fnumatic.v2.contract.Types._
import fnumatic.v2.contract._
import scala.{Unit, None}
import fnumatic.v2.ebc.{ FuncUnit, Platine}

case class Ui() extends Platine[Data, Message] {
  val print_data = Print_data()
  val read_interaction = Read_interaction()
  val exit = Read_exit()

  (this |> print_data) >> read_interaction >> exit >| this

  case class Read_interaction() extends FuncUnit[Unit, Message] {

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
      println("csvviewer v0.2\n")
      l.foreach(println _)
      print("\nN(ext page, P(revious page, F(irst page, L(ast page, eX(it: ")
    }
  }

  case class Read_exit() extends fnumatic.v2.ebc.Exit[Message,Message](Exit()){
    def process(mess: Message) = mess
  }
}


















