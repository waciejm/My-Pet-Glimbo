// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.jetbrainsKotlinSerialization) apply false
    alias(libs.plugins.jetbrainsComposeCompiler) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.ksp) apply false
}