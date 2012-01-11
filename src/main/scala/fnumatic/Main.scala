package fnumatic

import contract.First
import contract.Types._
import ebc.{Platine}
import logic.{Getpagefrombuffer, Format_page, Config}
import reactive.Var
import ui.Ui

object Main extends App with Platine[Unit, Unit] {
  val data = Var[Data](Nil)
  val lines = Var(0)
  val config = Config()
  val getpage_fromb = Getpagefrombuffer(data, lines)
  val format_page = Format_page(data)
  val ui = Ui()

  config.out >> data
  config.lines >> lines

  getpage_fromb.out >> format_page.in
  format_page.out >> ui.in
  ui.out >> getpage_fromb.in

  config.in.fire(args)
  getpage_fromb.in.fire(First())

}





