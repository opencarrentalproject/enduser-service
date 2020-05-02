package com.opencarrental.enduserservice.service

import com.opencarrental.enduserservice.domain.User
import com.opencarrental.enduserservice.repository.UserRepository
import io.konform.validation.Validation
import io.konform.validation.ValidationBuilder
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.minLength
import org.springframework.stereotype.Service


@Service
class UserValidationServiceImpl(val repository: UserRepository) : UserValidationService {

    private val validate = Validation<User> {
        User::password.has.minLength(8)
        User::email required {
            matches(".+@.+".toRegex())
        }
    }

    private val validateUniqueEmail = Validation<User> {
        hasUniqueEmail()
    }

    override fun validateUniqueEndUser(user: User): ValidationResult<User> = validateUniqueEmail(user)

    override fun validateUser(user: User): ValidationResult<User> = validate(user)


    private fun ValidationBuilder<String>.matches(regex: Regex) =
            addConstraint("must have correct format") { it.contains(regex) }

    private fun ValidationBuilder<User>.hasUniqueEmail() =
            addConstraint("email must be unique") {
                repository.findFirstByEmail(it.email)?.let { false } ?: true
            }
}