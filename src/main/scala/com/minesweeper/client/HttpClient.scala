package com.minesweeper.client

import com.minesweeper.api.request.GameRequest
import com.minesweeper.api.response.{ApiClientGameResponse, ApiClientGamesResponse}
import com.minesweeper.client.JsonParser.parseError
import com.squareup.okhttp._
import org.json4s.jackson.Serialization.write



class HttpClient(url: String) {

  val  jsonType : MediaType = MediaType.parse("application/json; charset=utf-8");

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
    val requestBody = gameActionBody("REVEAL", col, row)
    val request = createRequest(defaultEndpoint, "PUT", requestBody)
    val response = executeRequest(request)
    parseGameResponse(response)
  }

  def flag(id: Int, row: Int, col: Int): ApiClientGameResponse = {
    val requestBody = gameActionBody("FLAG", col, row)
    val request = createRequest(defaultEndpoint, "PUT", requestBody)
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
      case 200 => ApiClientGamesResponse(None,None)
      case _ =>
        ApiClientGamesResponse(None,parseError(jsonResponse))
    }
  }

  def parseGameResponse(response: Response): ApiClientGameResponse = {
    val jsonResponse = response.body().string()
    response.code() match {
      case 200 => ApiClientGameResponse(None,None)
      case _ => ApiClientGameResponse(None,parseError(jsonResponse))
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

  def gameRequestBody(rows: Int, cols: Int, mines: Int, user: String): RequestBody =
    RequestBody.create(jsonType,
      s"""{\"cols\":${cols},\"mines\": ${mines},\"rows\": ${rows},\"user\": \"${user}\"}""")

  def gameActionBody(action: String, col: Int, row: Int): RequestBody =
    RequestBody.create(jsonType, s"""{\"action\":\"${action}\",\"col\": ${col},\"row\": \"${row}\"}""")

}
