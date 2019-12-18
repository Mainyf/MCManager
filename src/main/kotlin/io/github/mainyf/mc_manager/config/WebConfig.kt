package io.github.mainyf.mc_manager.config

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleModule
import io.github.mainyf.mc_manager.interceptors.JWTHandlerInterceptor
import io.github.mainyf.mc_manager.internal.HttpStatusDeserializer
import io.github.mainyf.mc_manager.internal.HttpStatusSerializer
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    @Bean
    fun jwtHandlerInterceptor() = JWTHandlerInterceptor()

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(jwtHandlerInterceptor())
            .addPathPatterns("/**")
    }

    @Bean
    fun addJackJsonModule(): Module {
        return SimpleModule().apply {
            addDeserializer(HttpStatus::class.java, HttpStatusDeserializer())
            addSerializer(HttpStatus::class.java, HttpStatusSerializer())
        }
    }

//    @Bean
//    fun dispatcherServlet() = DispatcherServlet()
//
//    @Bean
//    fun dispatcherServletRegistration() = DispatcherServletRegistrationBean(dispatcherServlet(), "/api/").apply {
//        setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME)
//    }

}

@Component
class BeanTools : ApplicationContextAware {

    companion object {
        lateinit var context: ApplicationContext

        inline fun <reified T> getBean(): T {
            return context.getBean(T::class.java)
        }

        fun <T> getBean(clazz: Class<T>): T {
            return context.getBean(clazz)
        }

    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

}