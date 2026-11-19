plugins {
    alias(libs.plugins.android.application)
}

android {
<<<<<<< HEAD
    namespace = "com.example.calcsy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.calcsy"
        minSdk = 26
        targetSdk = 35
=======
    namespace = "com.example.kalkulator"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.kalkulator"
        minSdk = 24
        targetSdk = 37
>>>>>>> d2044fd5255eebadcd8e2d8834efe90239c4ff21
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
<<<<<<< HEAD
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
=======
        release {
            optimization {
                enable = false
            }
>>>>>>> d2044fd5255eebadcd8e2d8834efe90239c4ff21
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
<<<<<<< HEAD
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
=======
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
>>>>>>> d2044fd5255eebadcd8e2d8834efe90239c4ff21
