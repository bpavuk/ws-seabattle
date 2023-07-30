package com.bpavuk.wsSeabattle.chat.endpoints

interface ChatRepository {
    suspend fun sendTheMessage(userId: Int, message: String): ChatResponse
}

sealed interface ChatResponse {
    data object MessageSent: ChatResponse

    data object NotJoinedToAnyRoom: ChatResponse
}
