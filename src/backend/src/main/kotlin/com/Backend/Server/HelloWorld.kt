package com.Backend.Server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class HelloWorld {
    @GetMapping
    fun hellowWorld(): String = "HelloWorld"
}
