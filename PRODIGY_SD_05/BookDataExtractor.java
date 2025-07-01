import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

public class BookDataExtractor {
    public static void main(String[] args) {
        String inputFile = "bestsellers with categories.csv"; // Input file path
        String outputFile = "extracted_books.csv";            // Output file path

        try (
            CSVReader reader = new CSVReader(new FileReader(inputFile));
            CSVWriter writer = new CSVWriter(new FileWriter(outputFile))
        ) {
            String[] headers = reader.readNext(); // Read header
            if (headers == null) {
                System.out.println("Empty CSV file.");
                return;
            }

            // Write new header
            String[] outputHeaders = {"Name", "Author", "Price", "User Rating"};
            writer.writeNext(outputHeaders);

            // Map column indexes
            int nameIndex = Arrays.asList(headers).indexOf("Name");
            int authorIndex = Arrays.asList(headers).indexOf("Author");
            int priceIndex = Arrays.asList(headers).indexOf("Price");
            int ratingIndex = Arrays.asList(headers).indexOf("User Rating");

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String[] row = {
                    nextLine[nameIndex],
                    nextLine[authorIndex],
                    nextLine[priceIndex],
                    nextLine[ratingIndex]
                };
                writer.writeNext(row);
            }

            System.out.println("Extraction completed. Saved to: " + outputFile);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}