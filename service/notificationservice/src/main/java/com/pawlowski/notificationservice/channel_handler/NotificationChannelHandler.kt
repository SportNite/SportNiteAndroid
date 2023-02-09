package com.pawlowski.notificationservice.channel_handler

import android.app.NotificationChannel
import android.app.NotificationManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationChannelHandler @Inject constructor(
    private val notificationManager: NotificationManager
) : INotificationChannelHandler {
    private val channels by lazy {
        listOf(
            MyNotificationChannel(
                channelId = "NewResponse",
                tittle = "Nowa odpowiedź do oferty",
                description = "Przychodzi powiadomienie gdy ktoś nam wyśle odpowiedź do naszej oferty"
            ),
            MyNotificationChannel(
                channelId = "MyResponseAccepted",
                tittle = "Moja odpowiedź zaakceptowana",
                description = "Przychodzi powiadomienie gdy nasza oferta zostanie zaakceptowana"
            ),
            MyNotificationChannel(
                channelId = "MyResponseRejected",
                tittle = "Moja odpowiedź odrzucona",
                description = "Przychodzi powiadomienie gdy nasza odpowiedź do oferty oferta zostanie odrzucona"
            ),
            MyNotificationChannel(
                channelId = "ResponseCancelled",
                tittle = "Oferta do akceptacji anulowana",
                description = "Przychodzi powiadomienie gdy ktoś anuluje odpowiedź na ofertę do gry skierowaną do nas"
            ),
        )
    }

    override fun innitNotificationChannels() {
        channels.forEach {
            val name = it.tittle
            val descriptionText = it.description
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(it.channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }

}