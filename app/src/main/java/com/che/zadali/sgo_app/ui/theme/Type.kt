package com.che.zadali.sgo_app.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.che.zadali.sgo_app.R


val LightTypography = Typography(
    //On top bar
    h5 = TextStyle(
        color = MainTextColor,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        color = MainTextColor,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    caption = TextStyle(
        color = PrimaryBlue,
        fontSize = 12.sp
    ),
    subtitle1 = TextStyle(
        color = MainTextColor,
        fontSize = 16.sp
    ),
    button = TextStyle(
        color = PrimaryBlue,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        color = MainTextColor,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)
val SecondGradeFont = FontFamily(Font(R.font.lobster))
val DarkTypography = Typography(
    h5 = TextStyle(color = DarkMainTextColor),
    //On top bar
    h6 = TextStyle(
        color = DarkMainTextColor,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    caption = TextStyle(
        color = DarkCaptionTextBlue,
        fontSize = 12.sp
    ),
    subtitle1 = TextStyle(
        color = DarkMainTextColor,
        fontSize = 16.sp
    ),
    button = TextStyle(
        color = DarkCaptionTextBlue,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        color = DarkMainTextColor,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)