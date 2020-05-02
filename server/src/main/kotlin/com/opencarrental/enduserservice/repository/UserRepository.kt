package com.opencarrental.enduserservice.repository

import com.opencarrental.enduserservice.domain.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {

    fun findByEmailAndPassword(email: String, password: String): User?

    fun findFirstByEmail(email: String): User?
}