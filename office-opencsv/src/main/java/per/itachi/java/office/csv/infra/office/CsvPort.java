package per.itachi.java.office.csv.infra.office;

import java.util.List;

public interface CsvPort {

    <T> List<T> readCsv(String csvInputFile, Class<T> clazzInputFile);

    <T> void writeCsv(String csvOutputFile, Class<T> clazzInputFile, List<T> outputList);
}
