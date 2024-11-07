package nju.ora.base;

import java.util.Deque;

/**
 * @author FeiAiYue
 * @date 2024年11月02日 20:58
 * @description
 */
public class AlgoParam {
    public boolean debug;
    public String problemId;
    public String pathData;
    public String pathResult;
    public String instanceIdPrefix;
    public String instanceIdExt; // extension such as ".txt"/".json"
    public String server;
    public String algorithmId;
    public int timeLimit;
    public int randomSeed;
    public int numThreads;

    /**
     * Constructor to initialize the AlgoParam object with given parameters.
     *
     * @param debug            Flag to enable/disable debug mode
     * @param problemId        Problem identifier
     * @param pathData         Path to data files
     * @param pathResult       Path to store results
     * @param instanceIdPrefix Prefix for instance IDs
     * @param instanceIdExt    Extension for instance files (e.g., ".txt", ".json")
     * @param server           Server address or name
     * @param algorithmId      Algorithm identifier
     * @param timeLimit        Time limit for the algorithm
     * @param randomSeed       Random seed for the algorithm
     * @param numThreads       Number of threads for parallel execution
     */
    public AlgoParam(boolean debug, String problemId, String pathData, String pathResult, String instanceIdPrefix,
                     String instanceIdExt, String server, String algorithmId,
                     int timeLimit, int randomSeed, int numThreads) {
        this.debug = debug;
        this.problemId = problemId;
        this.pathData = pathData;
        this.pathResult = pathResult;
        this.instanceIdPrefix = instanceIdPrefix;
        this.instanceIdExt = instanceIdExt;
        this.server = server;
        this.algorithmId = algorithmId;
        this.timeLimit = timeLimit;
        this.randomSeed = randomSeed;
        this.numThreads = numThreads;
    }

}
