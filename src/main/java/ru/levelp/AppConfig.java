package ru.levelp;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by кайрат on 02.01.2017.
 */
@Configuration
@ComponentScan(basePackages = {"ru.levelp"})
public class AppConfig {

    @Bean
    public Gson gson(){
        return new Gson();
    }
}
