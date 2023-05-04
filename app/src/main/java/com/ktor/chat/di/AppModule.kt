package com.ktor.chat.di

import com.ktor.chat.data.remote.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient() : HttpClient{
        return HttpClient(CIO){
            install(Logging)
            install(WebSockets)
            install(JsonFeature){
                this.serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Provides
    @Singleton
    fun providesMessageService(client: HttpClient): MessageService{
        return MessageServiceImpl(client = client)
    }

    @Provides
    @Singleton
    fun providesChatSocketService(client: HttpClient): ChatSocketSession{
        return ChatSocketServiceImpl(client = client)
    }

    @Provides
    @Singleton
    fun providesP2pSocketService(client: HttpClient): P2PSession{
        return P2PServiceImpl(client = client)
    }

    @Provides
    @Singleton
    fun providesP2PMessageService(client: HttpClient): P2PMesService{
        return P2PMesServiceImpl(client = client)
    }

    @Provides
    @Singleton
    fun providesUserInfo(client: HttpClient): ListOfUsers{
        return GetUsersImpl(client = client)
    }


}