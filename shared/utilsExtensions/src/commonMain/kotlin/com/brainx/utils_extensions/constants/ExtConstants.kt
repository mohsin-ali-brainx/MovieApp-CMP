package com.brainx.utils_extensions.constants

sealed class ExtConstants {

    object IntegerConstants{
        const val ZERO = 0
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
        const val FIVE = 5
        const val SEVEN=7
        const val NINE = 9
        const val TEN = 10
        const val THIRTY = 30
        const val FIFTY = 50
        const val SIXTY = 60
        const val SECOND = 60
        const val HALF_SECOND = 500L
        const val ONE_SECOND = 1000L
        const val ONE_MINUTE = 60*1000L
        const val ONE_HOUR = 60*ONE_MINUTE
        const val DAY = 24 * ONE_HOUR
        const val NEGATIVE_ONE = -1
        const val UNKNOWN_CATEGORY = -9
        const val HUNDRED=100
        const val TWO_HUNDRED=200
        const val ANIMATION_TWEEN = 700
    }

    object FloatConstants{
        const val ZERO = 0f
        const val ONE = 1f
        const val TWO = 2f
        const val THREE = 3f
        const val ZERO_TWO_FIVE = 0.25F
        const val ZERO_FIVE_FLOAT = 0.5F
        const val ZERO_SIX_FLOAT = 0.6F
        const val ZERO_SEVEN_FLOAT = 0.7F
        const val ZERO_ZERO_EIGHT_FLOAT = 0.08F
    }

    object DoubleConstants{
        const val ZERO=0.0
        const val POINT_ONE=0.1
        const val ONE=1.0
    }

    object LongConstants{
        const val ZERO = 0L
        const val FIVE = 5L
        const val SECOND = 1000L
    }

    object AnimationsConstants{
        const val fastAnimDurationMillis = 200
        const val animDurationMillis = 250
        const val animDurationSec_Millis = 1000
        const val PULSE_RATE = 1.2f

        const val PAGE_ANIM_DURATION = 500
        const val IMAGE_ANIMATION_DURATION=800

    }

    object StringConstants : ExtConstants() {
        const val EMPTY = ""
        const val NO_IMAGE_URL = "https://img.freepik.com/premium-vector/image-available-icon-set-default-missing-photo-stock-vector-symbol-black-filled-outlined-style-no-image-found-sign_268104-6708.jpg?semt=ais_hybrid&w=740"

    }

    object ExtRegexUtils {
        const val NON_EMPTY = "(.|\\s)*\\S(.|\\s)*"
        const val EMAIL_REGEX =
            "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)\$"
        const val PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\\^\$*.\\[\\]{}\\(\\)?\\-“!@#%&/,><\\’:;|_])\\S{8,99}\$"
        const val USERNAME_REGEX = "^(?=.*[a-zA-Z])[a-zA-Z0-9._]+\$"
        const val ONLY_LETTERS = "^[a-zA-Z ]+\$"
        const val ONLY_DIGIT =  "^[0-9]+\$"
        const val PHONE_REGEX = "^\\+?[1-9]\\d{1,14}\$"

        const val PASSWORD_PATTERN =  "/^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$/g"

        const val EMAIL_REGEX_WITH_SPECIAL_CHARACTER =
            "^([a-zA-Z0-9_\\-\\.\\+\\~]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)\$"
    }

}