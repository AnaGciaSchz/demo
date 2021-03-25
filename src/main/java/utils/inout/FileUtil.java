package utils.inout;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to deal with files
 *
 * @Author Ana Garcia
 */

public class FileUtil {

    /**
     * Method that reads a csv file and return a List with its lines
     *
     * @param filePath Path of the csv
     * @return List of Strings with its lines
     * @throws IOException if there's an error
     */
    public List<String> readCsv(String filePath) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
        String line = fileReader.readLine();

        List<String> lines = new ArrayList<>();
        while (line != null) {
            lines.add(line);

            line = fileReader.readLine();
        }
        fileReader.close();
        return lines;
    }

    /**
     * Method that returns the reader os a file so we can read and index at the same time, for example
     *
     * @param filePath Path of the file lo read
     * @return The reader for the file
     * @throws IOException
     */
    public BufferedReader getReaderCsv(String filePath) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath));

        return fileReader;
    }
}
