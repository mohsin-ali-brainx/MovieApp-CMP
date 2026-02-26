package com.brainx.movie_app.app_config

import com.brainx.movie_app.BuildConfig


actual object ApiSecrets {
    actual val movieApiKey: String
        get() = BuildConfig.TMDB_API_KEY
    actual val movieAccessToken: String
        get() = BuildConfig.TMDB_ACCESS_TOKEN
}