package per.itachi.java.office.opencsv.infra.csv.opencsvnetsf;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.MappingStrategy;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import per.itachi.java.office.opencsv.app.exception.InfraException;

/**
 * @param <F> F is short for FROM.
 * @param <T> T is short for TO.
 * @since 1.0
 * */
public class ConvertableCsvToBean<F, T> extends CsvToBean<F> {

    private Function<F, T> convertor;

    public ConvertableCsvToBean(Function<F, T> convertor) {
        this.convertor = convertor;
    }

    public List<T> parseAndConvert(MappingStrategy<F> mapper, CSVReader csv) {
        try {
            mapper.captureHeader(csv);
            String[] line;
            List<T> list = new LinkedList<>();
            while (null != (line = csv.readNext())) {
                F fromBean = processLine(mapper, line);
                T toBean = convertor.apply(fromBean);
                list.add(toBean);
            }
            return list;
        }
        catch (Exception e) {
            throw new InfraException("Error parsing CSV!", e);
        }
    }
}
