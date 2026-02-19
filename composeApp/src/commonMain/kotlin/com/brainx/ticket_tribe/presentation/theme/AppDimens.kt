package com.brainx.ticket_tribe.presentation.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed interface AppDimens{


    data object Padding: AppDimens {
        val zero = 0.dp
        val textPadding = 4.0.dp
        val xxSmallPadding = 2.0.dp
        val smallPadding = 8.0.dp
        val defaultPadding = 16.0.dp
        val mediumPadding = 20.0.dp
        val xLargePadding = 42.0.dp
        val smallPadding12 = 12.0.dp
    }

    data object Radius: AppDimens {

        val radius12 = 12.0.dp
        val radius16 = 16.0.dp
    }

    data object Margins: AppDimens {

    }

    data object Button: AppDimens {
        val zero = 0.dp
        val defaultButtonHeight =  58.dp
    }

    data object EditText: AppDimens {
        val searchBarHeight = 58.dp
    }



    data object Icons: AppDimens {
        val loaderSize = 60.0.dp
    }



    data object ListItems: AppDimens {
    }



    data object ShapeDimens: AppDimens{
        val defaultEditTextCornerRadius = 8.dp
    }

    data object Fonts: AppDimens{
        val font16 = 16.sp
        val font18 = 18.sp
        val font24 = 24.sp

    }

}