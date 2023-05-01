package edu.upc.essi.dtim.odin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Getter
@Configuration
@EnableJpaRepositories("edu.upc.essi.dtim.odin.NextiaStore.ORMStore")
@EnableTransactionManagement
public class AppConfig {
    @Value("${dataStorage.DataBaseType}")
    public String DBTypeProperty;

    @Value("${dataStorage.diskPath}")
    public String diskPath;

    @Value("${dataStorage.JenaPath}")
    private String JenaPath;
}
