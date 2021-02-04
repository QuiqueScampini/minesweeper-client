package com.minesweeper.client.api.response

import CellContent.CellContent

case class CellResponse(content: CellContent, value: Int)

