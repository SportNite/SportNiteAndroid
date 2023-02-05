package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pawlowski.models.mappers.availableSports
import com.pawlowski.models.Sport

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportPickerDialog(
    showDialog: () -> Boolean,
    availableSports: List<Sport>,
    onSportChosen: (Sport) -> Unit,
    onDismissDialog: () -> Unit
) {
    if (showDialog()) {
        Dialog(
            onDismissRequest = { onDismissDialog() },
            properties = DialogProperties()
        ) {
            val selectedSport = remember {
                mutableStateOf<Sport?>(null)
            }
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Card(modifier = Modifier.fillMaxWidth(0.95f)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Wybierz sport:", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn {
                            items(availableSports) {
                                ListItem(
                                    headlineText = {
                                        Text(text = it.sportName.asString())
                                    },
                                    leadingContent = {
                                        RadioButton(
                                            selected = it == selectedSport.value,
                                            onClick = { selectedSport.value = it })
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Row {
                            Button(onClick = { onDismissDialog() }) {
                                Text(text = "Anuluj")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(onClick = {
                                selectedSport.value?.let {
                                    onSportChosen(it)
                                    onDismissDialog()
                                }
                            }, enabled = selectedSport.value != null) {
                                Text(text = "Wybierz")
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))

                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SportPickerDialogPreview() {
    SportPickerDialog(
        showDialog = { true },
        availableSports = availableSports.values.toList(),
        onSportChosen = {},
        onDismissDialog = {}
    )
}