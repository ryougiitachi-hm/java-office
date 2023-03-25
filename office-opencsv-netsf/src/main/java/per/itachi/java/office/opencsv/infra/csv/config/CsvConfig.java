package per.itachi.java.office.opencsv.infra.csv.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvConfig {

    @Bean
    @ConfigurationProperties("infra.csv.file")
    public Map<String, CsvFileProperties> mapCsvFileProperties() {
        return new HashMap<>();
    }
}
