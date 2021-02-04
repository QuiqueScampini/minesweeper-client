package com.minesweeper.client.api.response

object CellContent extends Enumeration {
  type CellContent = Value
  val FLAG, QUESTION, NONE, REVEALED = Value
}
