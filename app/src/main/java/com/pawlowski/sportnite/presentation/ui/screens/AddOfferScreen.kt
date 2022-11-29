package com.pawlowski.sportnite.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pawlowski.sportnite.R
import com.pawlowski.sportnite.presentation.ui.reusable_components.DateInputField
import com.pawlowski.sportnite.presentation.ui.reusable_components.SportInputField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOfferScreen(
    onNavigateBack: () -> Unit = {}
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart), 
                    onClick = { onNavigateBack() }
                ) {
                    Icon(painter = painterResource(id = R.drawable.back_icon), contentDescription = "")
                }
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Dodawanie oferty spotkania"
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            DateInputField(
                modifier = Modifier.padding(horizontal = 15.dp),
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_icon),
                        contentDescription = ""
                    )
                },
                dateText = "Kliknij aby wybrać czas spotkania"
            )
            Spacer(modifier = Modifier.height(15.dp))

            SportInputField(
                modifier = Modifier.padding(horizontal = 15.dp),
                chosenSport = null,
                onClick = {
                    //TODO
                }
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                value = "",
                onValueChange = {

                },
                label = {
                    Text(text = "Miasto")
                },
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.place_icon), contentDescription = "")
                }
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                value = "",
                onValueChange = {

                },
                label = {
                    Text(text = "Miejsce lub adres")
                },
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.place_icon), contentDescription = "")
                }
            )

            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                value = "",
                onValueChange = {

                },
                label = {
                    Text(text = "Dodatkowe informacje")
                },
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.notes_icon), contentDescription = "")
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Dodaj ofertę")
            }

        }
    }
}



@Preview(showBackground = true)
@Composable
fun AddOfferScreenPreview() {
    AddOfferScreen()
}