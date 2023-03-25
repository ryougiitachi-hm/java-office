package per.itachi.java.office.csv.infra.office.config;

import java.nio.charset.Charset;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenCsvConfig {

    @Bean
    @ConfigurationProperties("infra.office.csv.opencsv")
    public OpenCsvProperties openCsvProperties() {
        return new OpenCsvProperties();
    }

    @Bean
    public Charset opencsvCharset(OpenCsvProperties openCsvProperties) {
        return Charset.forName(openCsvProperties.getCharset());
    }
}
