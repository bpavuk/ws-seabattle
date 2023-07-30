package com.bpavuk.wsSeabattle.core.types

class Table {
    fun prettyPrint(): String {
        val returnable = StringBuilder()
        for (i in table) {
            for (j in i) {
                returnable.append("$j ")
            }
            returnable.append("\n")
        }
        return returnable.toString()
    }

    val table = List(9) {
        MutableList(9) {
            0
        }
    }
}
