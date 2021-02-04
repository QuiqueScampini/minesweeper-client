package com.minesweeper.client.api.response

case class ApiClientGamesResponse(response: Option[GamesResponse], error: Option[ErrorResponse])
case class ApiClientGameResponse(response: Option[GameResponse], error:  Option[ErrorResponse])
