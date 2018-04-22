/*
 * Copyright (C) 2018 The Smash Ks Open Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright (C) 2018 The Smash Ks Open Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    extra["kotlin_version"] = "1.2.40"

    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra["kotlin_version"]}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files

        classpath("com.google.gms:google-services:3.2.0") // google-services plugin
    }
}

allprojects {
    if (File("/volumes/ramdisk").exists()) {
        rootProject.buildDir = File("/volumes/ramdisk/Android/${rootProject.name}/${project.name}")
    }

    repositories {
        google()
        jcenter()
        mavenCentral()
        // required to find the project's artifacts
        maven("https://www.jitpack.io")
        maven("https://dl.bintray.com/pokk/maven")
        maven("https://dl.bintray.com/kodein-framework/Kodein-DI/")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
