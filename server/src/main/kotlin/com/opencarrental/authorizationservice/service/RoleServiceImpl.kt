package com.opencarrental.authorizationservice.service

import com.opencarrental.authorizationservice.api.RoleEdit
import com.opencarrental.authorizationservice.domain.Role
import com.opencarrental.authorizationservice.exception.RoleNotFoundException
import com.opencarrental.authorizationservice.repository.RoleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(val roleRepository: RoleRepository) : RoleService {

    override fun create(role: Role): Role = roleRepository.save(role)

    override fun retrieve(id: String): Role? = roleRepository.findByIdOrNull(id)


    override fun update(id: String, roleEdit: RoleEdit): Role {
        val persisted: Role = roleRepository.findByIdOrNull(id) ?: throw RoleNotFoundException("Role does not exist")
        val updated = persisted.copy(name = roleEdit.name)
        return roleRepository.save(updated)
    }

    override fun delete(id: String) {
        val persisted: Role = roleRepository.findByIdOrNull(id) ?: throw RoleNotFoundException("Role does not exist")
        roleRepository.delete(persisted)
    }

    override fun list(): List<Role> = roleRepository.findAll()
}