plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.tradingjournal"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.tradingjournal"
        minSdk = 26
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("com.github.PhilJay:MPAndroidChart:v3.0.3")
    implementation(files("libs/poishadow-all.jar"))
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation("com.github.ybq:Android-SpinKit:1.4.0")
    implementation("com.itextpdf:itext7-core:8.0.1")
    implementation("com.squareup.picasso:picasso:2.71828")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")
    implementation ("androidx.core:core-splashscreen:1.0.0")
    // ViewModel
    annotationProcessor("androidx.room:room-compiler:2.5.2")
 //....excel
//    implementation ("org.apache.poi:poi:5.2.2")
//    implementation ("org.apache.poi:poi-ooxml:5.2.2")

}