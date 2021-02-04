package com.minesweeper.client

import com.minesweeper.api.response.{ApiClientGameResponse, ApiClientGamesResponse}

class MinesweeperGameClient(url: String = "http://localhost:8080") {

  val httpClient: HttpClient = new HttpClient(url)

  def retrieveAllGames() : ApiClientGamesResponse =
    httpClient.retrieveAllGames()
  
  def retrieveGamesOfUser(user: String): ApiClientGamesResponse = 
    httpClient.retrieveGamesOfUser(user)
    
  def create(rows: Int, cols: Int, mines: Int, user: String): ApiClientGameResponse =
    httpClient.create(rows, cols, mines, user)
    
  def retrieve(id:Int): ApiClientGameResponse =
    httpClient.retrieve(id)
    
  def reveal( id:Int, row: Int, col: Int): ApiClientGameResponse = 
    httpClient.reveal(id, row, col)
    
  def flag(id:Int, row: Int, col: Int): ApiClientGameResponse = 
    httpClient.flag(id, row, col)
    
  def pause(id: Int): ApiClientGameResponse =
    httpClient.pauseGame(id)

}
