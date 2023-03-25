package per.itachi.java.office.csv.infra.office;

import java.util.List;
import java.util.Map;

public class CsvTable {

    private Map<String, Integer> mappingColumnToPosition;

    private List<String[]> dataRows;

    /**
     * @param rowNbr Actual data row. If there is a header row, the first actual row should be the second row. 0-based.
     * */
    public String getFieldValueByColumnName(int rowNbr, String columnName) {
        if (rowNbr < 0 || rowNbr >= dataRows.size()) {
            return null; // TODO: getFieldValueByColumnName
        }
        Integer columnPosition = mappingColumnToPosition.get(columnName);
        if (columnPosition == null) {
            return null; // TODO: getFieldValueByColumnName
        }
        return getFieldValueByColumnPosition(rowNbr, columnPosition);
    }

    /**
     * @param rowNbr Actual data row. If there is a header row, the first actual row should be the second row. 0-based.
     * @param columnPosition 0-based.
     *
     * */
    public String getFieldValueByColumnPosition(int rowNbr, int columnPosition) {
        String[] row = dataRows.get(rowNbr);
        if (row == null || columnPosition >= row.length ) {
            return null; // TODO: getFieldValueByColumnPosition
        }
        return row[columnPosition];
    }
}
