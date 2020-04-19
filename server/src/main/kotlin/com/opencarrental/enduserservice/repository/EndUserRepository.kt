package com.opencarrental.enduserservice.repository

import com.opencarrental.enduserservice.domain.EndUser
import org.springframework.data.mongodb.repository.MongoRepository

interface EndUserRepository : MongoRepository<EndUser, String> {

    fun findByEmailAndPassword(email: String, password: String): EndUser?

    fun findFirstByEmail(email: String): EndUser?
}