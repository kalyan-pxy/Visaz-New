package com.pxy.visaz.data.local


import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.pxy.visaz.core.model.User

object AppPreferenceHelper {

    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    private lateinit var securePreferences: SharedPreferences

    private const val SECURE_PREFS_NAME = "visaz_prefs"
    private const val USER_DATA = "userData"

    fun createSharedPreferences(context: Context) {
        securePreferences = EncryptedSharedPreferences.create(
            SECURE_PREFS_NAME,
            mainKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    var user: User?
        get() = if (securePreferences.contains(USER_DATA)) {
            try {
                val userData = securePreferences.getString(USER_DATA, null)
                Gson().fromJson(userData, User::class.java)
            } catch (e: Exception) {
                null
            }
        } else null
        set(flag) {
            with(securePreferences.edit()) {
                putString(USER_DATA, Gson().toJson(flag))
                apply()
            }
        }

    fun clearUser() {
        with(securePreferences.edit()) {
            putString(USER_DATA, "")
            apply()
        }
    }

    fun logout() {
        with(securePreferences.edit()) {
            clear()
            clear()
            apply()
        }
    }
}