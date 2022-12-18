package com.pawlowski.sportnite.utils

import java.time.OffsetDateTime
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
    }

    fun asLocalDateString(): String
    {
        return offsetDateTimeDate.format(dateFormatter())
    }

    fun asLocalDateTimeString(): String
    {
        return offsetDateTimeDate.format(dateTimeFormatter())
    }


}