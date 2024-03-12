package com.example.a653380011_7_massage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a653380011_7_massage.ui.theme._653380011_7_massageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _653380011_7_massageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ComposeAllNavigation()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    _653380011_7_massageTheme {
        Greeting("Android")
    }
}

sealed class ScreenRoute(val route: String){
    object First : ScreenRoute("first_screen")
    object Second : ScreenRoute("second_screen")
}

@Composable
fun ComposeAllNavigation() {
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(20.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NavHost(navController = navController,
            startDestination = ScreenRoute.First.route){
            composable(ScreenRoute.First.route){
                MyPage1(navController)
            }
            composable(ScreenRoute.Second.route){
                MyPage2(navController)
            }
        }
    }
}

@Composable
fun MyPage1(navHostController: NavHostController) {
    var name by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var time by rememberSaveable { mutableStateOf("") }
    var selectedMassageType by remember { mutableStateOf("") }
    var selectedRoomType by remember { mutableStateOf("") }

//    var context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = "Lab Midterm Exam",
            fontSize = 25.sp
        )

        Text(
            modifier = Modifier.padding(10.dp),
            text = "Massage & Spa Shop",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )

        Content(
            name = name,
            onNameChange = { name = it },
            date = date,
            onDateChange = { date = it },
        )

        RadioGroup {  room ->
            selectedRoomType = room
        }
        selectedMassageType = MassageTypeDropdown()

        TimeContent(
            time = time,
            onTimeChange = { time = it }
        )

        Spacer(modifier = Modifier.height(height = 8.dp))

        Button(
            onClick = {
                navHostController.currentBackStackEntry?.savedStateHandle?.set(
                    "data",
                   Booking( name,date, selectedRoomType, time.toInt() ,selectedMassageType)
                )
                navHostController.navigate(ScreenRoute.Second.route)
            }
        ) {
            Text(text = "Submit",
                fontSize = 18.sp)
        }

    }
}


@Composable
fun MyPage2(navHostController: NavHostController) {
    val data = navHostController.previousBackStackEntry?.savedStateHandle?.get<Booking>("data") ?:
    Booking( "","","",0 ,"")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = "Lab Midterm Exam",
            fontSize = 25.sp
        )
        Row{
            IconButton(
                modifier = Modifier.size(100.dp),
                onClick = {
                    navHostController.navigateUp()
                }
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "", tint = Color.Magenta)
            }
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Check Information",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start) {



            Text(
                modifier = Modifier.padding(16.dp),
                text = "Customer Name: ${data.name} \n\nBooking Date: ${data.date} \n\nRoom Type: ${data.room_type}  \n\nMassage Type: ${data. massage_type}\n" +
                        "\n" +
                        "Massage Time: ${data.time} \n" +
                        "\nPrice:\n",
                fontSize = 20.sp
            )

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    navHostController.navigate("first") {
                        launchSingleTop = true
                        popUpTo("first") { inclusive = false }
                    }
                }
            ) {
                Text(text = "Close",
                    fontSize = 18.sp)
            }
        }

    }

}

@Composable
fun Content(name: String, onNameChange: (String) -> Unit,
                  date: String, onDateChange:(String) -> Unit){
    Column(modifier = Modifier.padding(horizontal = 16.dp)){
        OutlinedTextField(
            modifier = Modifier.width(400.dp),
            value = name,
            onValueChange = onNameChange,
            label = { Text("Customer Name") })

        OutlinedTextField(
            modifier = Modifier.width(400.dp),
            value = date,
            onValueChange = onDateChange,
            label = { Text("Booking Date") })
    }


}

@Composable
fun TimeContent(time: String, onTimeChange: (String)->Unit){
    Column(modifier = Modifier.padding(horizontal = 5.dp,)) {
        OutlinedTextField(modifier = Modifier
            .width(400.dp)
            .padding(bottom = 16.dp),
            value = time,
            onValueChange = onTimeChange,
            label = { Text("Massage Time")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number)
        )

    }
}


@Composable
fun MyRadioGroup(mItems: List<String>, selected: String, setSelected: ( selected: String)->Unit,){
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        mItems.forEach{
                item ->
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == item ,
                    onClick = {
                        setSelected(item) },
                    enabled = true,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Magenta)
                )
                Text(
                    text = item,
                    modifier = Modifier.padding(start = 5.dp))
            }
        }
    }
}


@Composable
fun RadioGroup(onRoomTypeSelected: (String) -> Unit){
    val roomType = listOf("Private Room", "Normal Room")
    var selectedRoomType by remember { mutableStateOf("") }

    Column {
        Text(
            text = "Room Type: $selectedRoomType",
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 10.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            MyRadioGroup(
                mItems = roomType,
                selected = selectedRoomType,
                setSelected = { room ->
                    selectedRoomType = room
                    onRoomTypeSelected(room)
                }
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MassageTypeDropdown(): String {
    val keyboardController = LocalSoftwareKeyboardController.current

    val subjectsList = listOf(
        "Select Massage Type",
        "Thai massage",
        "Foot massage",
        "Aromatherapy massage"
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedMassageType by remember { mutableStateOf(subjectsList[0]) }

    ExposedDropdownMenuBox(
        modifier = Modifier.clickable { keyboardController?.hide() },
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .width(340.dp)
                .menuAnchor()
                .clickable { keyboardController?.hide() },
            textStyle = TextStyle.Default.copy(fontSize = 12.sp),
            readOnly = true,
            value = selectedMassageType,
            onValueChange = {},
            label = { Text(text = "Subjects") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
            colors = ExposedDropdownMenuDefaults.textFieldColors(),


            )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        )
        {
            subjectsList.forEach { selectionOption ->
                DropdownMenuItem(
                    text={Text(selectionOption)},
                    onClick = {
                        selectedMassageType = selectionOption
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }

    return selectedMassageType
}
