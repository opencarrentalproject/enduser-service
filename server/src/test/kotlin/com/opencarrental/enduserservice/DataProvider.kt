package com.opencarrental.enduserservice

import com.opencarrental.enduserservice.domain.User
import com.opencarrental.enduserservice.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class DataProvider(@Autowired val repository: UserRepository) {

    fun createUsers() {
        repository.save(User(password = "123456", firstName = "test1", lastName = "test2", email = "test1@example.com"))
        repository.save(User(password = "123456", firstName = "test2", lastName = "test2", email = "test2@example.com"))
        repository.save(User(password = "123456", firstName = "test3", lastName = "test3", email = "test3@example.com"))
    }

    fun createUser(firstName: String, lastName: String, email: String, password: String): User =
            repository.save(User(firstName = firstName, lastName = lastName, email = email, password = password))

}