

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.start2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.start2"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    implementation("io.ktor:ktor-client-core:1.6.6")
    implementation("io.ktor:ktor-client-json:1.6.6")
    implementation("io.ktor:ktor-client-serialization:1.6.6")
    implementation("io.ktor:ktor-client-logging:1.6.6")
    implementation ("com.spotify.android:auth:1.2.5")
    implementation ("androidx.compose.ui:ui:1.0.0")
    implementation ("androidx.compose.material:material:1.0.0")
    implementation ("androidx.activity:activity-compose:1.3.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")

    implementation ("com.google.dagger:hilt-android:2.38.1")
    implementation("androidx.core:core-ktx:+")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    annotationProcessor ("com.google.dagger:hilt-android-compiler:2.38.1")
    implementation ("androidx.compose.ui:ui:1.5.4")
    implementation ("androidx.compose.material3:material3:1.1.2")
    implementation ("androidx.compose.ui:ui:1.6.0-beta01")
    implementation ("androidx.compose.material:material:1.6.0-beta01")
    implementation ("androidx.compose.ui:ui-tooling:1.6.0-beta01")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0-beta01")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4") // or the latest version
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")


    implementation("com.github.tehras:charts:0.2.4-alpha")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")

    implementation ("androidx.core:core-ktx:1.12.0")  // Use the appropriate version
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation ("com.android.volley:volley:1.2.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("com.google.accompanist:accompanist-pager:0.32.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation ("androidx.compose.material:material:")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("io.coil-kt:coil-compose:1.3.2")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.patrykandpatrick.vico:compose:1.12.0")
    implementation("com.patrykandpatrick.vico:core:1.12.0")
    implementation("me.onebone:toolbar-compose:2.3.5")
    implementation ("com.spotify.android:auth:1.2.5") // Maven dependency

    implementation ("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation ("com.github.kittinunf.fuel:fuel-android:2.3.1")
    // All other dependencies for your app should also be here:
    implementation ("androidx.browser:browser:1.6.0")
    testImplementation ("androidx.compose.ui:ui-test-junit4:1.5.4")

    androidTestImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation ("org.mockito:mockito-core:4.0.0")
    testImplementation ("org.mockito:mockito-inline:4.0.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")
    implementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation ("org.mockito:mockito-android:3.11.0")
    androidTestImplementation ("org.mockito:mockito-android:3.11.0")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1")
    androidTestImplementation ("org.mockito:mockito-android:3.11.0")
    testImplementation ("io.mockk:mockk:1.12.0")
    testImplementation ("org.robolectric:robolectric:4.9.1")
    androidTestImplementation ("org.robolectric:robolectric:4.9.1")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Choose one of the following:
    // Material Design 3
    implementation("androidx.compose.material3:material3")
    // or Material Design 2
    implementation("androidx.compose.material:material")
    // or skip Material Design and build directly on top of foundational components
    implementation("androidx.compose.foundation:foundation")
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    implementation("androidx.compose.ui:ui")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    implementation("androidx.compose.material:material-icons-core")
    // Optional - Add full set of material icons
    implementation("androidx.compose.material:material-icons-extended")
    // Optional - Add window size utils
    implementation("androidx.compose.material3:material3-window-size-class")

    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.7.2")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
    // Optional - Integration with RxJava
    implementation("androidx.compose.runtime:runtime-rxjava2")

    val nav_version = "2.7.5"

    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation ("com.alexstyl.swipeablecard:swipeablecard:0.1.0")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation ("io.coil-kt:coil-compose:2.2.2")
    
    // For Jetpack Compose.
    implementation("com.patrykandpatrick.vico:compose:1.12.0")

    // For `compose`. Creates a `ChartStyle` based on an M2 Material Theme.
    implementation("com.patrykandpatrick.vico:compose-m2:1.12.0")

    // For `compose`. Creates a `ChartStyle` based on an M3 Material Theme.
    implementation("com.patrykandpatrick.vico:compose-m3:1.12.0")

    // Houses the core logic for charts and other elements. Included in all other modules.
    implementation("com.patrykandpatrick.vico:core:1.12.0")

    // For the view system.
    implementation("com.patrykandpatrick.vico:views:1.12.0")

}



