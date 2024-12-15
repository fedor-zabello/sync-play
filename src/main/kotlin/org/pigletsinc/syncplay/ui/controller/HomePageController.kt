package org.pigletsinc.syncplay.ui.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomePageController {
    @GetMapping("/home")
    fun getHomePage() = "home"

    @GetMapping("/youtube-iframe")
    fun getYoutubeIframe() = "youtube-iframe"
}
