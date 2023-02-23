package com.example.mycomposeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HomeActivity : ComponentActivity() {
    var imgURL = R.drawable.online_mage
    var img1 = imgURL
    var img2 = imgURL
    var img3 = imgURL
    var img4 = imgURL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyUI()
        }
    }
}

@Composable
fun MyUI(){
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()){
            MyYearText("2020")
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.online_mage),
                contentDescription = R.drawable.online_mage.toString(),
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .padding(20.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.online_mage),
                contentDescription = R.drawable.online_mage.toString(),
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .padding(20.dp)
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            MyText("img1")
            MyText("img2")
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)) {
            MyYearText("2019")
        }
        Row() {
            Image(
                painter = painterResource(id = R.drawable.online_mage),
                contentDescription = R.drawable.online_mage.toString(),
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .padding(20.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.online_mage),
                contentDescription = R.drawable.online_mage.toString(),
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .padding(20.dp)
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)) {
            MyText("img3")
            MyText("img4")
        }


    }
}

@Composable
fun MyText(desc: String){
    Text(
        text = desc,
        fontFamily = FontFamily.Monospace,
        textAlign = TextAlign.Left,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Red,
        modifier = Modifier.padding(horizontal = 30.dp)
    )
}

@Composable
fun MyYearText(year: String){
    Text(
        text = year,
        fontFamily = FontFamily.Monospace,
        textAlign = TextAlign.Left,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Red,
    )
}
