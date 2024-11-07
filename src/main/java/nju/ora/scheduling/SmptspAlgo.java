package nju.ora.scheduling;

import nju.ora.base.AlgoParam;
import nju.ora.base.ProblemIO;
import nju.ora.scheduling.bnp.SmptspBranchAndPrice;
import nju.ora.scheduling.data.Smptsp;
import nju.ora.scheduling.data.SmptspData;
import nju.ora.scheduling.model.SmptspModelGurobi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FeiAiYue
 * @date 2024年11月02日 21:16
 * @description
 */
public class SmptspAlgo {
    private static final boolean useParamFromPath = false; // or true
    private static final String pathParam = "result/2dvpp/param.json";
    public static AlgoParam param = new AlgoParam(
            true,
            "smptsp",
            "problem/smptsp/Data_010",
            // "result/smptsp",
            "result_test/smptsp",
            "",
            ".dat",
            "localhost",
            "modelGurobi",
            3600,
            3,
            1
    );

    public static void run() {
        System.out.println("param=" + param);
        ProblemIO.preparePaths(param);

        List<SmptspData> dataset = new ArrayList<>();
        if ("smptsp".equals(param.problemId)) {
            dataset = readSmptspDataSets(param);
        }

        switch (param.algorithmId) {
            case "branchAndPrice":
                runBranchAndPrice(dataset);
                break;
            case "modelGurobi":
                runModelGurobi(dataset);
                break;
            default:
                System.err.println("Invalid param.algorithmId!");
        }
    }

    public static void runBranchAndPrice(List<SmptspData> dataset) {
        for (SmptspData data : dataset) {
            System.out.println();
            Smptsp inst = new Smptsp(data);
            inst.prepare();
            SmptspBranchAndPrice bp = new SmptspBranchAndPrice(inst, param);
            bp.run();
            System.out.println(bp.getGlobalLb() + ", " + bp.getGlobalUb() + ", " + bp.getBestSol());
        }
    }

    public static void runModelGurobi(List<SmptspData> dataset) {
        // 使用 param 中 public 字段直接访问
        String pathAlgo = param.pathResult + "/" + param.algorithmId;
        String pathCsv = param.pathResult + "/" + param.algorithmId + "/" +
                param.pathData + "/" + param.algorithmId;

        ProblemIO.write(pathCsv, csvTitle());
        System.out.println(pathCsv);

        for (SmptspData data : dataset) {
            System.out.println(data.instanceId);
            Smptsp inst = new Smptsp(data);
            inst.prepare(data);
            SmptspModelGurobi model = new SmptspModelGurobi(inst);
            model.run(param.timeLimit, param.randomSeed, param.numThreads);

            if (model.isFeasible()) {
                String csv = model.getCsvResult();
                ProblemIO.append(pathCsv, csv);

                Solution s = model.getSolution();
                SmptspSol sol = new SmptspSol(data.getInstanceId(), inst.getI(), param, today, s.getFirst(), s.getSecond(), s.getThird());
                String filename = String.format("%s/sol_%s.json", pathAlgo, data.getInstanceId());
                ProblemIO.write(filename, Json.prettyPrint(Json.toJson(sol)));
                inst.validate(sol);
            } else {
                model.computeIIS();
            }
            model.end();
            System.gc();
        }
    }

    public static String csvTitle() {
        if ("modelGurobi".equals(param.getAlgorithmId())) {
            return "instanceId,numTasks,numWorkers,numConstraints,numVariables,numNonZeros,numNodes,timeOnModel," +
                    "timeOnOptimize,objValue,objBound,MIPGap,numConstraintsPresolve,numVariablesPresolve,numNonZerosPresolve";
        }
        return "";
    }

    public static List<SmptspData> readSmptspDataSets(AlgoParam param) {
        List<File> files = ProblemIO.fetchFiles(param);
        List<SmptspData> buffer = new ArrayList<>();
        for (File file : files) {
            List<String> text = ProblemIO.readText(file);
            if (param.isDebug()) {
                System.out.println("file name" + file.getName());
                System.exit(0);
            }
            SmptspData data = parseSmptsp(file.getName(), text);
            if (param.isDebug()) {
                System.out.println("the data of smptsp:" + data.toString());
                System.exit(0);
            }
            buffer.add(data);
        }
        return buffer;
    }

    /**
     * This dataset contains 137 instances,
     * with the number of workers (nWorkers) ranging from 22 to 420,
     * the number of tasks (nTasks) ranging from 40 to 2105, and a tightness of 90%.
     * Reference:
     * Krishnamoorthy, M., Ernst, A. T., & Baatar, D. (2012).
     * Algorithms for large scale Shift Minimisation Personnel Task Scheduling Problems.
     * European Journal of Operational Research, 219(1), 34–48.
     * https://doi.org/10.1016/j.ejor.2011.11.034
     * The dataset is named 'ptask'
     * e.g. data file is named `Data_10_51_111_66.dat`.
     *
     * This method parses the given text data to create an instance of SmptspData.
     *
     * @param instanceId The name of the file containing the data.
     * @param text       A list of strings containing the data, with each string corresponding to a line in the file.
     * @return           An instance of SmptspData populated with the parsed scheduling data.
     */
    public static SmptspData parseSmptsp(String instanceId, List<String> text) {
        String[] strs;
        strs = instanceId.split("\\.")[0].trim().split("_"); // // `Data_10_51_111_66.dat` -> `Data_10_51_111_66` -> ["Data", "10", "51", "111", "66"]
        int numOfWorkers = Integer.parseInt(strs[2]); // 51
        int numOfTasks = Integer.parseInt(strs[3]); // 111
        int[] startT = new int[numOfWorkers];
        int[] finishT = new int[numOfWorkers];
        int[][] qualifiedTasksPerWorker = new int[numOfWorkers][];
        List<String[]> lines = text.stream()
                .map(String::trim)  // 去除每行的首尾空格
                .map(line -> line.split(" +"))  // 根据一个或多个空格分割
                .collect(Collectors.toList());  // 收集结果到列表中
        int line = 5;
        for (int i = 0; i < numOfTasks; i++) {
            strs = text.get(line++).trim().split("\\s+");
            startT[i] = Integer.parseInt(strs[0]);
            finishT[i] = Integer.parseInt(strs[1]);
        }
        line++;
        for (int i = 0; i < numOfWorkers; i++) {
            strs = text.get(line++).split(":");
            int taskCountForWorker = Integer.parseInt(strs[0]);
            qualifiedTasksPerWorker[i] = new int[taskCountForWorker];
            for (int j = 0; j < taskCountForWorker; j++) {
                qualifiedTasksPerWorker[i][j] = Integer.parseInt(strs[j + 1].trim());
            }
        }
        return new SmptspData(instanceId, numOfTasks, startT, finishT, numOfWorkers, qualifiedTasksPerWorker);
    }
}
