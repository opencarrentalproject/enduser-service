package com.opencarrental.authorizationservice.service

import com.opencarrental.authorizationservice.domain.User
import io.konform.validation.ValidationResult

interface UserValidationService {

    fun validateUniqueEndUser(user: User): ValidationResult<User>

    fun validateUser(user: User): ValidationResult<User>
}