package openidconnectclient;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.context.WebApplicationContext;
import openidconnectclient.Model.BaseSessionInformation;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class AppSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .authorizeRequests(authorizeRequests ->
                authorizeRequests.mvcMatchers("/**")
                                 .permitAll()
                                 .anyRequest()
                                 .authenticated()
            )
            .oauth2Login(oauthLogin -> oauthLogin.permitAll())
            .logout();
    }
    

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public BaseSessionInformation baseSessionInformation() {
        return new BaseSessionInformation();
    }

}