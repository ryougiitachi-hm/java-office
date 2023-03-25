package per.itachi.java.office.opencsv.infra.csv.opencsvnetsf;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import au.com.bytecode.opencsv.bean.MappingStrategy;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import per.itachi.java.office.opencsv.app.exception.InfraException;
import per.itachi.java.office.opencsv.infra.csv.CsvPort;
import per.itachi.java.office.opencsv.infra.csv.config.CsvFileProperties;

public class OpenCsvNetsfAdapter implements CsvPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenCsvNetsfAdapter.class);

    private static final Character DEFAULT_FLD_DELIMITER = ',';

    @Value("${infra.csv.mappingStrategy}")
    private MappingStrategyEnum mappingStrategyEnum;

    @Autowired
    private Map<String, CsvFileProperties> mapCsvFileProperties;

    /**
     * Just convenient to see variable values when debugging.
     * */
    @PostConstruct
    public void init() {
        LOGGER.info("");
    }

    @Override
    public <T> List<T> readRecordsFrom(String filePath, Class<T> beanClazz) {
        if (Files.notExists(Paths.get(filePath))) {
            LOGGER.error("The file {} doesn't exist. ", filePath);
            throw new InfraException(String.format("The file %s doesn't exist when parsing csv file. ", filePath));
        }
        CsvFileProperties csvFileProperties = mapCsvFileProperties.get(beanClazz.getSimpleName());
        if ((csvFileProperties == null)) {
            LOGGER.error("The csv config {} is not found. Please check the relevant config. ", beanClazz.getSimpleName());
            throw new InfraException(String.format("The csv config %s is not found. Please check the relevant config. ",
                    beanClazz.getSimpleName()));
        }
        LOGGER.info("Matched the relevant csv config, and started to parse the {}. ", beanClazz.getSimpleName());
        try(InputStreamReader isr = new FileReader(filePath);
            CSVReader csvReader = new CSVReader(isr, csvFileProperties.getFieldDelimiter())) {
            MappingStrategy<T> strategy = generateMappingStrategy(beanClazz, csvFileProperties);
            CsvToBean<T> csvToBean = new VerifiableCsvToBean<>();
//            CsvToBean<T> csvToBean = new CsvToBean<>();
            LOGGER.info("Parsing csv file {} with the format of {} ...", filePath, beanClazz.getSimpleName());
            List<T> result = csvToBean.parse(strategy, csvReader);
            LOGGER.info("Parsed csv file {} with the format of {} ...", filePath, beanClazz.getSimpleName());
            return result;
        }
        catch (IOException e) {
            LOGGER.error("Error occurred when parsing csv file {}. ", filePath, e);
            throw new InfraException("", e);
        }
    }

    @Override
    public <F, T> List<T> readRecordsFrom(String filePath, Class<F> fromBeanClazz, Class<T> toBeanClazz, Function<F, T> convertor) {
        if (Files.notExists(Paths.get(filePath))) {
            LOGGER.error("The file {} doesn't exist. ", filePath);
            throw new InfraException(String.format("The file %s doesn't exist when parsing csv file. ", filePath));
        }
        if (convertor == null) {
            LOGGER.error("The convertor function is required. ");
            throw new InfraException("The convertor function is required. ");
        }
        CsvFileProperties csvFileProperties = mapCsvFileProperties.get(fromBeanClazz.getSimpleName());
        if ((csvFileProperties == null)) {
            LOGGER.error("The csv config {} is not found. Please check the relevant config. ", fromBeanClazz.getSimpleName());
            throw new InfraException(String.format("The csv config %s is not found. Please check the relevant config. ",
                    fromBeanClazz.getSimpleName()));
        }
        LOGGER.info("Matched the relevant csv config, and started to parse the {}. ", fromBeanClazz.getSimpleName());
        try(InputStreamReader isr = new FileReader(filePath);
            CSVReader csvReader = new CSVReader(isr, csvFileProperties.getFieldDelimiter())) {
            MappingStrategy<F> strategy = generateMappingStrategy(fromBeanClazz, csvFileProperties);
            ConvertableCsvToBean<F, T> csvToBean = new ConvertableCsvToBean<>(convertor);
//            CsvToBean<T> csvToBean = new CsvToBean<>();
            LOGGER.info("Parsing csv file {} with the format of {}, and converting into {}...",
                    filePath, fromBeanClazz.getSimpleName(), toBeanClazz.getSimpleName());
            List<T> result = csvToBean.parseAndConvert(strategy, csvReader);
            LOGGER.info("Parsed csv file {} with the format of {}, and converting into {} ...",
                    filePath, fromBeanClazz.getSimpleName(), toBeanClazz.getSimpleName());
            return result;
        }
        catch (IOException e) {
            LOGGER.error("Error occurred when parsing csv file {}. ", filePath, e);
            throw new InfraException("", e);
        }
    }

    private <T> MappingStrategy<T> generateMappingStrategy(Class<T> beanClazz, CsvFileProperties csvFileProperties) {
        switch (this.mappingStrategyEnum) {
        case COLUMN_HEADER:
            HeaderColumnNameTranslateMappingStrategy<T> strategyHeader = new HeaderColumnNameTranslateMappingStrategy<>();
            strategyHeader.setType(beanClazz);
            strategyHeader.setColumnMapping(csvFileProperties.getColumnMappings());
            return strategyHeader;
        case COLUMN_POSITION:
        default:
            ColumnPositionMappingStrategy<T> strategyPosition = new ColumnPositionMappingStrategy<>();
            strategyPosition.setType(beanClazz);
            strategyPosition.setColumnMapping(csvFileProperties.getColumnFields().toArray(new String[0]));
            return strategyPosition;
        }
    }

    @Override
    public <T> void writeRecordsInto(String filePath, List<T> records, Class<T> beanClazz) {
        CsvFileProperties csvFileProperties = mapCsvFileProperties.get(beanClazz.getSimpleName());
        if ((csvFileProperties == null)) {
            LOGGER.error("The csv config {} is not found. Please check the relevant config. ", beanClazz.getSimpleName());
            throw new InfraException(String.format("The csv config %s is not found. Please check the relevant config. ",
                    beanClazz.getSimpleName()));
        }

        List<PropertyDescriptor> propertyDescriptorList = Collections.emptyList();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            propertyDescriptorList = Arrays.asList(propertyDescriptors);
        }
        catch (IntrospectionException e) {
            LOGGER.error("Failed to write {} into csv file {}. ", beanClazz, filePath, e);
            throw new InfraException("Failed to write into csv file. ", e);
        }

        try(//OutputStreamWriter osr = new FileWriter(filePath, StandardCharsets.UTF_8);
            OutputStreamWriter osr = new FileWriter(filePath);
            CSVWriter csvWriter = new CSVWriter(osr, csvFileProperties.getFieldDelimiter())) {
            LOGGER.info("Writing {} into csv file {} ...", beanClazz, filePath);
            // write headers
            csvWriter.writeNext(propertyDescriptorList.stream()
                    .map(PropertyDescriptor::getName)
                    .toArray(String[]::new));
            // write fields
            List<String> fields = new ArrayList<>();
            for (T record : records) {
                propertyDescriptorList.forEach(propertyDescriptor -> {
                    try {
                        fields.add(propertyDescriptor.getReadMethod().invoke(record).toString());
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        throw new InfraException("Failed to get property value. ", e);
                    }
                });
                csvWriter.writeNext(fields.toArray(new String[0]));
                fields.clear();
            }
        }
        catch (IOException e) {
            LOGGER.error("Error occurred when parsing csv file {}. ", filePath, e);
        }
    }

    protected enum MappingStrategyEnum {
        COLUMN_HEADER,
        COLUMN_POSITION;
    }
}
