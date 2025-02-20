/*
 * Copyright (c) 2022 Proton AG
 *
 * This file is part of Proton Mail.
 *
 * Proton Mail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Mail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Mail. If not, see https://www.gnu.org/licenses/.
 */

buildscript {
    initVersions()
    repositories(repos)
    dependencies(classpathDependencies)
}

// Room 2.3 use a jdbc not compatible with arm so use the updated one to support
// arm build. Room 2.4 should fix this issue (not stable yet)
allprojects {
    configurations.all {
        resolutionStrategy {
            force("org.xerial:sqlite-jdbc:3.34.0")
        }
    }
}

plugins {
    id("me.proton.kotlin") version "0.1" // Released: Oct 09, 2020
    id("me.proton.tests") version "0.1" // Released: Oct 09, 2020
    id("me.proton.core.gradle-plugins.detekt") version "1.1.2"
    id("xyz.mishkun.lobzik") version "0.6.0"
    `sonarQube`
}

lobzik {
    monolithModule.set(":app")
    packagePrefix.set("ch.protonmail.android")
    this.variantName.set("betaDebug")
    ignoredClasses.addAll(
        ".*UserManager$",
        ".*Constants$",
        ".*ProtonMailApiManager$",
        ".*Util.*",
        ".*ProtonMailApplication$",
        ".*ResponseBody$",
        "Base.*",
        ".*Module",
        "^Message$",
        "^User$",
        "^ProtonMailApi$"
    )
}

allprojects {
    repositories(repos)
}

kotlinCompilerArgs(
    "-Xuse-experimental=kotlin.Experimental",
    // Enables unsigned types, like `UInt`, `ULong`, etc
    "-Xopt-in=kotlin.ExperimentalUnsignedTypes",
    // Enables experimental Coroutines from coroutines-test artifact, like `runBlockingTest`
    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    // Enables experimental kotlin.time
    "-Xopt-in=kotlin.time.ExperimentalTime"
)

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
