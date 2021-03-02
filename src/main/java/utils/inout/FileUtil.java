package utils.inout;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to deal with files
 *
 * @Author Ana García
 */

public class FileUtil {

    /**
     * Method that reads a csv file and return a List with its lines
     * @param filePath Path of the csv
     * @return List of Strings with its lines
     * @throws IOException
     */
    public List<String> readCsv(String filePath) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
        String line = fileReader.readLine();

        List<String> lines = new ArrayList<String>();
        while(line != null){
            lines.add(line);

            line = fileReader.readLine();
        }
        fileReader.close();
        return lines;
    }
}
