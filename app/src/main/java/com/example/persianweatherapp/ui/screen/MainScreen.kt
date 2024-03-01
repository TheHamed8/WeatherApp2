package com.example.persianweatherapp.ui.screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.persianweatherapp.R
import com.example.persianweatherapp.data.model.City
import com.example.persianweatherapp.data.model.Favorite
import com.example.persianweatherapp.data.model.WeatherItem
import com.example.persianweatherapp.data.remote.NetworkResult
import com.example.persianweatherapp.navigation.Screen
import com.example.persianweatherapp.ui.theme.cardBg
import com.example.persianweatherapp.ui.theme.lightBlue
import com.example.persianweatherapp.ui.theme.mainBg
import com.example.persianweatherapp.util.Constants.USER_CITY
import com.example.persianweatherapp.util.Constants.USER_UNIT
import com.example.persianweatherapp.util.digitBySeparator
import com.example.persianweatherapp.util.formatDate
import com.example.persianweatherapp.util.formatDateTime
import com.example.persianweatherapp.util.formatDecimals
import com.example.persianweatherapp.viewModel.DatastoreViewModel
import com.example.persianweatherapp.viewModel.FavoriteViewModel
import com.example.persianweatherapp.viewModel.WeatherViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    datastoreViewModel: DatastoreViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    navController: NavController,
    city: String?
) {

    val weatherData by viewModel.data.collectAsState()

    var unit by remember {
        mutableStateOf("metric")
    }
    val curCity: String = if (city!!.isBlank()) "Tehran" else city

    USER_CITY = city.ifBlank { "Tehran" }

    var isImperial by remember {
        mutableStateOf(false)
    }

    if (USER_UNIT.isNotEmpty()) {
        unit = USER_UNIT
        isImperial = unit == "imperial"
    }

    var cityData by remember {
        mutableStateOf(City())
    }

    var cntData by remember {
        mutableStateOf(0)
    }
    var message by remember {
        mutableStateOf(0.0)
    }

    var dataList by remember {
        mutableStateOf<List<WeatherItem>>(emptyList())
    }

    var codData by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        viewModel.getWeather(
            units = unit,
            city = curCity
        )
    }

    when (weatherData) {
        is NetworkResult.Success -> {
            cityData = (weatherData.data?.city ?: "") as City
            cntData = weatherData.data?.cnt ?: 0
            codData = weatherData.data?.cod ?: ""
            dataList = weatherData.data?.list ?: emptyList()
            message = weatherData.data?.message ?: 0.0
            datastoreViewModel.saveUserCity(curCity)
            loading = false
        }

        is NetworkResult.Error -> {
            loading = false
            Log.e("1212", "Weather api call had a issue: ${weatherData.message}")
        }

        is NetworkResult.Loading -> {
            loading = true
        }
    }


    if (loading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(mainBg),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.LightGray
            )
        }
    } else {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val list = favoriteViewModel.favList.collectAsState().value.filter {
            it.city != cityData.name
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(drawerContainerColor = cardBg) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        IconButton(
                            onClick = { navController.navigate(Screen.Setting.route) },
                            modifier = Modifier
                                .padding(top = 4.dp, end = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "",
                                tint = Color.Gray,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Current Location",
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(start = 4.dp, bottom = 6.dp)
                                .fillMaxWidth()
                                .align(Alignment.Start)
                        )
                        CityRow(
                            favorite = Favorite(
                                city = cityData.name ?: curCity,
                                country = cityData.country ?: curCity
                            ),
                            imageUrl = dataList[0].weather[0].icon,
                            tempDay = dataList[0].temp.day,
                            iconVisible = false,
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DotLine()
                    }

                    Surface(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth(),
                        color = cardBg
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Other Locations",
                                color = Color.LightGray,
                                modifier = Modifier
                                    .padding(start = 4.dp, bottom = 6.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                            LazyColumn {
                                items(items = list, key = { it.city }) {
                                    CityRow(
                                        favorite = it,
                                        imageUrl = dataList[0].weather[0].icon,
                                        tempDay = dataList[0].temp.day
                                    ) {
                                        navController.navigate(Screen.Main.route + "/${it.city}")
                                    }
                                }
                                item { Spacer(modifier = Modifier.height(4.dp)) }
                            }
                        }

                    }

                }
            },
            content = {
                val refreshScope = rememberCoroutineScope()
                val pullRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = {
                    refreshScope.launch {
                        refreshDataFromServer(viewModel = viewModel, city = curCity, unit = unit)
                        Log.e("1212", "swipe Refresh")
                    }
                })
                Box(
                    Modifier
                        .pullRefresh(pullRefreshState)
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(mainBg),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            MainTopBar(
                                navController = navController,
                                onClick = { scope.launch { drawerState.open() } }
                            )
                        }
                        item {
                            CityDetail(
                                city = cityData.name ?: "Null",
                                countryCode = cityData.country ?: "Null",
                                date = dataList[0].dt,
                                tempDay = dataList[0].temp.day,
                                tempMin = dataList[0].temp.min,
                                tempMax = dataList[0].temp.max,
                                weatherState = dataList[0].weather[0].description,
                                icon = dataList[0].weather[0].icon,
                                population = cityData.population ?: 0,
                            )
                        }
                        item { Spacer(modifier = Modifier.height(15.dp)) }
                        items(dataList, key = { it.dt }) { WeeklyData(item = it) }
                        item { Spacer(modifier = Modifier.height(15.dp)) }
                        item { DailyData(item = dataList[0], isImperial = isImperial) }
                        item { Spacer(modifier = Modifier.height(15.dp)) }
                    }
                    PullRefreshIndicator(
                        refreshing = false,
                        state = pullRefreshState,
                        Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        )


    }

}

