package com.minesweeper.client.api.response

import GameStatus.GameStatus

case class GameResponse (board: List[RowResponse],
                         gameTime: Int,
                         id: Int,
                         leftFlags: Int,
                         status: GameStatus,
                         user: String)

