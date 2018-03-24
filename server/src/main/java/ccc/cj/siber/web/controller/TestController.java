package ccc.cj.siber.web.controller;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping(value = "api/courses")
public class TestController {

    private static final Logger logger = LogManager.getLogger(TestController.class);

    @RequestMapping(method = RequestMethod.GET)
    public Object test() {
        logger.info("/test [GET]");
        try {
            return "success";
        } catch (Throwable t) {
            logger.error("", t);
        }
        return null;
    }

    private static final String SAMPLE_CSV_FILE_PATH = "./users-with-header.csv";

    public static void main(String[] args) throws IOException {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim());
        ) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                // Accessing values by Header names

                String name = csvRecord.get("name");
                String email = csvRecord.get("email");
                String phone = csvRecord.get("phone");
                String country = csvRecord.get("country");

                System.out.println("Record No - " + csvRecord.getRecordNumber());
                System.out.println("---------------");
                System.out.println("Name : " + name);
                System.out.println("Email : " + email);
                System.out.println("Phone : " + phone);
                System.out.println("Country : " + country);
                System.out.println("---------------\n\n");
            }
        }
    }

}
