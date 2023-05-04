package com.ktor.chat.presentation.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.ktor.chat.presentation.chat.ChatViewModel
import com.ktor.chat.ui.theme.TextColor
import com.ktor.chat.ui.theme.P2PBackground
import com.ktor.chat.ui.theme.loginTitle
import java.util.regex.Matcher
import java.util.regex.Pattern

@Composable
fun LoginPage(viewModel: ChatViewModel) {
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
    var OTP by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var vfId by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val result = viewModel.result.observeForever {
        val otpPattern: Pattern = Pattern.compile("(|^)\\d{6}")
        val matcher: Matcher = otpPattern.matcher(it)
        if (matcher.find()) {
            println("Matched")
            matcher.group(0)?.let {
                OTP = OTP.copy(text = it)
            }
        }
    }
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val currentActivity = context as? Activity
    var isOTPSENT by remember {
        mutableStateOf(false)
    }
    println("Result is $result")

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxSize()
            .background(P2PBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Sign Up Now",
                color = TextColor,
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
                    color = TextColor,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(15.dp)
                )
            }
            Text(
                text = "Enter the Following Credentials",
                color = TextColor,
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
            if (isOTPSENT) {
                TextFieldWithIcons(
                    textValue = "Enter OTP",
                    placeholder = "Enter Your OTP",
                    icon = Icons.Filled.Message,
                    mutableText = OTP,
                    onValueChanged = {
                        OTP = it
                    },
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Send,
                )
            }
            println("IS OTP SENT $isOTPSENT")
            if (!isOTPSENT) {
                Button(
                    onClick = {
                        currentActivity?.let {
                            val callbacks =
                                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                        // Verification successful, automatically sign in the user
                                        signInWithPhoneAuthCredential(credential, auth)
                                    }

                                    override fun onVerificationFailed(exception: FirebaseException) {
                                        // Verification failed, show error message to the user
                                        Log.w(TAG, "onVerificationFailed", exception)
                                    }

                                    override fun onCodeSent(
                                        verificationId: String,
                                        token: PhoneAuthProvider.ForceResendingToken,
                                    ) {
                                        vfId = vfId.copy(verificationId)
                                    }
                                }


                            val options = PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber(phone.text) // Phone number to verify
                                .setTimeout(0L,
                                    java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
                                .setCallbacks(callbacks)
                                .setActivity(currentActivity)// OnVerificationStateChangedCallbacks
                                .build()
                            PhoneAuthProvider.verifyPhoneNumber(options)
                        }
                        isOTPSENT = true
                    },
                ) {
                    Text(text = "Send OTP")

                }
            }

        }
        println("VFID is ${vfId.text}")

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {

                        val credential = PhoneAuthProvider.getCredential(vfId.text, OTP.text)
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Sign in success
                                    val user = task.result?.user
                                    println("Success")
                                    // Do something with user
                                } else {
                                    // Sign in failed
                                    val message = task.exception?.message
                                    // Handle error
                                }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(P2PBackground),
                    shape = RoundedCornerShape(15.dp),
                    elevation = ButtonDefaults.buttonElevation(5.dp)
                ) {
                    Text(
                        text = "Sign Up",
                        fontSize = 20.sp,
                        color = TextColor
                    )

                }
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(P2PBackground),
                    shape = RoundedCornerShape(15.dp),
                    elevation = ButtonDefaults.buttonElevation(5.dp)
                ) {
                    Text(
                        text = "Login",
                        fontSize = 20.sp,
                        color = TextColor
                    )
                }
            }
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
    onValueChanged: (TextFieldValue) -> Unit,
) {
    if (keyboardType == KeyboardType.Password) {
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        TextField(
            value = mutableText,
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    tint = Color(0xFF4483D1),
                    contentDescription = "Icon"
                )
            },
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onValueChange = onValueChanged,
            label = { Text(text = textValue, color = TextColor) },
            placeholder = { Text(text = placeholder, color = TextColor) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            modifier = Modifier
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp, end = 15.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = P2PBackground,
                textColor = TextColor
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
            label = { Text(text = textValue, color = TextColor) },
            placeholder = { Text(text = placeholder, color = TextColor) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            modifier = Modifier
                .padding(start = 15.dp, top = 5.dp, bottom = 5.dp, end = 15.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = P2PBackground,
                textColor = TextColor
            ),

            )
    }
}


fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, auth: FirebaseAuth) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Successful")
            } else {
                println("Failed")
            }
        }
}

