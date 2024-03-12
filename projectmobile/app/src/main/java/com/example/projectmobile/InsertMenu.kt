
package com.example.projectmobile

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
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
@Composable
fun InsertMenuScreen(navController: NavController) {
    val MenuTypeList = remember { mutableStateListOf<MenuTypeClass>() }
    val selectedFoodType = remember { mutableStateOf("") }
    val context = LocalContext.current
    val createClient = MenuTypeAPI.create()
    val createMenuClient = MenuAPI.create()
    val menuTypes = remember { mutableStateListOf<String>() }
    var textFieldName by remember { mutableStateOf("") }
    var textFieldPrice by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
            }
        }
    )

    // ดึงข้อมูลประเภทอาหารจาก API
    LaunchedEffect(Unit) {
        val response = createClient.retrieveMenuType().execute()
        if (response.isSuccessful) {
            response.body()?.forEach{MenuTypeList.add(
                MenuTypeClass(
                it.food_type_id,
                it.food_type_name,
                it.food_type_img,
                )
            )
            }

        } else {
            Toast.makeText(context, "Error retrieving food types", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Add A New Menu",
            fontSize = 25.sp
        )
        OutlinedTextField(
            value = textFieldName,
            onValueChange = { textFieldName = it },
            label = { Text("Enter Name") }
        )
        val mMaxLength = 4
        OutlinedTextField(
            value = textFieldPrice,
            onValueChange = { if (it.length <= mMaxLength) textFieldPrice = it },
            label = { Text("Enter Price") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .size(300.dp)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Image(
                painter = if (imageUri != null) {
                    rememberAsyncImagePainter(imageUri)
                } else {
                    painterResource(id = R.drawable.image_search)
                },
                contentDescription = null,
                modifier = Modifier
                    .size(270.dp)
                    .align(Alignment.Center)
            )
        }
        Button(
            modifier = Modifier.width(180.dp),
            onClick = {
                galleryLauncher.launch("image/*")
            }
        ) {
             Text("Open Gallery")
        }

        // Dropdown menu เพื่อเลือกประเภทอาหาร
//        Column {
//            Text("Select Food Type")
//            DropdownMenu(
//                expanded = false,
//                onDismissRequest = { /* Dismiss the dropdown */ }
//            ) {
//                menuTypes.forEach { foodType ->
//                    DropdownMenuItem(
//                        onClick = {
//                            selectedFoodType.value = foodType
//                        }
//                    ) {
//                        Text(foodType)
//                    }
//                }
//            }
//        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.width(130.dp),
                onClick = {
                    val inputStream = context.contentResolver.openInputStream(imageUri!!)
                        ?: throw Exception("Failed to open input stream")
                    val imageFile = File.createTempFile("image", ".jpg")
                    val outputStream = FileOutputStream(imageFile)
                    inputStream.copyTo(outputStream)
                    inputStream.close()
                    outputStream.close()

                    val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image",
                        imageFile.name, requestBody)
                    val nameRequestBody = textFieldName.toRequestBody("text/plain".toMediaTypeOrNull())
                    val priceRequestBody = textFieldPrice.toRequestBody("text/plain".toMediaTypeOrNull())

                    // เพิ่ม request body สำหรับ food_type_id
                    val foodTypeIdRequestBody = selectedFoodType.value.toRequestBody("text/plain".toMediaTypeOrNull())

                    createMenuClient.uploadMenu(imagePart, nameRequestBody, priceRequestBody, foodTypeIdRequestBody
                    ).enqueue(object : Callback<MenuClass> {
                        override fun onResponse(call: Call<MenuClass>,
                                                response: Response<MenuClass>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Successfully Inserted",
                                    Toast.LENGTH_SHORT).show()
                                navController.navigate(Screen.Menu.route) {
                                    popUpTo(Screen.InsertMenu.route) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Error",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<MenuClass>, t: Throwable) {
                            Toast.makeText(context, "Error onFailure" + t.message,
                                Toast.LENGTH_LONG).show()
                        }
                    })
                }
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.width(10.dp))
            Button(
                modifier = Modifier.width(130.dp),
                onClick = {
                    textFieldName = ""
                    textFieldPrice = ""
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                    navController.navigate(Screen.Menu.route)
                }
            ) {
                Text("Cancel")
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                itemsIndexed(
                    items = MenuTypeList,
                    itemContent = {index, item ->
                        Card {
                            Row {
                                Text(text =" ${item.food_type_name}\n"+
                                "${item.food_type_id}")
                            }
                        }
                    }
                )
            }
        }
    }
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DropdownMenuTypeItem(blockTypeList: List<MenuTypeClass>): String {
//    val keyboardController = LocalSoftwareKeyboardController.current
//    var expanded by remember { mutableStateOf(false) }
//    var selectedMenuTypeId by remember { mutableStateOf("") }
//    var selectedMenuTypeName by remember { mutableStateOf("") }
//
//    ExposedDropdownMenuBox(
//        modifier = Modifier.clickable { keyboardController?.hide() },
//        expanded = expanded,
//        onExpandedChange = { expanded = !expanded }
//    ) {
//        OutlinedTextField(
//            modifier = Modifier
//                .width(340.dp)
//                .menuAnchor()
//                .clickable { keyboardController?.hide() },
//            textStyle = TextStyle.Default.copy(fontSize = 12.sp),
//            readOnly = true,
//            value = selectedMenuTypeName,
//            onValueChange = {},
//            label = { Text(text = "Menu Type") },
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
//            colors = ExposedDropdownMenuDefaults.textFieldColors(),
//        )
//
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            blockTypeList.forEach { blockType ->
//                DropdownMenuItem(
//                    onClick = {
//                        selectedMenuTypeId = blockType.food_type_id
//                        selectedMenuTypeName = blockType.food_type_name
//                        expanded = false
//                    }
//                ) {
//                    Text(text = blockType.food_type_name)
//                }
//            }
//        }
//    }
//    return selectedMenuTypeId
//}
