package com.opencarrental.enduserservice.service

import com.opencarrental.enduserservice.api.EndUserEdit
import com.opencarrental.enduserservice.domain.EndUser


interface EndUserService {

    fun create(endUser: EndUser): EndUser

    fun retrieve(id: String): EndUser?

    fun update(id: String, endUserEdit: EndUserEdit): EndUser

    fun delete(id: String)

    fun list(): List<EndUser>
}