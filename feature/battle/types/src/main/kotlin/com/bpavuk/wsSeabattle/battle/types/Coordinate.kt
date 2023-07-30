package com.bpavuk.wsSeabattle.battle.types

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
