package com.brainx.movie_app.app_config

import platform.Foundation.NSBundle

actual object ApiSecrets {
    actual val movieApiKey: String
        get() = NSBundle.mainBundle
            .objectForInfoDictionaryKey("TMDB_API_KEY") as String
    actual val movieAccessToken: String
        get() = NSBundle.mainBundle
            .objectForInfoDictionaryKey("TMDB_ACCESS_TOKEN") as String

}