package org.pigletsinc.syncplay.web.ui

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginPageController {

    @GetMapping("/login")
    fun getLoginPage() = "login"

    @GetMapping("/registration")
    fun showRegistrationForm() = "registration"
}
