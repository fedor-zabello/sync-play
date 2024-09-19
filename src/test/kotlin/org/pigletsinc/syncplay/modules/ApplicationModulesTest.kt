package org.pigletsinc.syncplay.modules

import org.pigletsinc.syncplay.SyncPlayApplication
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter
import kotlin.test.Test


class ApplicationModulesTest {
    @Test
    fun verifiesModularStructure() {
        val modules = ApplicationModules.of(SyncPlayApplication::class.java)
        modules.verify()
    }

    @Test
    fun createModuleDocumentation() {
        val modules: ApplicationModules = ApplicationModules.of(SyncPlayApplication::class.java)
        Documenter(modules)
            .writeDocumentation()
            .writeIndividualModulesAsPlantUml()
    }
}