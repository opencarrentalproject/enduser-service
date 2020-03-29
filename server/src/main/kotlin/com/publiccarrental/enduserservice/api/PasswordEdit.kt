package com.publiccarrental.enduserservice.api

data class PasswordEdit(
        val currentPassword: String,
        val newPassword: String
)