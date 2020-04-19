package com.opencarrental.enduserservice.service

import com.opencarrental.enduserservice.domain.EndUser
import io.konform.validation.ValidationResult

interface EndUserValidationService {

    fun validateUniqueEndUser(endUser: EndUser): ValidationResult<EndUser>

    fun validateUser(endUser: EndUser): ValidationResult<EndUser>
}