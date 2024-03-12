package com.example.lab3layout

import android.os.Bundle
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import com.example.lab3layout.ui.theme.Lab3LayoutTheme
import org.intellij.lang.annotations.JdkConstants.BoxLayoutAxis

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab3LayoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    RowLayout("World" , "We are cats" )
//                    BoxLayout()
//                    BoxLayout2()
               ConstraintLayoutEx()
//                    BackGroundBox()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab3LayoutTheme {
     RowLayout("World" , "We are cats" )
//        BoxLayout()
    }
}

@Composable
fun RowLayout(message1: String, message2: String, modifier: Modifier = Modifier){
    Row{
        Text(
            text = "Hello $message1!",
            style = TextStyle(background = Color.Yellow),
            fontSize = 30.sp,
            modifier = modifier
        )
        Text(
            text = "Hello $message2!",
            style = TextStyle(background = Color.Gray),
            fontSize = 20.sp,
            modifier = Modifier
                .padding(5.dp)
        )
        Image(
            painter = painterResource(R.drawable.cartoon_cat),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
fun BoxLayout(){
    Box(
        modifier = Modifier
            .background(Color(0.447f, 0.878f, 0.933f, 0.749f))
            .fillMaxSize()
    ){
        Text(
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.TopStart),
            fontSize = 20.sp,
            text = "TopStart"
        )
        Text(
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.TopCenter),
            fontSize = 20.sp,
            text = "TopCenter"
        )
        Text(
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.TopEnd),
            fontSize = 20.sp,
            text = "TopEnd"
        )
        Text(
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.CenterStart),
            fontSize = 20.sp,
            text = "CenterStart"
        )
        Text(
            modifier = Modifier
                .background(Color(0.933f, 0.063f, 0.514f, 0.749f))
                .padding(10.dp)
                .align(Alignment.Center),
            fontSize = 20.sp,
            text = "Center"
        )
        Text(
            modifier = Modifier
                .background(Color(0.933f, 0.063f, 0.514f, 0.749f))
                .padding(10.dp)
                .align(Alignment.CenterEnd),
            fontSize = 20.sp,
            text = "CenterEnd"
        )
        Text(
            modifier = Modifier
                .background(Color(0.933f, 0.063f, 0.514f, 0.749f))
                .padding(10.dp)
                .align(Alignment.BottomStart),
            fontSize = 20.sp,
            text = "BottomStart"
        )
        Text(
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.BottomCenter),
            fontSize = 20.sp,
            text = "BottomCenter"
        )
        Text(
            modifier = Modifier
                .background(Color.Yellow)
                .padding(10.dp)
                .align(Alignment.BottomEnd),
            fontSize = 20.sp,
            text = "BottomEnd"
        )
    }
}



@Composable
fun ColumnLayout(message1: String, message2: String, modifier: Modifier = Modifier){
    Column{
        Text(
            text = "Hello $message1!",
            style = TextStyle(background = Color.Yellow),
            fontSize = 30.sp,
            modifier = modifier
        )
        Text(
            text = "Hello $message2!",
            style = TextStyle(background = Color.Gray),
            fontSize = 20.sp,
            modifier = Modifier
                .padding(5.dp)
        )
        Image(
            painter = painterResource(R.drawable.cartoon_cat),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(100.dp)
        )
    }
}


@Composable
fun ConstraintLayoutEx(){
    ConstraintLayout(
        modifier = Modifier.fillMaxSize())
    {
        val(firstText, secondText, catImage) = createRefs()
        Text(
            text = "Hello World!",
            style = TextStyle(background = Color.Yellow),
            fontSize = 30.sp,
            modifier = Modifier
                .constrainAs(firstText){
                    centerHorizontallyTo(parent)
                }
        )
        Text(
            text = "We are cats!",
            style = TextStyle(background = Color.Gray),
            fontSize = 20.sp,
            modifier = Modifier
                .padding(5.dp)
                .constrainAs(secondText) {
                    top.linkTo(catImage.bottom)
                    centerHorizontallyTo(parent)
                }
        )
        Image(
            painter = painterResource(R.drawable.cartoon_cat),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(300.dp)
                .border(
                    width = 0.8.dp,
                    color = Color.Blue,
                    shape = RoundedCornerShape(10.dp)
                )
                .constrainAs(catImage) {
                    top.linkTo(firstText.bottom)
                    centerHorizontallyTo(parent)
                }
        )
    }

}

@Composable
fun BoxLayout2(){
    Box(modifier = Modifier
        .wrapContentSize(unbounded = true, align = Alignment.CenterEnd)
        .border(
            width = 1.dp,
            color = Color.Green,
            shape = RoundedCornerShape(32.dp)
        )
    ){
        Image(
            painter = painterResource(id = R.drawable.cartoon_cat) ,
            contentDescription = "cat image",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(300.dp)
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            text = "We are cats.",
            fontSize = 30.sp,
            color = Color(50,50,250)
        )
    }
}


@Composable
fun BackGroundBox(){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.3F
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(unbounded = true, align = Alignment.TopEnd)
            .border(
                width = 1.dp,
                color = Color.Green.copy(alpha = 0.5f),
                shape = RoundedCornerShape(28.dp)
            )
    ){
        Image(   painter = painterResource(R.drawable.cartoon_cat) ,
            contentDescription = "cat image",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = "We are cats!",
            fontSize = 25.sp,
            color = Color.Magenta,
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.BottomCenter)
        )
        Text(
            text = "Happy Cat!",
            fontSize = 25.sp,
            color = Color.Red,
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.TopCenter)
        )
    }
}