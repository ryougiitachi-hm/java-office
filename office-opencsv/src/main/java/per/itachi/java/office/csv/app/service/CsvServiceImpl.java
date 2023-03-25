package per.itachi.java.office.csv.app.service;

import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import per.itachi.java.office.csv.infra.office.CsvPort;
import per.itachi.java.office.csv.infra.office.dojo.SampleByNameDO;

@Slf4j
public class CsvServiceImpl implements CsvService {

    @Resource
    private CsvPort csvPort;

    @Override
    public void printCsvFileForSample(String csvInputFile) {
        List<SampleByNameDO> list = csvPort.readCsv(csvInputFile, SampleByNameDO.class);
        for (SampleByNameDO item : list) {
            log.info("Application Name = {}, item={}", item.getApplicationName(), item);
        }
    }
}
