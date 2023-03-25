package per.itachi.java.office.opencsv.infra.csv.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvFileProperties {

    /**
     * When data type is char, neither "" nor ",,," pass the validation of springboot.
     * */
    private char fieldDelimiter;

    /**
     * key = column name in csv file, value = property name.
     * */
    private Map<String, String> columnMappings = Collections.emptyMap();

    /**
     * The usage of this property is to specify interested columns when loading data from csv file.
     * By default, all of columns are loaded into Java Bean if this property is not specified.
     * Sometimes, we may care about several columns instead of all columns.
     * In this case, we can specify this property, which will be set as null,
     * but columnFields is still required.
     * */
    private List<String> columnFields = Collections.emptyList();
}
