package com.minesweeper.client.api.response

import org.joda.time.LocalDateTime

case class ErrorResponse (message: String, status: String, timestamp: LocalDateTime)

