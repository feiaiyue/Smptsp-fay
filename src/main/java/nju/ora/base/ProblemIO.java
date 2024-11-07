package nju.ora.base;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @author FeiAiYue
 * @date 2024年11月02日 21:21
 * @description
 */
public class ProblemIO {
    /**
     * Creates the necessary directories for storing algorithm results.
     * It creates the following directory structure if it doesn't exist:
     * <ul>
     *   <li>Result directory (from {@code param.getPathResult()})</li>
     *   <li>Algorithm directory (from {@code param.getAlgorithmId()})</li>
     *   <li>Data directory (from {@code param.getPathData()})</li>
     * </ul>
     *
     * @param param The {@link AlgoParam} object containing paths for result storage and algorithm ID.
     */
    public static void preparePaths(AlgoParam param) {
        try {
            File dirResult = new File(param.pathResult);
            if (!dirResult.exists() || !dirResult.isDirectory()) {
                dirResult.mkdir();
            }
            File dirResAlgo = new File(dirResult, param.algorithmId);
            if (!dirResAlgo.exists() || !dirResAlgo.isDirectory()) {
                dirResAlgo.mkdir();
            }
            File dirResAlgoData = new File(dirResAlgo, param.pathData);
            if (!dirResAlgoData.exists() || !dirResAlgoData.isDirectory()) {
                dirResAlgoData.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(String filename, String text) {
        try (FileWriter fw = new FileWriter(new File(filename))) {
            fw.write(text + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void append(String filename, String text) {
        try (FileWriter fw = new FileWriter(new File(filename), true)) {
            fw.write(text + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readText(String pathname) {
        List<String> text = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathname))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error when reading " + pathname);
            e.printStackTrace();
        }
        return text;
    }

    public static List<String> readText(File file) {
        List<String> text = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error when reading " + file);
            e.printStackTrace();
        }
        return text;
    }

    public static List<File> fetchFiles(AlgoParam param) {
        File data = new File(param.getPathData());
        List<File> files = new ArrayList<>();
        List<File> queue = new ArrayList<>();

        if (data.isDirectory()) {
            queue.add(data);
        }

        while (!queue.isEmpty()) {
            File folder = queue.remove(0);
            File[] list = folder.listFiles();
            if (list != null) {
                for (File file : list) {
                    if (file.isDirectory()) {
                        queue.add(file);
                    } else if (file.getName().startsWith(param.getInstanceIdPrefix()) &&
                            file.getName().endsWith(param.getInstanceIdExt())) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }
}
