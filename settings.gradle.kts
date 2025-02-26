rootProject.name = "Bamb"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
//        maven { url=uri ("https://www.jitpack.io")}
//        maven { url=uri ("https://maven.aliyun.com/repository/public")}
//        maven { url=uri ("https://maven.aliyun.com/repository/releases")}
//        maven { url=uri ("https://repo.huaweicloud.com/repository/maven")}
//        maven { url=uri ("https://maven.aliyun.com/repository/google")}
//        maven { url=uri ("https://maven.aliyun.com/repository/central")}
//        maven { url=uri ("https://maven.aliyun.com/repository/gradle-plugin")}
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
//        maven { url=uri ("https://www.jitpack.io")}
//        maven { url=uri ("https://maven.aliyun.com/repository/public")}
//        maven { url=uri ("https://maven.aliyun.com/repository/releases")}
//        maven { url=uri ("https://repo.huaweicloud.com/repository/maven")}
//        maven { url=uri ("https://maven.aliyun.com/repository/google")}
//        maven { url=uri ("https://maven.aliyun.com/repository/central")}
//        maven { url=uri ("https://maven.aliyun.com/repository/gradle-plugin")}
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")