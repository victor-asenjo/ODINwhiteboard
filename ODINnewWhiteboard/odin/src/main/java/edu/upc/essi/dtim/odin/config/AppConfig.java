package edu.upc.essi.dtim.odin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${dataStorage.DataBaseType}")
    public String DBTypeProperty;

}
