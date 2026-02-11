package com.giovanna.amatucci.desafio_android_picpay

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.giovanna.amatucci.desafio_android_picpay.di.desktopModule
import com.giovanna.amatucci.desafio_android_picpay.di.initKoin
import java.awt.Dimension
import java.io.File

fun main() = application {
    initKoin {
        modules(desktopModule)
    }
    Window(
        onCloseRequest = { exitApplication() },
        title = "PicPay KMP Challenge",
        state = rememberWindowState(width = 400.dp, height = 800.dp)
    ) {
        window.minimumSize = Dimension(400, 600)
        App()
        println("Onde estou salvando? -> " + File(".").absolutePath)
    }
}