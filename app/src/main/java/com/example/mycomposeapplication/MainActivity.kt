package com.example.mycomposeapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycomposeapplication.ui.theme.Purple200
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Retrofit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginUI(applicationContext)
        }
    }
}

@Composable
fun LoginUI(context: Context) {
    val mContext = LocalContext.current

    var  userID by remember {mutableStateOf("")}
    var  password by remember {mutableStateOf("")}

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        OutlinedTextField(
            value = userID,
            onValueChange = {userID = it},
            label = {Text ("Enter Your userID")},
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Green,
                unfocusedBorderColor = Purple200),
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "person")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = {Text ("Enter Your pass")},
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Green,
                unfocusedBorderColor = Purple200),
            leadingIcon = {
                Icon(Icons.Default.Info, contentDescription = "pass")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp)
        )

        OutlinedButton(
            onClick = { logged(userID,password,context)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            shape = CircleShape,
            border = BorderStroke(2.dp,
            Color.Green))
        {
            Text(
                text = "Login",
                textAlign = TextAlign.Center)
        }

    }
}

fun logged(userID: String, password: String, context: Context) {
    if (checkPassFormat(password, context) && checkUserIDFormat(userID, context)){
        //try to login
        Log.i("All good","Ready to login")
        //take token and go to next page
        rawJSON(userID,password,context)
    }
    goToHomePage(context)

}

fun checkPassFormat(password: String, context: Context):Boolean {
    //2num+3LC+2UC+1SC//any order
    var pattern = Regex("^(?=(?:.*[A-Z].*){2})(?!(?:.*[A-Z].*){3,})(?=(?:.*[^\\w\\s].*){1})(?!(?:.*[^\\w\\s].*){2,})(?=(?:.*[a-z].*){3})(?!(?:.*[a-z].*){4,})(?=(?:.*\\d.*){2})(?!(?:.*\\d.*){3,}).*\$")
    var resultt = pattern.containsMatchIn(password)
    if(resultt){
        Log.i("Pass Format",resultt.toString())
        return true
    }else {
        Log.i("Pass Format",resultt.toString())
        Toast.makeText(context,"Check Your Password",Toast.LENGTH_SHORT).show()
        return false
    }
}

fun checkUserIDFormat(userID: String, context: Context):Boolean {
    //"[A-Z]{2}\d{4}" && length=6
    var pattern = Regex("[A-Z]{2}\\d{4}")
    var resultt = pattern.containsMatchIn(userID)
    //Log.i("test ergex",resultt.toString())
    if(userID.length == 6 && resultt){
        Log.i("UserID Format","UserID Correct Format")
        return  true
    }else{
        Log.i("UserID Format","UserID Wrong Format")
        Toast.makeText(context,"Check Your UserID",Toast.LENGTH_SHORT).show()
        return false
    }
}

fun rawJSON(clientID: String, clientSecret: String,context: Context){

    // Create Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("https://3nt-demo-backend.azurewebsites.net/Access/")
        .build()

    // Create Service
    val service = retrofit.create(APIService::class.java)

    // Create JSON using JSONObject
    val jsonObject = JSONObject()
    jsonObject.put("UserName", clientID)
    jsonObject.put("Password", clientSecret)

    // Convert JSONObject to String
    val jsonObjectString = jsonObject.toString()
    // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

    CoroutineScope(Dispatchers.IO).launch {
        // Do the POST request and get response
        val response = service.createEmployee(requestBody)

        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {

                // Convert raw JSON to pretty JSON using GSON library
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string()
                    )
                )
                //toast token
                val jsonObject = JSONTokener(prettyJson).nextValue() as JSONObject
                val access_token = jsonObject.getString("access_token")
                val refresh_token = jsonObject.getString("refresh_token")
                val token_type = jsonObject.getString("token_type")
                val expires_in = jsonObject.getString("expires_in")
                Log.i("refresh_token: ", access_token)
                Toast.makeText(context,"Your token is: $access_token",Toast.LENGTH_SHORT).show()
                //move to next activity
                goToHomePage(context)
                Log.d("Pretty Printed JSON :", prettyJson)
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())

            }
        }
    }
}

fun goToHomePage(context: Context){
    val intent = Intent(context, HomeActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

