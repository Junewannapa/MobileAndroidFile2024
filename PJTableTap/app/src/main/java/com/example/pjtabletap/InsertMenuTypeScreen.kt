package com.example.pjtabletap

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertMenuTypeScreen(navController: NavController) {
    val contextForToast = LocalContext.current
    val createClient = MenuTypeAPI.create()
    var textFieldTypeName by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri ->
            uri?.let{
                imageUri = it
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Add A New Menu Type",
            fontSize = 25.sp
        )
        OutlinedTextField(
            value = textFieldTypeName,
            onValueChange = {textFieldTypeName = it},
            label = { Text("Enter Type Name" ) }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier
            .size(300.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(30.dp)
            )
        ){
            Image(
                painter = if (imageUri != null) {
                    rememberAsyncImagePainter(
                        imageUri
                    )
                }else{
                    painterResource(id = R.drawable.image_search)
                },
                contentDescription = null,
                modifier = Modifier
                    .size(270.dp)
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            Button(modifier = Modifier
                .width(130.dp),
                onClick = {
                    val inpurStream = contextForToast.contentResolver.openInputStream(imageUri!!)
                        ?: throw Exception("Failed to open input stream")
                    val imageFile = File.createTempFile("image",".jpg")
                    val outputStream = FileOutputStream(imageFile)
                    inpurStream.copyTo(outputStream)
                    inpurStream.close()
                    outputStream.close()

                    val  requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image",
                        imageFile.name, requestBody)
                    val nameRequestBody = textFieldTypeName.toRequestBody("text/plain".toMediaTypeOrNull())


                    createClient.inserttype(imagePart, nameRequestBody).enqueue(object : Callback<MenuTypeClass> {
                        override fun onResponse(call: Call<MenuTypeClass>, response: Response<MenuTypeClass>){
                            if(response.isSuccessful){
                                Toast.makeText(contextForToast, "Successfully Inserted",
                                    Toast.LENGTH_SHORT).show()
                                navController.navigate(Screen.Menu.route) {
                                    popUpTo(Screen.InsertMenuType.route) {
                                        inclusive = true
                                    }
                                }
                            }else{
                                Toast.makeText(contextForToast,"Error",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<MenuTypeClass>, t: Throwable){
                            Toast.makeText(contextForToast, "Error onFailure" + t.message,
                                Toast.LENGTH_LONG).show()
                        }
                    })
                }
            ){
                Text("Save")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(modifier = Modifier
                .width(130.dp),
                onClick = {
                    textFieldTypeName = ""
                    if(navController.previousBackStackEntry != null){
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.Menu.route)
                }){
                Text("Cancel")
            }
        }
    }

}