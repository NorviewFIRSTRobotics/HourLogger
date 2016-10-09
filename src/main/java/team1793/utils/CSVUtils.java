package team1793.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import team1793.data.Day;
import team1793.data.Member;
import team1793.dialog.AddMember;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CSVUtils {

    //Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String DATE = "date", LOGIN = "login", LOGOUT="logout", TOTAL="total", BUSPASS="buspass";
    private static final String[] HEADER = new String[]{DATE,LOGIN,LOGOUT,TOTAL,BUSPASS};
    //CSV file header
    public static void addMemberList(File file) {
        FileReader fileReader = null;
        CSVParser csvFileParser = null;
        //Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader("name");
        try {
            //initialize FileReader object
            fileReader = new FileReader(file);
            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                String name = record.get("name");
                String[] full_name = name.split("_");
                AddMember.addMember(full_name[0],full_name[1]);
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
    }
    public static Member readMemberFile(File file) {
        String fullname = file.getName().replace(".csv","").replace("_"," ");
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
                String date = record.get(DATE);
                String login = String.format("%s %s", date, record.get(LOGIN));
                String logout =  String.format("%s %s", date, record.get(LOGOUT));
                member.addDay(login,logout, Boolean.parseBoolean(record.get(BUSPASS)));
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
            fileWriter = new FileWriter(member.getSaveFile());
            //initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            //Create CSV file header
            csvFilePrinter.printRecord((Object[]) HEADER);
            for(Map.Entry<String,Day> entry: member.days.entrySet()) {
                String date = entry.getKey();
                Day day = entry.getValue();
                String login = day.getFormattedLoginTime();
                String logout = day.getFormattedLogoutTime();
                int total = day.getTimeLoggedIn();
                csvFilePrinter.printRecord(date, login,logout,total,day.needsBusPass());
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
