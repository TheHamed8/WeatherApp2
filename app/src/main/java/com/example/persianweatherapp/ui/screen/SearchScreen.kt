package com.example.persianweatherapp.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.persianweatherapp.data.model.Favorite
import com.example.persianweatherapp.navigation.Screen
import com.example.persianweatherapp.ui.theme.cardBg
import com.example.persianweatherapp.ui.theme.mainBg
import com.example.persianweatherapp.viewModel.FavoriteViewModel
import com.example.persianweatherapp.viewModel.WeatherViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
) {
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(mainBg)
            .padding(top = 12.dp)
    ) {
        SearchBar(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) { mCity ->
            navController.navigate(Screen.Main.route + "/${mCity}")
            favoriteViewModel
                .insertFavorite(
                    Favorite(
                        city = mCity.replaceFirstChar {
                            if (it.isLowerCase()) it.uppercase() else it.toString()
                        } // city name
                    )
                )

        }

    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter a city name.",
            style = MaterialTheme.typography.titleLarge,
            color = Color.LightGray
        )

    }

}


@ExperimentalComposeUiApi
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    onSearch: (String) -> Unit = {}
) {
    val searchQueryState = rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(searchQueryState.value) {
        searchQueryState.value.trim().isNotEmpty() && searchQueryState.value.length >= 3
    }
    val ctx = LocalContext.current
    Column {
        CommonTextField(
            value = searchQueryState,
            text = "City",
            navController = navController,
            onAction = KeyboardActions {
                if (!valid) {
                    keyboardController?.hide()
                    Toast.makeText(ctx, "City name is wrong!", Toast.LENGTH_SHORT).show()
                    return@KeyboardActions
                } else {
                    onSearch(searchQueryState.value.trim())
                    searchQueryState.value = ""
                    keyboardController?.hide()
                }
            })

    }

}


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("RememberReturnType")
@Composable
fun CommonTextField(
    value: MutableState<String>,
    text: String,
    navController: NavController,
    maxLine: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
) {

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    OutlinedTextField(
        value = value.value,
        onValueChange = { value.value = it },
        placeholder = {
            androidx.compose.material3.Text(
                text = text,
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        leadingIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "",
                    tint = Color.LightGray
                )
            }
        },
        maxLines = maxLine,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.LightGray,
            unfocusedTextColor = Color.Gray,
            cursorColor = mainBg,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            focusedContainerColor = cardBg,
            unfocusedContainerColor = cardBg
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .bringIntoViewRequester(bringIntoViewRequester)
            .onFocusEvent { focusState ->
                if (focusState.isFocused) {
                    coroutineScope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            }
            .fillMaxWidth(0.9f)
            .padding(top = 4.dp)
            .height(60.dp)

    )
}