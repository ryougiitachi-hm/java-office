package per.itachi.java.office.opencsv.infra.csv;

import java.util.List;
import java.util.function.Function;

public interface CsvPort {

    <T> List<T> readRecordsFrom(String filePath, Class<T> beanClazz);

    /**
     * Sometimes, we don't expect to use directly data from csv, which is expected to convert into another bean class.
     * In this case, this method may help via convertor.
     * @param <F> F is short for FROM.
     * @param <T> T is short for TO.
     * @param convertor convertor.
     * */
    <F, T> List<T> readRecordsFrom(String filePath, Class<F> fromBeanClazz, Class<T> toBeanClazz, Function<F, T> convertor);

    <T> void writeRecordsInto(String filePath, List<T> records, Class<T> beanClazz);
}
