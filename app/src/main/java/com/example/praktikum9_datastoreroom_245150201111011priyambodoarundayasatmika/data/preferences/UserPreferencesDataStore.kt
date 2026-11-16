package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

internal val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)
