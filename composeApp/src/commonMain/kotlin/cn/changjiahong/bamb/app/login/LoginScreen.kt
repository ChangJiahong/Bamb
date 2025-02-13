package cn.changjiahong.bamb.app.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cn.changjiahong.bamb.bamb.utils.padding

object LoginScreen : Screen {
    @Composable
    override fun Content() = LoginView()
}

@Composable
fun Screen.LoginView() {
    val loginViewModel: LoginViewModel = koinScreenModel<LoginViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xE5E5E5))
            .padding(40.dp, 50.dp)
    ) {


//        LaunchedEffect(Unit) {
//            loginViewModel.handleEffect { value ->
//                when (value) {
//                    LoginUiEffect.LoginSuccess -> {
//                        // 登录成功
//                        activity.startCompose(RR.MAIN_SCREEN)
//                        activity.finish()
//                    }
//
//                    is LoginUiEffect.LoginError -> {
//                        Toast.makeText(context, value.msg, Toast.LENGTH_SHORT).show()
//                    }
//
//                    else -> return@handleEffect false
//                }
//                true
//            }
//        }

        Text(
            "Sign In",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
        )

        InputBox(
            modifier = Modifier
                .align(Alignment.CenterHorizontally), loginViewModel
        )

        val (checkedState, onStateChange) = remember { mutableStateOf(userStore.isSaveInfo) }

        CheckTextBox("Remember your email and password", checkedState, onStateChange)
        val focusManager = LocalFocusManager.current

        Button(
            onClick = {
                focusManager.clearFocus()
                LoginUiEvent.SubmitLogin(checkedState).sendTo(loginViewModel)
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(65.dp)
                .padding {
                    paddingTop(20.dp)
                },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.Black
            )
        ) {
            Text("Sign In")
        }

//        val loginNavController = LoginNavController.current

//        HtmlActionView(
//            "Don’t have an account? <click action=\"action1\"><font color=\"#ffffff\"><b>Sign up!</b></font></click>",
//            modifier = Modifier.padding { paddingTop(20.dp) },
//            textColor = "#ff333333"
//        ) {
//            when (it) {
//                "action1" -> {
//                    // 用户协议
//                    loginNavController.navigate(RR.REGISTER_SCREEN)
//                }
//            }
//        }
    }
}

@Composable
fun CheckTextBox(
    text: String,
    checkedState: Boolean,
    onStateChange: (Boolean) -> Unit = {},
    color: Color = Color.Blue,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(20.dp)
            .toggleable(
                value = checkedState,
                onValueChange = { onStateChange(!checkedState) },
                role = Role.Checkbox
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(15.dp)
                .aspectRatio(1f)
                .border(
                    width = 1.dp,
                    color = if (checkedState) color else Color.Gray,
                    shape = CircleShape
                )
                .background(
                    color = if (checkedState) color.copy(alpha = 0.2f) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checkedState) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Checked",
                    tint = color,
                    modifier = Modifier.fillMaxSize(0.5f)
                )
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}


@Composable
fun InputBox(modifier: Modifier = Modifier, loginViewModel: LoginViewModel) {
    val focusManager = LocalFocusManager.current

    val uiState by loginViewModel.uiState.collectAsState()

    InputView(
        value = uiState.email,
        label = "Email",
        onValueChange = { LoginUiEvent.EnterEmail(it).sendTo(loginViewModel) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding {
                paddingTop(30.dp)
            },
        errorText = uiState.emailError
    )

    InputView(
        value = uiState.password,
        label = "Password",
        onValueChange = { loginViewModel.sendEvent(LoginUiEvent.EnterPassword(it)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        modifier = modifier
            .fillMaxWidth()
            .padding {
                paddingTop(20.dp)
            },
        errorText = uiState.pwdError
    )
}

@Composable
fun InputView(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "",
    errorText: String = "",
    leadingIcon: ImageVector? = null,
    onValueChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val isError = errorText.isNotEmpty()
    Column {
        OutlinedTextField(
            value,
            onValueChange = onValueChange,
            label = { Text(label, style = MaterialTheme.typography.labelMedium) },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = if (keyboardOptions.keyboardType == KeyboardType.Password)
                PasswordVisualTransformation('*') else VisualTransformation.None,
            singleLine = true,
            leadingIcon = if (leadingIcon != null) {
                @Composable { Icon(leadingIcon, label) }
            } else null,
            modifier = modifier,
            isError = isError,
            trailingIcon = {
                if (isError) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "error",
                        tint = Color.Red
                    )
                }
            },
            shape = RoundedCornerShape(10.dp, 15.dp, 15.dp, 20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isError) {
            Text(
                text = errorText,
                color = Color.Red,
                modifier = Modifier.padding(6.dp, 0.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
