package per.itachi.java.office.csv.infra.office.dojo;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SampleByNameDO {

    @CsvBindByName(column = "Application Name")
    private String applicationName;

    @CsvBindByName(column = "Application modules")
    private String applicationModule;

    @CsvBindByName(column = "Developers")
    private String developers;

    @CsvBindByName(column = "Leader")
    private String leader;

    @CsvBindByName(column = "Technology used on the application")
    private String technology;

    @CsvBindByName(column = "SVN")
    private String svn;

    @CsvBindByName(column = "Git")
    private String git;
}