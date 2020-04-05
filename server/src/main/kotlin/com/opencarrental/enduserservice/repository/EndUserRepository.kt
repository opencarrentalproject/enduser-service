package com.opencarrental.enduserservice.repository

import com.opencarrental.enduserservice.domain.EndUser
import org.springframework.data.repository.CrudRepository

interface EndUserRepository : CrudRepository<EndUser, String> {

    fun findByEmailAndPassword(email: String, password: String): EndUser?

    fun findAllOrderByEmail(): Iterable<EndUser>
}