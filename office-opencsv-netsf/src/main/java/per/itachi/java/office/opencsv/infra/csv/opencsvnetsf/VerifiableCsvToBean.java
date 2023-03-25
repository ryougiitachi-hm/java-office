package per.itachi.java.office.opencsv.infra.csv.opencsvnetsf;

import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameMappingStrategy;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import au.com.bytecode.opencsv.bean.MappingStrategy;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import per.itachi.java.office.opencsv.app.exception.InfraException;

public class VerifiableCsvToBean<T> extends CsvToBean<T> {

    @Override
    protected T processLine(MappingStrategy<T> mapper, String[] line)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
        verifyLine(mapper, line);
        return super.processLine(mapper, line);
    }

    private void verifyLine(MappingStrategy<T> mapper, String[] line) {
        if (mapper instanceof HeaderColumnNameMappingStrategy) {
            HeaderColumnNameTranslateMappingStrategy<T> mappingStrategy = (HeaderColumnNameTranslateMappingStrategy<T>) mapper;
            Map<String, String> mapping = mappingStrategy.getColumnMapping();
            if (mapping == null || mapping.isEmpty()) {
                return;
            }
            if (line.length < mapping.size()) {
                throw new InfraException("Expected at least " + mapping.size()
                        + " columns, but found" + line.length);
            }
        }
    }

}
