package com.publiccarrental.enduserservice.repository

import com.publiccarrental.enduserservice.domain.EndUser
import org.springframework.data.repository.CrudRepository
import java.util.*

interface EndUserRepository : CrudRepository<EndUser, UUID> {

    fun findByEmailAndPassword(email: String, password: String): EndUser?

    fun findByLoginNameAndPassword(loginName: String, password: String): EndUser?

    fun findAllOrderByLogin(): Iterable<EndUser>
}