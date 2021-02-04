package com.minesweeper.client.api.response

object GameStatus extends Enumeration {
  type GameStatus = Value
  val CREATED, IN_PROGRESS, PAUSED, LOST, WON = Value
}
