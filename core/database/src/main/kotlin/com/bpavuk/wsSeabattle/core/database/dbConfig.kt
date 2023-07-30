package com.bpavuk.wsSeabattle.core.database

import org.jetbrains.exposed.sql.Database

object SeabattleDatabase {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
}
