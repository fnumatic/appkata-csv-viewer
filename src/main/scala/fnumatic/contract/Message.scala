package fnumatic.contract

sealed abstract class Message
case class Next() extends Message
case class Prev() extends Message
case class First() extends Message
case class Last() extends Message
case class Exit() extends Message






















