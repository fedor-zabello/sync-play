package org.pigletsinc.syncplay.room.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RoomController {
    @GetMapping("/watch")
    fun watch(): String {
        return "watch"
    }
}