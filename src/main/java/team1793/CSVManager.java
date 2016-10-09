package team1793;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVManager {

    //Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String DATE = "date", LOGIN = "login", LOGOUT="logout", TOTAL="total";
    private static final String[] HEADER = new String[]{"date", "login", "logout", "total"};
    //CSV file header
    public static Member readMemberFile(File file) {
        String fullname = file.getName().replace(".csv","");
        String firstName = fullname.split(" ")[0],lastName = fullname.split(" ")[1];
        Member member = new Member(firstName,lastName);
        FileReader fileReader = null;
        CSVParser csvFileParser = null;
        //Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(HEADER);
        try {
            //initialize FileReader object
            fileReader = new FileReader(file);
            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                Day day = new Day(record.get(LOGIN), record.get(LOGOUT));
            }
        }
        catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
        return member;
    }


    public static void writeMemberFile(Member member) {
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
            List<Object[]> data = member.getData();
            for(Object[] d:data) {
                csvFilePrinter.printRecord(d);
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
