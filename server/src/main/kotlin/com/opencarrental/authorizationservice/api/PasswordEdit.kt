package com.opencarrental.authorizationservice.api

data class PasswordEdit(
        val currentPassword: String,
        val newPassword: String
)