package com.opencarrental.enduserservice.service

import com.opencarrental.enduserservice.domain.EndUser
import com.opencarrental.enduserservice.repository.EndUserRepository
import io.konform.validation.Validation
import io.konform.validation.ValidationBuilder
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.minLength
import org.springframework.stereotype.Service


@Service
class EndUserValidationServiceImpl(val repository: EndUserRepository) : EndUserValidationService {

    private val validate = Validation<EndUser> {
        EndUser::password.has.minLength(8)
        EndUser::email required {
            matches(".+@.+".toRegex())
        }
    }

    private val validateUniqueEmail = Validation<EndUser> {
        hasUniqueEmail()
    }

    override fun validateUniqueEndUser(endUser: EndUser): ValidationResult<EndUser> = validateUniqueEmail(endUser)

    override fun validateUser(endUser: EndUser): ValidationResult<EndUser> = validate(endUser)


    private fun ValidationBuilder<String>.matches(regex: Regex) =
            addConstraint("must have correct format") { it.contains(regex) }

    private fun ValidationBuilder<EndUser>.hasUniqueEmail() =
            addConstraint("email must be unique") {
                repository.findFirstByEmail(it.email)?.let { false } ?: true
            }
}