package per.itachi.java.office.csv.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import per.itachi.java.office.csv.app.service.CsvService;
import per.itachi.java.office.csv.app.service.CsvServiceImpl;

@Configuration
public class ServiceConfig {

    @Bean
    public CsvService csvService() {
        return new CsvServiceImpl();
    }
}
