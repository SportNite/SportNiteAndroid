package com.pawlowski.sportnite.presentation.ui.utils

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.datetime.dateTimePicker
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*

fun showDateTimePicker(
    context: Context,
    requireFutureDateTime: Boolean = false,
    onDatePicked: (OffsetDateTime) -> Unit
) {
    MaterialDialog(context).show {
        dateTimePicker(show24HoursView = true, requireFutureDateTime = requireFutureDateTime) { _, datetime ->
            val year = datetime.get(Calendar.YEAR)
            val month = datetime.get(Calendar.MONTH)+1
            val day = datetime.get(Calendar.DAY_OF_MONTH)
            val hour = datetime.get(Calendar.HOUR_OF_DAY)
            val minute = datetime.get(Calendar.MINUTE)
            val offsetDateTime = OffsetDateTime.of(year, month, day, hour, minute, 0, 0, ZoneId
                .systemDefault()
                .rules
                .getOffset(
                    Instant.now()
                ))
            onDatePicked(offsetDateTime)
        }
    }
}

fun showDatePicker(
    context: Context,
    requireFutureDate: Boolean = false,
    onDatePicked: (OffsetDateTime) -> Unit
) {
    MaterialDialog(context).show {
        datePicker(requireFutureDate = requireFutureDate) { _, datetime ->
            val year = datetime.get(Calendar.YEAR)
            val month = datetime.get(Calendar.MONTH)+1
            val day = datetime.get(Calendar.DAY_OF_MONTH)
            val offsetDateTime = OffsetDateTime.of(year, month, day, 0, 0, 0, 0, ZoneId
                .systemDefault()
                .rules
                .getOffset(
                    Instant.now()
                ))
            onDatePicked(offsetDateTime)
        }
    }
}