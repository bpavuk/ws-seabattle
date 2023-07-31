package com.bpavuk.wsSeabattle

import com.bpavuk.wsSeabattle.battle.database.integration.DatabaseCreateRoomStorage
import com.bpavuk.wsSeabattle.battle.database.integration.DatabaseGetRoomStorage
import com.bpavuk.wsSeabattle.battle.database.integration.DatabaseJoinRoomStorage
import com.bpavuk.wsSeabattle.battle.endpoints.create.CreateRoomPlugin
import com.bpavuk.wsSeabattle.battle.endpoints.join.JoinRoomPlugin
import com.bpavuk.wsSeabattle.battle.endpoints.whereami.WhereAmIPlugin
import com.bpavuk.wsSeabattle.battle.usecase.CreateRoomUsecase
import com.bpavuk.wsSeabattle.battle.usecase.GetRoomUsecase
import com.bpavuk.wsSeabattle.battle.usecase.JoinRoomUsecase
import com.bpavuk.wsSeabattle.battle.usecase.integration.UsecaseCreateRoomRepository
import com.bpavuk.wsSeabattle.battle.usecase.integration.UsecaseGetRoomRepository
import com.bpavuk.wsSeabattle.battle.usecase.integration.UsecaseJoinRoomRepository
import com.bpavuk.wsSeabattle.chat.usecase.ChatUsecase
import com.bpavuk.wsSeabattle.chat.usecase.integration.UsecaseChatRepository
import com.bpavuk.wsSeabattle.core.endpoints.ConnectionContainer
import com.bpavuk.wsSeabattle.core.endpoints.PluginRegistry
import com.bpavuk.wsSeabattle.frontend.FrontendDependencies
import com.bpavuk.wsSeabattle.frontend.launchFrontend
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

    val getRoomUsecase = GetRoomUsecase(
        storage = DatabaseGetRoomStorage()
    )

    val connectionContainer = ConnectionContainer
    val pluginRegistry = PluginRegistry

    routing {
        launchFrontend(
            FrontendDependencies(
                createRoomPlugin = CreateRoomPlugin(
                    UsecaseCreateRoomRepository(
                        usecase = CreateRoomUsecase(
                            storage = DatabaseCreateRoomStorage()
                        )
                    )
                ),
                joinRoomPlugin = JoinRoomPlugin(
                    UsecaseJoinRoomRepository(
                        usecase = JoinRoomUsecase(
                            storage = DatabaseJoinRoomStorage()
                        )
                    )
                ),
                whereAmIPlugin = WhereAmIPlugin(
                    UsecaseGetRoomRepository(
                        usecase = getRoomUsecase
                    )
                ),
                chatRepository = UsecaseChatRepository(
                    usecase = ChatUsecase(
                        getRoomUsecase = getRoomUsecase,
                        connectionContainer = connectionContainer
                    )
                ),
                connectionContainer = connectionContainer,
                pluginRegistry = pluginRegistry
            ),
        )
    }
}
