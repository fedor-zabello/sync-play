package org.pigletsinc.syncplay

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SyncPlayApplication

fun main(args: Array<String>) {
    runApplication<SyncPlayApplication>(*args)
}
