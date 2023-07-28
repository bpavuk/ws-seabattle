package com.bpavuk.wsSeabattle.battle.endpoints

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

fun Route.battleRouting() {
    webSocket("/battle/session") {
        for (frame in incoming) {
            if (frame is Frame.Text) {
//                val text = frame.readText()
//                outgoing.send(Frame.Text("YOU SAID: $text"))
//                if (text.equals("bye", ignoreCase = true)) {
//                    close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
//                }
                when (val message = frame.readText()) {
                    ":q" -> {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Player gone"))
                    }
                    else -> {
                        val coordinate = message.toCoordinate()
                        if (coordinate == null) {
                            outgoing.send(Frame.Text("Invalid coordinate"))
                        } else {
                            table[coordinate.x][coordinate.y] = 1
                            outgoing.send(Frame.Text(table.prettify()))
                        }
                    }
                }
            }
        }
    }
}

val table = List(9) {
    MutableList(9) {
        0
    }
}

fun List<List<Int>>.prettify(): String {
    val returnable = StringBuilder()
    for (i in this) {
        for (j in i) {
            returnable.append("$j ")
        }
        returnable.append("\n")
    }
    return returnable.toString()
}

data class Coordinate(val x: Int, val y: Int)

fun String.toCoordinate(): Coordinate? {
    require(length == 2) { return null }
    val coordinateList = mutableListOf<Int>()
    for (i in this) {
        when (i) {
            in '1'..'9' -> {
                coordinateList.add(i.toString().toInt() - 1)
            }
            in 'A'..'I' -> {
                coordinateList.add(i.uppercaseChar().code - 'A'.code)
            }
            else -> {
                return null
            }
        }
    }
    return Coordinate(coordinateList[0], coordinateList[1])
}
