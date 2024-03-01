package com.example.persianweatherapp.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.persianweatherapp.ui.theme.cardBg
import com.example.persianweatherapp.ui.theme.mainBg
import com.example.persianweatherapp.util.Constants.USER_UNIT
import com.example.persianweatherapp.viewModel.DatastoreViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingScreen(
    navController: NavController,
    datastoreViewModel: DatastoreViewModel = hiltViewModel()
) {

    var unitToggleState by rememberSaveable { mutableStateOf(false) }
    val defaultChoice = USER_UNIT.ifBlank { "metric" }

    var choiceState by remember {
        mutableStateOf(defaultChoice)
    }

    Scaffold(topBar = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(mainBg)
                .padding(start = 10.dp, top = 12.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Weather setting",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }


    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = mainBg
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Change Units of Measurement",
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.White
                )

                IconToggleButton(
                    checked = unitToggleState,
                    onCheckedChange = {
                        unitToggleState = !unitToggleState
                        choiceState = if (unitToggleState) {
                            "Imperial (F)"
                        } else {
                            "Metric (C)"
                        }
                        Log.d("TAG", "MainContent: $unitToggleState")

                    }, modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(bottom = 12.dp)
                        .background(cardBg)
                ) {

                    Text(
                        text = if (unitToggleState) "Fahrenheit ºF" else "Celsius ºC",
                        color = Color.White
                    )


                }
                Button(
                    onClick = {
                        USER_UNIT = choiceState.split(" ")[0].lowercase()
                        datastoreViewModel.saveUserUnit(USER_UNIT)
                    },
                    modifier = Modifier
                        .padding(3.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = cardBg,
                        contentColor = Color.White
                    )
                ) {
                    androidx.compose.material.Text(
                        text = "Save",
                        modifier = Modifier
                            .padding(4.dp),
                        color = Color.White,
                        fontSize = 17.sp
                    )

                }


            }
        }


    }

}