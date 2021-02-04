package com.minesweeper.client

import com.minesweeper.client.api.response
import com.minesweeper.client.api.response.CellContent.{CellContent, FLAG, NONE, QUESTION, REVEALED}
import com.minesweeper.client.api.response._
import org.joda.time.LocalDateTime
import org.json4s.JsonAST.{JString, JValue}
import org.json4s.ext.JodaTimeSerializers
import org.json4s.jackson.JsonMethods
import org.json4s.{CustomSerializer, DefaultFormats, Formats, JArray, JNull}

object JsonParser {

  implicit val formats: Formats = DefaultFormats +
    CellContentSerializer ++
    JodaTimeSerializers.all

  def parseGames(error: String): Option[GamesResponse] = {
    val value = JsonMethods.parse(error)
    val user = (value \ "user").extractOpt[String]

    val games = (value \ "games").extract[JArray] match {
      case JArray(games) => games.map(game => parseGame(game))
      case _ => List()
    }
    Some(GamesResponse(games,user))
  }

  def parseGame(error: String): Option[GameResponse] = {
    val game = JsonMethods.parse(error)
    Some(parseGame(game))
  }

  def parseGame(game : JValue): GameResponse = {
    val status = (game \ "status").extract[String]
    val id = (game \ "id").extract[Int]
    val gameTime = (game \ "gameTime").extract[Int]
    val leftFlags = (game \ "leftFlags").extract[Int]
    val user = (game \ "user").extract[String]

    val board = (game \ "board").extract[JArray] match {
      case JArray(rows) => rows.map(row => parseRow(row))
      case _ => List()
    }

    response.GameResponse(board, gameTime, id, leftFlags, GameStatus.withName(status),user)
  }

  def parseRow(row : JValue): RowResponse = {
    val cols = (row \ "cols") match {
      case JArray(cells) => cells.map(cell => parseCell(cell))
      case _ => List()
    }
    RowResponse(cols)
  }

  def parseCell(cell : JValue): CellResponse = {
    val value = (cell \ "value").extractOpt[Int]
    val content = (cell \ "content").extract[String]
    CellResponse(CellContent.withName(content),value)
  }

  def parseError(error: String): Option[ErrorResponse] = {
    val value = JsonMethods.parse(error)
    val status = (value \ "status").extract[String]
    val message = (value \ "message").extract[String]
    val timestamp = (value \ "timestamp").extract[LocalDateTime]
    Some(ErrorResponse(status,message, timestamp))
  }
}

case object CellContentSerializer extends CustomSerializer[CellContent](format => ( {
  case JString(dt) => {
    dt match {
      case "FLAG"   => FLAG
      case "QUESTION"  => QUESTION
      case "NONE" => NONE
      case "REVEALED" => REVEALED
    }
  }
  case JNull       => null
}, {case g: CellContent => JString(g.toString)}))
