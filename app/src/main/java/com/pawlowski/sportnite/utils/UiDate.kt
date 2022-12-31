package com.pawlowski.sportnite.utils

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class UiDate(val offsetDateTimeDate: OffsetDateTime) {

    companion object {
        fun dateTimeFormatter(): DateTimeFormatter
        {
            return DateTimeFormatter.ofPattern("dd.MM.u HH:mm")
        }

        fun timeFormatter(): DateTimeFormatter
        {
            return DateTimeFormatter.ofPattern("HH:mm")
        }

        fun dateFormatter(): DateTimeFormatter
        {
            return DateTimeFormatter.ofPattern("dd.MM.u")
        }

        fun getZoneOffsetOfDevice(): ZoneOffset {
            return ZoneId
                .systemDefault()
                .rules
                .getOffset(
                    Instant.now()
                )
        }
    }

    fun asLocalDateString(): String
    {
        val newOffsetDateTime = offsetDateTimeDate.withOffsetSameInstant(
            getZoneOffsetOfDevice()
        )
        return newOffsetDateTime.format(dateFormatter())
    }

    fun asLocalDateTimeString(): String {
        val newOffsetDateTime = offsetDateTimeDate.withOffsetSameInstant(
            getZoneOffsetOfDevice()
        )
        return newOffsetDateTime.format(dateTimeFormatter())
    }


}