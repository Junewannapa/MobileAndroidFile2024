package com.example.projectmobile

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import java.io.File
import java.io.FileOutputStream

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuScreen(navController: NavHostController){
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<MenuClass>("data") ?:
    MenuClass(0,"",0,"",0,0)

    val contextForToast = LocalContext.current
    val createClient = MenuAPI.create()

    var menu_id by remember { mutableIntStateOf(data.food_id)}
    var textFieldMenuName by remember { mutableStateOf(data.food_name)}
    var textFieldMenuPrice by remember { mutableStateOf(data.price.toString())}
    var image by remember { mutableStateOf(data.food_img) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri ->
            uri?.let{
                imageUri = it
            }
        }
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            value = textFieldMenuName,
            onValueChange  = {textFieldMenuName = it},
            label = {Text("Menu name ")},
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            value = textFieldMenuPrice,
            onValueChange  = {textFieldMenuPrice = it},
            label = {Text("Menu Price")},
        )
        Spacer(modifier = Modifier.heightIn(5.dp))
        Box(modifier = Modifier
            .size(270.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(30.dp)
            )
        ){
            Image(painter = if(imageUri != null){
                rememberAsyncImagePainter(imageUri!!)
            }else{
                rememberAsyncImagePainter(image)
            },
                contentDescription = null,
                modifier =
                Modifier.size(250.dp)
                    .align(Alignment.Center)
            )
        }
        Button(modifier = Modifier.width(180.dp),
            onClick = {
                galleryLauncher.launch("image/*")
            }){
            Text("Open Gallery")
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            Spacer(modifier = Modifier.width(5.dp))
            Button(modifier = Modifier.width(100.dp),
                onClick = {
                    if (imageUri != null) {
                        val inputStream = contextForToast.contentResolver.openInputStream(imageUri!!)
                        if (inputStream != null) {
                            val imageFile = File.createTempFile("image", ".jpg")
                            val outputStream = FileOutputStream(imageFile)
                            inputStream.copyTo(outputStream)
                            inputStream.close()
                            outputStream.close()

                            val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

                            val nameRequestBody = textFieldMenuName.toRequestBody("text/plain".toMediaTypeOrNull())
                            val priceRequestBody = textFieldMenuPrice.toRequestBody("text/plain".toMediaTypeOrNull())
                            createClient.editImage(menu_id, nameRequestBody, priceRequestBody, imagePart)
                                .enqueue(object : Callback<MenuClass> {
                                    override fun onResponse(call: Call<MenuClass>, response: retrofit2.Response<MenuClass>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(contextForToast, "Successfully Updated", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(contextForToast, "Error", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<MenuClass>, t: Throwable) {
                                        Toast.makeText(contextForToast, "Error onFailure" + t.message, Toast.LENGTH_LONG).show()
                                    }
                                })
                            if (navController.currentBackStack.value.size >= 2) {
                                navController.popBackStack()
                            }
                            navController.navigate(Screen.Menu.route)
                        } else {
                            Toast.makeText(contextForToast, "Failed to open input stream", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(contextForToast, "Image URI is null", Toast.LENGTH_SHORT).show()
                    }
                }) {
                Text("Update")
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(modifier = Modifier
                .width(100.dp),
                onClick = {
                    if(navController.currentBackStack.value.size >= 2){
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.Menu.route)
                }
            ){
                Text("Cancel")
            }
        }
    }
}
