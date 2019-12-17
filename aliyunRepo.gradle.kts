import java.util.Properties
import java.io.File

val prop = Properties()

prop.load((File(System.getProperty("user.home"), "private_repo.properties")).inputStream())

val repoUsername = prop.getProperty("username")
val repoPassword = prop.getProperty("password")

val releaseRepo = "https://repo.rdc.aliyun.com/repository/113475-release-H8vJTT/repository/releases/"
val snapshotRepo = "https://repo.rdc.aliyun.com/repository/113475-snapshot-qjOYCE/repository/releases/"

repositories {
    maven {
        url = uri("https://maven.aliyun.com/repository/public")
    }
    maven {
        credentials {
            username = repoUsername
            password = repoPassword
        }
        url = uri(releaseRepo)
    }
    maven {
        credentials {
            username = repoUsername
            password = repoPassword
        }
        url = uri(snapshotRepo)
    }
//    jcenter()
//    mavenCentral()
    mavenLocal()
}