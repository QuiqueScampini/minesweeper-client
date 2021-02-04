package com.minesweeper.client.api.request

import com.minesweeper.api.request.Action.Action

case class ActionRequest(action:String, row: Int, col: Int)
