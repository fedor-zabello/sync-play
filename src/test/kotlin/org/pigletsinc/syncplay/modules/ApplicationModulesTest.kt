package org.pigletsinc.syncplay.modules

import org.pigletsinc.syncplay.SyncPlayApplication
import org.springframework.modulith.core.ApplicationModules
import kotlin.test.Test

class ApplicationModulesTest {
    @Test
    fun verifiesModularStructure() {
        val modules = ApplicationModules.of(SyncPlayApplication::class.java)
        modules.verify()
    }
}