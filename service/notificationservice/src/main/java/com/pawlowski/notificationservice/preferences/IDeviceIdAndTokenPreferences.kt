package com.pawlowski.notificationservice.preferences

internal interface IDeviceIdAndTokenPreferences {
    fun updateDeviceIdAndToken(calculateNewValue: (DeviceIdAndToken) -> DeviceIdAndToken)
    fun getDeviceIdAndToken(): DeviceIdAndToken
}