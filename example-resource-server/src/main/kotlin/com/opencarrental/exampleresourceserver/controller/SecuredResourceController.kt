package com.opencarrental.exampleresourceserver.controller

import com.opencarrental.exampleresourceserver.api.Item
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/items")
class SecuredResourceController {

    @GetMapping("/{id}")
    fun retrieveItem(principal: Principal, @PathVariable("id") id: String): Item = Item(id, """secured information call by ${principal.name} ${SecurityContextHolder.getContext().authentication.authorities} """)


    @PostMapping("/{id}")
    fun writeItem(principal: Principal, @PathVariable("id") id: String, @RequestBody text: String): Item = Item(id, """$text updated by ${principal.name}""")
}