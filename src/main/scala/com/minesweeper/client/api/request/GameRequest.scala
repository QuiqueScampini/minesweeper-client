package com.minesweeper.client.api.request

case class GameRequest(user: String, rows: Int, cols: Int, mines: Int)
