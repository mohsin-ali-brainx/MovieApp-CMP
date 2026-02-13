package com.brainx.utils_extensions.enums

enum class CoroutineDispatcherModuleEnums(val dispatcherName:String){
    DEFAULT("defaultDispatcher"),
    IO("ioDispatcher"),
    MAIN("mainDispatcher"),
    MAIN_IMMEDIATE("mainImmediateDispatcher"),
}