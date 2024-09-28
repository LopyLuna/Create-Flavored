architectury.forge()

loom {
    val common = project(":common")
    accessWidenerPath = common.loom.accessWidenerPath

    forge {
        mixinConfig("create_dd-common.mixins.json")
        mixinConfig("create_dd.mixins.json")

        convertAccessWideners = true
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
    }
}

dependencies {
    forge("net.minecraftforge:forge:${"minecraft_version"()}-${"forge_version"()}")

    // Create and its dependencies
    modImplementation("com.simibubi.create:create-${"minecraft_version"()}:${"create_forge_version"()}:slim") { isTransitive = false }
    modImplementation("com.tterrag.registrate:Registrate:${"registrate_forge_version"()}")
    modImplementation("com.jozufozu.flywheel:flywheel-forge-${"minecraft_version"()}:${"flywheel_forge_version"()}")

    // Development QOL
//    modLocalRuntime("mezz.jei:jei-${minecraft_version}-forge:${jei_forge_version}")

    // if you would like to add integration with JEI, uncomment this line.
//    modCompileOnly("mezz.jei:jei-${minecraft_version}:${jei_forge_version}:api")

    compileOnly("io.github.llamalad7:mixinextras-common:${"mixin_extras_version"()}")
    annotationProcessor(implementation(include("io.github.llamalad7:mixinextras-forge:${"mixin_extras_version"()}")!!)!!)

    modApi("dev.architectury:architectury-forge:${"architectury_version"()}") { isTransitive = false }
}

operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
        ?: throw IllegalStateException("Property $this is not defined")
}
