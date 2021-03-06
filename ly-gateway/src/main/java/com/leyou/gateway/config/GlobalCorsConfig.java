package com.leyou.gateway.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/****
 * 配置Cors解决跨域问题
 */
@Configuration
public class GlobalCorsConfig {
        @Bean
    public CorsFilter corsFilter(){
        //1.添加CORS配置信息
        CorsConfiguration config=new CorsConfiguration();
        //1.1允许的域不要写*，否则cookie就无法使用了
        config.addAllowedOrigin("http://manage.leyou.com");
        //1.2是否发送cookie信息
        config.setAllowCredentials(true);

        //1.3允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");

        //1.4允许的头信息
        config.addAllowedHeader("*");
        //1.5有效时长
        config.setMaxAge(3600L);
        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource configSource=new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**",config);

        //3.返回新的CorsFilter
        return new CorsFilter(configSource);
    }

}
