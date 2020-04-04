package com.opencarrental.enduserservice.api

data class PasswordEdit(
        val currentPassword: String,
        val newPassword: String
)