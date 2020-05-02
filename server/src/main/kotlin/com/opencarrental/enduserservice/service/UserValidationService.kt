package com.opencarrental.enduserservice.service

import com.opencarrental.enduserservice.domain.User
import io.konform.validation.ValidationResult

interface UserValidationService {

    fun validateUniqueEndUser(user: User): ValidationResult<User>

    fun validateUser(user: User): ValidationResult<User>
}