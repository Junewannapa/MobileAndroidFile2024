package com.example.ass04

import android.os.Bundle
import android.widget.RadioGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.SetSelectionCommand
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.ass04.ui.theme.ASS04Theme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ASS04Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyScreen()
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
    ASS04Theme {
        MyScreen()
    }
}


@Composable
fun IdNameContent(username: String, onUsernameChange: (String) -> Unit,
                  pwd: String, onPwdChange:(String) -> Unit){
    Column(modifier = Modifier.padding(horizontal = 16.dp)){
        OutlinedTextField(
            modifier = Modifier.width(400.dp),
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text)
        )

        OutlinedTextField(
            modifier = Modifier.width(400.dp),
            value = pwd,
            onValueChange = onPwdChange,
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation()
        )


    }


}

@Composable
fun MyScreen(calendar: Calendar = Calendar.getInstance()){
    var textInformation by rememberSaveable{ mutableStateOf("Register Information: ") }

    var uname by rememberSaveable{ mutableStateOf("") }
    var pwd by rememberSaveable{ mutableStateOf("") }
    var email by rememberSaveable{ mutableStateOf("") }

    var selectedDate by remember { mutableLongStateOf(calendar.timeInMillis) }

    var selectedGender by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier.padding(5.dp),
            text = "Register Form",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp)

        IdNameContent(
            username = uname,
            onUsernameChange = { uname = it },
            pwd = pwd,
            onPwdChange = { pwd = it }
        )
        RadioGroupUsage { gender ->
            selectedGender = gender
        }


        CreditContent(email, onEmailChange={email=it})

        MyDatePicker(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            calendar = calendar
        )


        Spacer(modifier = Modifier.height(height = 8.dp))

        Button(onClick = {
            val formatter = SimpleDateFormat("dd-MMM-yyyy")
            val formattedDate = formatter.format(Date(selectedDate))

            textInformation = "Register Information: \n"+
                    "Name: $uname \n" + "Password: $pwd \n" +
                    "Gender: $selectedGender \n " +
                    "E-mail: $email \n "+
                    "Birthday: $formattedDate \n "



        }) {
            Text(text = "Register")
        }



        Column(
            modifier = Modifier.background(color = Color(133, 252, 191, 255))
                .width(400.dp)
                .wrapContentHeight(unbounded = true)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Text( modifier = Modifier.padding(5.dp),
                text = textInformation, fontSize = 18.sp
            )
        }
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
fun RadioGroupUsage(onGenderSelected: (String) -> Unit){
    val kinds = listOf("Male", "Female", "Other")
    var selectedGender by remember { mutableStateOf("") }

    Column {
        Text(
            text = "Gender : $selectedGender",
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
                mItems = kinds,
                selected = selectedGender,
                setSelected = { gender ->
                    selectedGender = gender
                    onGenderSelected(gender)
                }
            )
        }
    }
}

@Composable
fun CreditContent(email: String, onEmailChange:(String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)){
        OutlinedTextField(
            modifier = Modifier.width(400.dp),
            value = email,
            onValueChange = onEmailChange,
            label = { Text("E-mail") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(selectedDate: Long, onDateSelected: (Long) -> Unit, calendar: Calendar) {
    val calendar = Calendar.getInstance()
    val mYear = calendar.get(Calendar.YEAR)
    val mMonth = calendar.get(Calendar.MONTH)
    val mDay = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.set(mYear,mMonth,mDay)

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis)
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableLongStateOf(calendar.timeInMillis) }

    if (showDatePicker){
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    selectedDate = datePickerState.selectedDateMillis!!
                 }) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(text = "Cancel")
                }
            }) {
            DatePicker(state = datePickerState)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Birthday: "
        )
        FilledIconButton(
            onClick = { showDatePicker = true }
        ) {
            Icon(
                modifier = Modifier.size(size = 30.dp),
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "Time Icon",
            )
        }
        val formatter = SimpleDateFormat("dd-MMM-yyyy")
        Text(text = "Date: ${formatter.format(Date(selectedDate))}")
    }

}
