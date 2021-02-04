package com.minesweeper.client.api.response

case class GamesResponse (games: List[GameResponse], user: Option[String])

