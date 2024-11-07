package nju.ora.scheduling.data;

import java.util.Arrays;

/**
 * @author FeiAiYue
 * @date 2024年11月02日 21:26
 * @description
 */
public class SmptspData {
    public String instanceId;
    public int numJobs;
    public int[] jobsStartT;
    public int[] jobsFinishT;
    public int numWorkers;
    public int[][] tasksPerWorkerQualified;

    public SmptspData(String instanceId, int numOfTasks, int[] jobsStartT, int[] jobsFinishT, int numOfWorkers, int[][] tasksPerWorkerQualified) {
        this.instanceId = instanceId;
        this.numJobs = numOfTasks;
        this.jobsStartT = jobsStartT;
        this.jobsFinishT = jobsFinishT;
        this.numWorkers = numOfWorkers;
        this.tasksPerWorkerQualified = tasksPerWorkerQualified;
    }


    @Override
    public String toString() {
        return "SmptspData{" +
                "instanceId='" + instanceId + '\'' +
                ", numOfTasks=" + numJobs +
                ", startT=" + Arrays.toString(jobsStartT) +
                ", finishT=" + Arrays.toString(jobsFinishT) +
                ", numOfWorkers=" + numWorkers +
                ", tasksPerWorker=" + Arrays.toString(tasksPerWorkerQualified) +
                '}';
    }
}
