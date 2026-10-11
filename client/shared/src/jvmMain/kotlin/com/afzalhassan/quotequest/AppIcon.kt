package com.afzalhassan.quotequest

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.painterResource
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.ic_app

@Composable
fun appIcon(): Painter = painterResource(Res.drawable.ic_app)