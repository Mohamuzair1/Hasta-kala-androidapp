package com.example.inventory.ui.login

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventory.R
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_title
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var mobile by remember { mutableStateOf("") }
    var secretCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isRegisterMode by remember { mutableStateOf(false) }

    val fieldsFilled = mobile.isNotBlank() && secretCode.isNotBlank()
    val mobileValid = mobile.length == 10

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8F0),
                        Color(0xFFF5E6D3),
                        Color(0xFFEAD6BC)
                    )
                )
            )
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "✦  हस्तकला  ✦",
            fontSize = 20.sp,
            color = Color(0xFF8B4513),
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D2F0F),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (isRegisterMode) stringResource(R.string.register_subtitle) else stringResource(R.string.login_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF8C684D),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = mobile,
                    onValueChange = { input ->
                        mobile = input.filter { it.isDigit() }.take(10)
                    },
                    label = { Text(stringResource(R.string.mobile_number)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFFFF6EE),
                        unfocusedContainerColor = Color(0xFFFFF6EE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = secretCode,
                    onValueChange = { secretCode = it },
                    label = { Text(stringResource(R.string.secret_code)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFFFF6EE),
                        unfocusedContainerColor = Color(0xFFFFF6EE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = {
                        errorMessage = null
                        if (!fieldsFilled) {
                            errorMessage = "Please enter mobile number and secret code."
                            return@Button
                        }
                        if (!mobileValid) {
                            errorMessage = "Please enter a valid 10-digit mobile number."
                            return@Button
                        }

                        if (isRegisterMode) {
                            saveUserCredentials(context, mobile, secretCode)
                            errorMessage = "Registration successful. You can now log in."
                            isRegisterMode = false
                            secretCode = ""
                            return@Button
                        }

                        val savedMobile = getSavedMobile(context)
                        val savedSecret = getSavedSecret(context)
                        val isAdmin = mobile == "9876543210" && secretCode == "admin123"
                        val isRegisteredUser = mobile == savedMobile && secretCode == savedSecret

                        if (isAdmin || isRegisteredUser) {
                            onLoginSuccess()
                        } else {
                            errorMessage = "Invalid credentials. Please try again."
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD4A574),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (isRegisterMode) stringResource(R.string.register_action) else stringResource(R.string.login_action),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = {
                            isRegisterMode = !isRegisterMode
                            errorMessage = null
                            secretCode = ""
                        }
                    ) {
                        Text(
                            text = if (isRegisterMode) stringResource(R.string.switch_to_login) else stringResource(R.string.switch_to_register),
                            color = Color(0xFF8B4513)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    InventoryTheme {
        LoginScreen(onLoginSuccess = {})
    }
}

private fun saveUserCredentials(context: Context, mobile: String, secret: String) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(KEY_MOBILE, mobile)
        .putString(KEY_SECRET, secret)
        .apply()
}

private fun getSavedMobile(context: Context): String {
    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getString(KEY_MOBILE, "")
        .orEmpty()
}

private fun getSavedSecret(context: Context): String {
    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getString(KEY_SECRET, "")
        .orEmpty()
}

private const val PREFS_NAME = "hasta_kala_auth"
private const val KEY_MOBILE = "registered_mobile"
private const val KEY_SECRET = "registered_secret"
