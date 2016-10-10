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
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CSVUtils {

    //Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String DATE = "date", LOGIN = "login", LOGOUT="logout",  BUSPASS="buspass";
    private static final String[] HEADER = new String[]{DATE,LOGIN,LOGOUT,BUSPASS};

    //CSV file header
    public static void addMemberList(File file) {
        FileReader fileReader = null;
        CSVParser csvFileParser = null;
        //Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader("name","team");
        try {
            //initialize FileReader object
            fileReader = new FileReader(file);
            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                String name = record.get("name");
                String team = record.get("team");
                String[] full_name = name.split("_");
                AddMember.addMember(full_name[0],full_name[1],team);
            }
        }
        catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                assert fileReader != null;
                fileReader.close();
                assert csvFileParser != null;
                csvFileParser.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
    }
    public static Member readMemberFile(File file) {
        String team = file.getParentFile().getName();
        String fullname = file.getName().replace(".csv","").replace("_"," ");
        String[] s = fullname.split(" ");
        String firstName = s[0] != null ? s[0] : "", lastName = s[1] != null ? s[1] : "";
        Member member = new Member(firstName,lastName, Member.Team.valueOf(team.toUpperCase()));
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
                Date date = TimeUtils.fromStringToDate(record.get(DATE));
                Date login = TimeUtils.fromStringToDateTime(String.format("%s %s",record.get(DATE),record.get(LOGIN)));
                Date logout = TimeUtils.fromStringToDateTime(String.format("%s %s",record.get(DATE),record.get(LOGOUT)));
                boolean buspass = Boolean.parseBoolean(record.get(BUSPASS));
                member.addDay(login,logout, buspass);
            }
        }
        catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                assert fileReader != null;
                fileReader.close();
                assert csvFileParser != null;
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
            for(Map.Entry<Date,Day> entry: member.days.entrySet()) {
                String date = TimeUtils.toString(entry.getKey());
                Day day = entry.getValue();
                String login = day.getFormattedLoginTime();
                String logout = day.getFormattedLogoutTime();
                csvFilePrinter.printRecord(date, login,logout,day.needsBusPass());
            }
            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                assert fileWriter != null;
                fileWriter.flush();
                fileWriter.close();
                assert csvFilePrinter != null;
                csvFilePrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace();
            }
        }
    }
}
