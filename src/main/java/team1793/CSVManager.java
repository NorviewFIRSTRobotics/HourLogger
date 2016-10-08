package team1793;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CSVManager {

    //Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String[] HEADER = new String[]{"date", "login time", "logout time", "minutes"};
    //CSV file header

    public static void writeCSVFiles(Member member) {
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        try {
            //initialize FileWriter object
            fileWriter = new FileWriter(new File(HourLogger.saveDir, member.getFullname() + ".csv"));
            //initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            //Create CSV file header
            csvFilePrinter.printRecord(HEADER);
            for (Map.Entry<Integer, Day> entry : member.getDays().entrySet()) {
                String date = TimeUtil.getDate(entry.getValue().getLoginTime());
                String login_time = TimeUtil.getHour(entry.getValue().getLoginTime());
                String logout_time = TimeUtil.getHour(entry.getValue().getLogoutTime());
                int diff = entry.getValue().getLogoutTime() - entry.getValue().getLoginTime();
                System.out.printf("%s,%s,%s,%s\n", date, login_time, logout_time, diff);
                csvFilePrinter.printRecord(date, login_time, logout_time, diff);
            }
            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace();
            }
        }
    }
}
