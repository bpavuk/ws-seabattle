package com.bpavuk.wsSeabattle.chat.usecase.integration

import com.bpavuk.wsSeabattle.chat.endpoints.ChatRepository
import com.bpavuk.wsSeabattle.chat.endpoints.ChatResponse
import com.bpavuk.wsSeabattle.chat.usecase.ChatUsecase

class UsecaseChatRepository(val usecase: ChatUsecase): ChatRepository {
    override suspend fun sendTheMessage(userId: Int, message: String): ChatResponse =
        usecase.sendMessage(userId, message).toEndpoint()

    private fun ChatUsecase.Result.toEndpoint(): ChatResponse = when (this) {
        ChatUsecase.Result.MessageSent -> ChatResponse.MessageSent
        ChatUsecase.Result.NotJoinedToAnyRoom -> ChatResponse.NotJoinedToAnyRoom
    }
}