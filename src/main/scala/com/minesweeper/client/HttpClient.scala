package com.minesweeper.client

import com.minesweeper.client.JsonParser.{parseError, parseGame, parseGames}
import com.minesweeper.client.api.request.{ActionRequest, GameRequest}
import com.minesweeper.client.api.response.{ApiClientGameResponse, ApiClientGamesResponse}
import com.squareup.okhttp._
import org.json4s.jackson.Serialization.write
import org.json4s.{DefaultFormats, Formats}



class HttpClient(url: String) {

  val  jsonType : MediaType = MediaType.parse("application/json; charset=utf-8")

  implicit val formats: Formats = DefaultFormats


  val basePath = "/minesweeper/game"
  val defaultEndpoint = s"${url}${basePath}"

  val client = new OkHttpClient()

  def retrieveAllGames(): ApiClientGamesResponse = {
    val request = createRequest(defaultEndpoint,"GET")
    val response = executeRequest(request)
    parseGamesResponse(response)
  }

  def retrieveGamesOfUser(user: String): ApiClientGamesResponse = {
    val request = createRequest(s"${defaultEndpoint}?user=${user}","GET")
    val response = executeRequest(request)
    parseGamesResponse(response)
  }

  def create(rows: Int, cols: Int, mines: Int, user: String): ApiClientGameResponse = {
    val gameRequest = write(GameRequest(user,rows,cols,mines))
    val requestBody = RequestBody.create(jsonType,gameRequest)
    val request = createRequest(defaultEndpoint,"POST",requestBody)
    val response = executeRequest(request)
    parseGameResponse(response)
  }

  def retrieve(id: Int): ApiClientGameResponse = {
    val request = createRequest(s"${defaultEndpoint}/${id}","GET")
    val response = executeRequest(request)
    parseGameResponse(response)
  }

  def reveal(id: Int, row: Int, col: Int): ApiClientGameResponse = {
    val action = write(ActionRequest("REVEAL",row,col))
    val requestBody = RequestBody.create(jsonType,action)
    val request = createRequest(s"${defaultEndpoint}/${id}", "PUT", requestBody)
    val response = executeRequest(request)
    parseGameResponse(response)
  }

  def flag(id: Int, row: Int, col: Int): ApiClientGameResponse = {
    val action = write(ActionRequest("FLAG",row,col))
    val requestBody = RequestBody.create(jsonType,action)
    val request = createRequest(s"${defaultEndpoint}/${id}", "PUT", requestBody)
    val response = executeRequest(request)
    parseGameResponse(response)
  }

  def pauseGame(id: Int): ApiClientGameResponse = {
    val request = createRequest(s"${defaultEndpoint}/${id}", "PATCH", RequestBody.create(jsonType,""))
    val response = executeRequest(request)
    parseGameResponse(response)
  }

  def parseGamesResponse(response: Response): ApiClientGamesResponse = {
    val jsonResponse = response.body().string()
    response.code() match {
      case 200 => api.response.ApiClientGamesResponse(parseGames(jsonResponse),None)
      case _ =>
        api.response.ApiClientGamesResponse(None,parseError(jsonResponse))
    }
  }

  def parseGameResponse(response: Response): ApiClientGameResponse = {
    val jsonResponse = response.body().string()
    response.code() match {
      case 200 => ApiClientGameResponse(parseGame(jsonResponse),None)
      case _ => api.response.ApiClientGameResponse(None,parseError(jsonResponse))
    }
  }

  private def executeRequest(request: Request): Response = {
    client.newCall(request).execute()
  }

  def createRequest(url: String, method: String, requestBody: RequestBody = null): Request ={
    new Request.Builder()
      .url(url)
      .method(method, requestBody)
      .build()
  }
}
