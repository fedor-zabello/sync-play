package org.pigletsinc.syncplay.video.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class VideoController {
    @GetMapping("/watch")
    fun watch() : String {
        return "watch"
    }
}