package io.github.mainyf.mc_manager

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
class MCManagerConfiguration

@SpringBootApplication
@EnableScheduling
class MCManagerApplication : SpringBootServletInitializer() {

    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
        return builder.sources(MCManagerApplication::class.java)
    }

}

//class Application {
//
//    private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
//
//    private var runtimeDir: File = File("")
//
//    private var configData: Config? = null
//
//    val appConfig: Config get() = configData!!
//
//    fun init(args: Array<String>) {
//        loadConfig()
//    }
//
//    private fun loadConfig() {
//        val configFile = runtimeDir.resolve("config.yml")
//        if (!configFile.exists()) {
//            getJarInResourceAsStream("/config.yml").run {
//                IOUtils.copy(
//                    this,
//                    configFile.outputStream()
//                )
//            }
//        }
//        configFile.inputStream().use {
//            configData = mapper.readValue<Config>(it)
//        }
//    }
//
//    private fun getJarInResourceAsStream(path: String): InputStream {
//        return this::class.java.getResourceAsStream(path)
//    }
//
//}

fun main(args: Array<String>) {
    runApplication<MCManagerApplication>(*args)
}
