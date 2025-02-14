package cn.changjiahong.bamb.app.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cn.changjiahong.bamb.GlobalNavigator
import cn.changjiahong.bamb.bamb.utils.padding

class RegisterScreen:Screen {

    @Composable
    override fun Content() {
        val globalNavigator = GlobalNavigator.current
        Column {
            IconButton(onClick = {
                globalNavigator.pop()
            }, modifier = Modifier.padding {
                paddingTop(10.dp)
            }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "")
            }

            Register()
        }
    }

}

@Composable
fun RegisterScreen.Register(){
    val registerViewModel = koinScreenModel<RegisterViewModel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xE5E5E5))
            .padding(40.dp, 20.dp)
    ) {

        Text(
            "Sign Up",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
        )

        Input4Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally), registerViewModel
        )

        Button(
            onClick = {   },
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
            Text("Sign Up")
        }

//        val loginNavController = LoginNavController.current
//
//        HtmlActionView(
//            "Already have an account? <click action=\"action1\"><font color=\"#ffffff\"><b>Sign In!</b></font></click>",
//            modifier = Modifier.padding { paddingTop(20.dp) },
//            textColor = "#ff333333"
//        ) {
//            when (it) {
//                "action1" -> {
//                    // 用户协议
//                    loginNavController.navigate(RR.LOGIN_SCREEN){
//                        popUpTo<RR.LOGIN_SCREEN>(){
//                            inclusive = true
//                        }
//                    }
//                }
//            }
//        }
    }
}

@Composable
fun Input4Box(modifier: Modifier = Modifier, registerViewModel: RegisterViewModel) {

    val uiState by registerViewModel.uiState.collectAsState()

    InputView(
        value = uiState.username,
        label = "User Name",
        leadingIcon = Icons.Filled.Person,
        onValueChange = { RegisterUiEvent.EnterUsername(it).sendTo(registerViewModel) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding {
                paddingTop(30.dp)
            }
    )

    InputView(
        value = uiState.email,
        label = "Email",
        leadingIcon = Icons.Filled.Email,
        onValueChange = { RegisterUiEvent.EnterEmail(it).sendTo(registerViewModel) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding {
                paddingTop(30.dp)
            }
    )


    InputView(
        value = uiState.password,
        label = "Password",
        leadingIcon = Icons.Filled.Lock,
        onValueChange = { RegisterUiEvent.EnterPassword(it).sendTo(registerViewModel) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onDone = {
//                    Log.d("TT", password)
        }),
        modifier = modifier
            .fillMaxWidth()
            .padding {
                paddingTop(20.dp)
            }
    )

    InputView(
        value = uiState.reEnterPassword,
        label = "RePassword",
        leadingIcon = Icons.Filled.Lock,
        onValueChange = { RegisterUiEvent.EnterRePassword(it).sendTo(registerViewModel) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onDone = {
//                    Log.d("TT", password)
        }),
        modifier = modifier
            .fillMaxWidth()
            .padding {
                paddingTop(20.dp)
            }
    )
}

