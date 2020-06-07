package com.opencarrental.authorizationservice.service

import com.opencarrental.authorizationservice.api.RoleEdit
import com.opencarrental.authorizationservice.domain.Role

interface RoleService {

    fun create(role: Role): Role

    fun retrieve(id: String): Role?

    fun update(id: String, roleEdit: RoleEdit): Role

    fun delete(id: String)

    fun list(): List<Role>
}