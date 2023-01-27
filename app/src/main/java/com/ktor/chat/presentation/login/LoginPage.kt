package com.ktor.chat.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ktor.chat.ui.theme.loginBackground
import com.ktor.chat.ui.theme.loginTitle

@Composable
fun LoginPage() {
    var email by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var name by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var username by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var password by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var phone by remember {
        mutableStateOf(TextFieldValue("+91"))
    }

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxSize()
            .background(loginBackground)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Sign Up Now",
                color = Color.White,
                fontSize = 45.sp,
                modifier = Modifier
                    .padding(start = 15.dp, end = 10.dp, top = 15.dp, bottom = 25.dp)
                    .fillMaxWidth(),
                fontFamily = loginTitle,
                style = TextStyle(fontStyle = FontStyle.Normal)
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp)) {
                    ProfileImage(
                        imageUrl = com.ktor.chat.R.drawable.pfp,
                        modifier = Modifier
                            .size(60.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFF4483D1),
                                shape = CircleShape
                            )
                            .padding(3.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = "Hi,\nThis is Kailash, Developer",
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(15.dp)
                    )
            }
            Text(
                text = "Enter the Following Credentials",
                color = Color.White,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(start = 15.dp, end = 10.dp, top = 15.dp, bottom = 25.dp)
                    .fillMaxWidth(),
                fontFamily = loginTitle,
                style = TextStyle(fontStyle = FontStyle.Normal)
            )
            TextFieldWithIcons(
                textValue = "Email ID",
                placeholder = "Enter Your Email",
                icon = Icons.Filled.Email,
                mutableText = email,
                onValueChanged = {
                    email = it
                },
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            )
            TextFieldWithIcons(
                textValue = "Name",
                placeholder = "Enter Your Name",
                icon = Icons.Filled.Info,
                mutableText = name,
                onValueChanged = {
                    name = it
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            )
            TextFieldWithIcons(
                textValue = "Username",
                placeholder = "Enter Your Username",
                icon = Icons.Filled.Face,
                mutableText = username,
                onValueChanged = {
                    username = it
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            )
            TextFieldWithIcons(
                textValue = "Password",
                placeholder = "Enter Your Password",
                icon = Icons.Filled.Lock,
                mutableText = password,
                onValueChanged = {
                    password = it
                },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
            )
            TextFieldWithIcons(
                textValue = "Phone Number",
                placeholder = "Enter Your Number",
                icon = Icons.Filled.PhoneAndroid,
                mutableText = phone,
                onValueChanged = {
                    phone = it
                },
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Send,
            )


        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithIcons(
    textValue: String,
    placeholder: String,
    icon: ImageVector,
    mutableText: TextFieldValue,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onValueChanged: (TextFieldValue) ->Unit
) {
    if (keyboardType == KeyboardType.Password){
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        TextField(
            value = mutableText,
            leadingIcon = { Icon(
                imageVector = icon,
                tint = Color(0xFF4483D1),
                contentDescription = "Icon"
            ) },
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, description)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onValueChange = onValueChanged,
            label = { Text(text = textValue) },
            placeholder = { Text(text = placeholder) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            modifier = Modifier
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp, end = 15.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = loginBackground,
                textColor = Color.White
            ))
    } else {
        TextField(
            value = mutableText,
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    tint = Color(0xFF4483D1),
                    contentDescription = "Icon"
                )
            },
            onValueChange = onValueChanged,
            label = { Text(text = textValue) },
            placeholder = { Text(text = placeholder) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            modifier = Modifier
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp, end = 15.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = loginBackground,
                textColor = Color.White
            ),

            )
    }
}