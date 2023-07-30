package com.bpavuk.wsSeabattle

import com.bpavuk.wsSeabattle.battle.database.integration.DatabaseCreateRoomStorage
import com.bpavuk.wsSeabattle.battle.database.integration.DatabaseGetRoomStorage
import com.bpavuk.wsSeabattle.battle.database.integration.DatabaseJoinRoomStorage
import com.bpavuk.wsSeabattle.battle.endpoints.BattleDependencies
import com.bpavuk.wsSeabattle.battle.endpoints.battleRouting
import com.bpavuk.wsSeabattle.battle.usecase.CreateRoomUsecase
import com.bpavuk.wsSeabattle.battle.usecase.GetRoomUsecase
import com.bpavuk.wsSeabattle.battle.usecase.JoinRoomUsecase
import com.bpavuk.wsSeabattle.battle.usecase.integration.UsecaseCreateRoomRepository
import com.bpavuk.wsSeabattle.battle.usecase.integration.UsecaseGetRoomRepository
import com.bpavuk.wsSeabattle.battle.usecase.integration.UsecaseJoinRoomRepository
import com.bpavuk.wsSeabattle.plugins.configureOpenAPI
import com.bpavuk.wsSeabattle.plugins.configureSerialization
import com.bpavuk.wsSeabattle.plugins.configureSockets
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureOpenAPI()
    configureSerialization()
    configureSockets()

    routing {
        battleRouting(
            BattleDependencies(
                createRoomRepository = UsecaseCreateRoomRepository(
                    usecase = CreateRoomUsecase(
                        storage = DatabaseCreateRoomStorage()
                    )
                ),
                getRoomRepository = UsecaseGetRoomRepository(
                    usecase = GetRoomUsecase(
                        storage = DatabaseGetRoomStorage()
                    )
                ),
                joinRoomRepository = UsecaseJoinRoomRepository(
                    usecase = JoinRoomUsecase(
                        storage = DatabaseJoinRoomStorage()
                    )
                )
            )
        )
    }
}
