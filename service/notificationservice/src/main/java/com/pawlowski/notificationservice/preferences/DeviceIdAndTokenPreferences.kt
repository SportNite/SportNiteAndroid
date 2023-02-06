package com.pawlowski.notificationservice.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DeviceIdAndTokenPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences,
): IDeviceIdAndTokenPreferences {
    override fun updateDeviceIdAndToken(calculateNewValue: (DeviceIdAndToken) -> DeviceIdAndToken) {
        val lastValue = getDeviceIdAndToken()
        val newValue = calculateNewValue(lastValue)
        sharedPreferences.edit().apply {
            putString(NOTIFICATION_TOKEN_KEY, newValue.token)
            putString(DEVICE_ID_KEY, newValue.deviceId)
        }.apply()
    }

    override fun getDeviceIdAndToken(): DeviceIdAndToken {
        return DeviceIdAndToken(
            token = sharedPreferences.getString(NOTIFICATION_TOKEN_KEY, null),
            deviceId = sharedPreferences.getString(DEVICE_ID_KEY, null)
        )
    }

    companion object {
        const val DEVICE_ID_KEY = "deviceId"
        const val NOTIFICATION_TOKEN_KEY = "notificationToken"
    }
}