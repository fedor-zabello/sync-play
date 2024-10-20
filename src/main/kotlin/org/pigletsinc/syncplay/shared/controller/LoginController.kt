package org.pigletsinc.syncplay.shared.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {
    @GetMapping("/login")
    fun getLoginPage() = "login"

    @GetMapping("/registration")
    fun showRegistrationForm() = "registration"
}
