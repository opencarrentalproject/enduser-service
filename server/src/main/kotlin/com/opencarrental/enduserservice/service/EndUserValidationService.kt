package com.opencarrental.enduserservice.service

import com.opencarrental.enduserservice.domain.EndUser
import io.konform.validation.ValidationResult

interface EndUserValidationService {

    fun validateUser(endUser: EndUser): ValidationResult<EndUser>
}