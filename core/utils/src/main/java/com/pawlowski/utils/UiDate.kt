package com.pawlowski.utils

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class UiDate(val offsetDateTimeDate: OffsetDateTime) {

    companion object {
        private fun dateTimeFormatter(): DateTimeFormatter
        {
            return DateTimeFormatter.ofPattern("dd.MM.u HH:mm")
        }

        private fun timeFormatter(): DateTimeFormatter
        {
            return DateTimeFormatter.ofPattern("HH:mm")
        }

        private fun dateFormatter(): DateTimeFormatter
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