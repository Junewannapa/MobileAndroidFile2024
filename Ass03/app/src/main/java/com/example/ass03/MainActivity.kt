@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.ass03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ass03.ui.theme.Ass03Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Ass03Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BirthdeyCard()
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
    Ass03Theme {
        Greeting("Android")
    }
}


@Composable
fun BirthdeyCard(){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            alpha = 0.3F,
            modifier = Modifier.fillMaxSize()
        )
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(unbounded = true, align = Alignment.Center)
    ){
        val (text1,pic1) = createRefs()
        Image(   painter = painterResource(R.drawable.pic1) ,
            contentDescription = "image",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(250.dp)
                .constrainAs(pic1){
                    centerHorizontallyTo(parent)

                }
        )
        Text(
            text = "I hope you have \n a good everyday.",
            fontSize = 25.sp,
            color = Color.Black,
            modifier = Modifier.constrainAs(text1) {
                top.linkTo(pic1.bottom, margin = 15.dp)
                centerHorizontallyTo(parent)

            }
            )
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(unbounded = true, align = Alignment.BottomEnd)

    ){
        val (text2,pic2) = createRefs()
      Text(text = "From June",
        fontSize = 20.sp,
        color = Color.Black,
          style = TextStyle(background = Color.Gray),
          modifier = Modifier.constrainAs(text2) {
              bottom.linkTo(pic2.top, margin = 10.dp)
              centerHorizontallyTo(parent)

          }
      )
        Image(
            painter = painterResource(R.drawable.pic2) ,
            contentDescription = "cat image",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(150.dp)
                .constrainAs(pic2){
                    centerHorizontallyTo(parent)
                }
        )
    }
}

