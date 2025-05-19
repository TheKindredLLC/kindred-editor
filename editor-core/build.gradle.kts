plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "editor-core"
            isStatic = true
        }
    }
    js(IR) {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                // e.g. kotlinx.serialization, emoji registry, etc.
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}
