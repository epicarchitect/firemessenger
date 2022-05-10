plugins {
    id("com.android.application") version "7.2.0" apply false
    id("com.android.library") version "7.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("org.jetbrains.kotlin.jvm") version "1.6.10" apply false
    id("com.google.gms.google-services") version "4.3.10" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}