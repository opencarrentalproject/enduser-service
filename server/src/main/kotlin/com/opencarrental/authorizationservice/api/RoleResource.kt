package com.opencarrental.authorizationservice.api

import org.springframework.hateoas.RepresentationModel

data class RoleResource(val id: String, val name: String) : RepresentationModel<RoleResource>()