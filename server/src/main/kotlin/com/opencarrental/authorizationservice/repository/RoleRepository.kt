package com.opencarrental.authorizationservice.repository

import com.opencarrental.authorizationservice.domain.Role
import org.springframework.data.mongodb.repository.MongoRepository

interface RoleRepository : MongoRepository<Role, String> {
}