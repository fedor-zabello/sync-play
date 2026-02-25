package org.pigletsinc.syncplay.modules

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import org.pigletsinc.syncplay.SyncPlayApplication
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter
import kotlin.test.Test

class ApplicationModulesTest {

    private val root = "org.pigletsinc.syncplay"
    private val classes: JavaClasses by lazy { ClassFileImporter().importPackages(root) }

    @Test
    fun verifiesLayerDependencies() {
        layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("web").definedBy("$root.web..")
            .layer("application").definedBy("$root.application..")
            .layer("domain").definedBy("$root.domain..")
            .layer("repository").definedBy("$root.repository..")
            .whereLayer("web").mayOnlyAccessLayers("application")
            .whereLayer("application").mayOnlyAccessLayers("domain")
            .whereLayer("repository").mayOnlyAccessLayers("domain")
            .whereLayer("domain").mayNotAccessAnyLayer()
            .check(classes)
    }

    @Test
    fun createModuleDocumentation() {
        val modules: ApplicationModules = ApplicationModules.of(SyncPlayApplication::class.java)
        Documenter(modules)
            .writeDocumentation()
            .writeIndividualModulesAsPlantUml()
    }
}
