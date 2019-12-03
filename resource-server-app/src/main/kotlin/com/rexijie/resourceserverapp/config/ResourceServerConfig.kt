package com.rexijie.resourceserverapp.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices

@Configuration
@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter() {

    @Value("\$security.oauth2.resource.id")
    final lateinit var resourceId: String

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources!!.resourceId(resourceId)
                .stateless(false)
    }

    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
    }
}