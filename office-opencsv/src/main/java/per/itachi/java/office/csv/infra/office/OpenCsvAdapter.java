package per.itachi.java.office.csv.infra.office;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OpenCsvAdapter implements CsvPort {

    @Autowired
    private Charset charset;

    /**
     * @param csvInputFile the path of csv input file.
     * */
    public <T> List<T> readCsv(String csvInputFile, Class<T> clazzInputFile) {
        try(InputStreamReader isr = new InputStreamReader(new FileInputStream(csvInputFile), charset)) {
            CsvToBeanBuilder<T> builder = new CsvToBeanBuilder<>(isr);
            CsvToBean<T> csvToBean = builder.withType(clazzInputFile).build();
            List<T> list = csvToBean.parse();
            return list;
        }
        catch (IOException e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }

    /**
     * Hasn't been verified.
     * */
    @Override
    public <T> void writeCsv(String csvOutputFile, Class<T> clazzInputFile, List<T> outputList) {
        Map<String, Integer> columnOrderMap = new HashMap<>();

        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setColumnOrderOnWrite(Comparator.comparingInt(column -> columnOrderMap.getOrDefault(column, 0)));
        strategy.setType(clazzInputFile);

        try(OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(csvOutputFile), charset)) {
            StatefulBeanToCsvBuilder<T> builder = new StatefulBeanToCsvBuilder<T>(osw);
            StatefulBeanToCsv<T> beanToCsv = builder
                    .withMappingStrategy(strategy)
                    .withApplyQuotesToAll(false) //
                    .build();
            beanToCsv.write(outputList);
        }
        catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error("", e);
        }
    }
}
