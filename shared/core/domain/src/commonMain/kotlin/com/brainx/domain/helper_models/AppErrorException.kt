package com.brainx.domain.helper_models

import com.brainx.utils_extensions.constants.ExtConstants.StringConstants.EMPTY

data class AppErrorException(
     val errorMsg: String,
     val errCode: Int = 0,
     val errorLog: String?=EMPTY,
     val isSessionExpired:Boolean?=false
)
