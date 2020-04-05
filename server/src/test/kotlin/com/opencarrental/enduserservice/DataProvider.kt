package com.opencarrental.enduserservice

import com.opencarrental.enduserservice.domain.EndUser
import com.opencarrental.enduserservice.repository.EndUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class DataProvider(@Autowired val repository: EndUserRepository) {

    fun createUsers() {
        repository.save(EndUser(password = "123456", firstName = "test1", lastName = "test2", email = "test1@example.com"))
        repository.save(EndUser(password = "123456", firstName = "test2", lastName = "test2", email = "test2@example.com"))
        repository.save(EndUser(password = "123456", firstName = "test3", lastName = "test3", email = "test3@example.com"))
    }
}