@Composable
private fun DotLine() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Canvas(
        Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(0.95f)
            .height(1.dp)
    ) {

        drawLine(
            color = Color.Gray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}

private suspend fun refreshDataFromServer(
    viewModel: WeatherViewModel,
    city: String,
    unit: String
) {
    viewModel.getWeather(
        city = city,
        units = unit
    )
}

@Composable
private fun MainTopBar(
    navController: NavController,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = R.drawable.weather,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.25f)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {
                    navController.navigate(Screen.Search.route)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "",
                        tint = Color.White
                    )
                }

            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.90f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Weather",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                        .copy(
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Light
                        ),
                )
            }
        }


    }
}

@Composable
private fun CityDetail(
    city: String,
    countryCode: String,
    date: Int,
    population: Int,
    tempDay: Double,
    tempMin: Double,
    tempMax: Double,
    weatherState: String,
    icon: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(.9f)
            .height(240.dp),
        shape = RoundedCornerShape(corner = CornerSize(25f)),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .fillMaxHeight()
                .padding(start = 12.dp, top = 8.dp)
        ) {
            Text(
                text = "$city, $countryCode",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge
                    .copy(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Normal
                    ),
            )
            Text(
                text = " ${formatDate(date)}",
                color = Color.LightGray,
                style = MaterialTheme.typography.headlineLarge
                    .copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${icon}.png",
                    contentDescription = "",
                    modifier = Modifier.size(90.dp)
                )
                Text(
                    text = "${formatDecimals(tempDay)}º",
                    color = Color.White,
                    fontSize = 52.sp,
                    modifier = Modifier
                        .height(80.dp)
                )
            }
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(end = 8.dp)) {
                Text(text = weatherState, color = Color.LightGray)
                Text(
                    text = "${formatDecimals(tempMax)}º/${formatDecimals(tempMin)}º",
                    color = Color.LightGray
                )
                Text(text = "Feels like ${formatDecimals(tempDay - 2)}º", color = Color.LightGray)
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
                .fillMaxHeight()
                .padding(start = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(text = "Population: ${digitBySeparator(population)}", color = Color.LightGray)
        }


    }
}

@Composable
private fun WeeklyData(
    item: WeatherItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(.9f)
            .wrapContentHeight()
            .padding(vertical = 3.dp),
        shape = RoundedCornerShape(corner = CornerSize(25f)),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                formatDate(item.dt)
                    .split(",")[0],
                modifier = Modifier
                    .padding(start = 5.dp)
                    .weight(0.25f),
                color = Color.LightGray
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.25f)
            ) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = "",
                    tint = lightBlue,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "${item.humidity}%",
                    color = Color.LightGray,
                    fontWeight = FontWeight.Light
                )
            }

            AsyncImage(
                model = "https://openweathermap.org/img/wn/${item.weather[0].icon}.png",
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .weight(0.25f),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "${formatDecimals(item.temp.max)}º/${formatDecimals(item.temp.min)}º",
                modifier = Modifier.weight(0.25f),
                color = Color.LightGray
            )

        }

    }

}

@Composable
private fun DailyData(
    item: WeatherItem,
    isImperial: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(.9f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(corner = CornerSize(25f)),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.5f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sunrise),
                        contentDescription = "",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Sunrise", color = Color.LightGray, fontSize = 18.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        text = formatDateTime(item.sunrise),
                        color = Color.LightGray,
                        fontSize = 18.sp
                    )
                }


            }
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 2.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.5f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cloud),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Sunset", color = Color.LightGray, fontSize = 18.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        text = formatDateTime(item.sunset),
                        color = Color.LightGray,
                        fontSize = 18.sp
                    )
                }
            }
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 2.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.wind),
                        contentDescription = "",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Wind", color = Color.LightGray, fontSize = 18.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        text = "${item.speed.roundToInt()} ${if (isImperial) "mph" else "m/s"}",
                        color = Color.LightGray,
                        fontSize = 18.sp
                    )
                }
            }
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 2.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.humidity),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Humidity", color = Color.LightGray, fontSize = 18.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(text = "${item.humidity}%", color = Color.LightGray, fontSize = 18.sp)
                }
            }
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 2.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pressure),
                        contentDescription = "",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Pressure", color = Color.LightGray, fontSize = 18.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(text = "${item.pressure}", color = Color.LightGray, fontSize = 18.sp)
                }
            }

        }

    }

}

@Composable
fun CityRow(
    favorite: Favorite,
    imageUrl: String,
    tempDay: Double,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    iconVisible: Boolean = true,
    navController: () -> Unit = {}
) {
    Surface(
        Modifier
            .padding(vertical = 4.dp, horizontal = 2.dp)
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        color = cardBg
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    navController()
                }
                .padding(horizontal = 6.dp)
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(0.4f)
            ) {
                Text(
                    text = favorite.city, modifier = Modifier
                        .padding(start = 4.dp).align(Alignment.CenterVertically),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light
                    )
                )
            }

            if (!iconVisible) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.3f)
                ) {
                    AsyncImage(
                        model = "https://openweathermap.org/img/wn/${imageUrl}.png",
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxHeight()
                    )

                    Text(text = "${formatDecimals(tempDay)}º", color = Color.White)
                }
            }
            if (iconVisible) {
                IconButton(onClick = {
                    favoriteViewModel.deleteFavorite(favorite)
                }) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "delete",
                        modifier = Modifier
                            .weight(0.3f)
                            .fillMaxHeight()
                            .align(Alignment.CenterVertically),
                        tint = Color.White
                    )
                }
            }


        }

    }


}