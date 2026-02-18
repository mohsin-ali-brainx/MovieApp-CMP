package com.brainx.ktor_network.core.error_handling

import com.brainx.ktor_network.utils.contants.NetworkConstants.EMPTY
import com.brainx.ktor_network.utils.contants.NetworkConstants.ERROR_400
import com.brainx.ktor_network.utils.contants.NetworkConstants.ERROR_OCCURRED
import com.brainx.ktor_network.utils.contants.NetworkConstants.SESSION_EXPIRED_CODE

class AppException : Exception {

     var errorMsg: String
     var errCode: Int = 0
     var errorLog: String?=EMPTY
     var isSessionExpired:Boolean?=false

    constructor(errCode: Int?=null, error: String?, errorLog: String? = "") : super(error) {
        this.errorMsg = error ?: ERROR_OCCURRED
        this.errCode = errCode ?: ERROR_400
        this.errorLog = errorLog?:this.errorMsg
        this.isSessionExpired = errCode == SESSION_EXPIRED_CODE
    }

    constructor(errCode: Int?=null, error: String?) : super(error) {
        this.errorMsg = error ?: ERROR_OCCURRED
        this.errCode = errCode ?: ERROR_400
        this.isSessionExpired = errCode == SESSION_EXPIRED_CODE
    }

    constructor(error: Errors, e: Throwable?) {
        errCode = error.getKey()
        errorMsg = error.getValue()
        errorLog = e?.message
        isSessionExpired = error.getKey()== SESSION_EXPIRED_CODE
    }
}