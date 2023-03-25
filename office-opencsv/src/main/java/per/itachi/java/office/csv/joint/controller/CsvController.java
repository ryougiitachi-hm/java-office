package per.itachi.java.office.csv.joint.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import per.itachi.java.office.csv.app.service.CsvService;

@Slf4j
@RestController
@RequestMapping("/csv")
public class CsvController {

    @Autowired
    private CsvService csvService;

    @GetMapping("/input/sample")
    public void readCsvFileForSample(@RequestParam String csvInputPath) {
        csvService.printCsvFileForSample(csvInputPath);
    }
}