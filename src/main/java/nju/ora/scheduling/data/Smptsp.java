package nju.ora.scheduling.data;

import java.sql.RowId;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FeiAiYue
 * @date 2024年11月06日 21:42
 * @description
 */
public class Smptsp {
    public String instanceId;
    public int J; // add a dummy source node 0 and a dummy sink node n+1
    public int[] jobsStartT;
    public int[] jobsFinishT;
    public int W;
    public int[][] tasksPerWorkerQualified;
    public List<int[]>[] arcs;
    public Smptsp(SmptspData data) {
        this.instanceId = data.instanceId;
        this.J = data.numJobs + 2;
        this.jobsStartT = new int[this.J + 2];
        this.jobsFinishT = new int[this.J + 2];

        this.W = data.numWorkers;
        this.tasksPerWorkerQualified = data.tasksPerWorkerQualified;
        this.arcs = new List[W];
        for (int w = 0; w < W; w++) {
            arcs[w] = new ArrayList<>();
        }
    }

    public void prepare(SmptspData data) {
        // define the start T of job 0 and job n+1
        this.jobsStartT[0] = 0;
        this.jobsFinishT[0] = 0;
        this.jobsStartT[this.J - 1] = Integer.MAX_VALUE;
        this.jobsFinishT[this.J - 1] = Integer.MAX_VALUE;
        for (int i = 0; i < data.numJobs; i++) {
            this.jobsStartT[i + 1] = data.jobsStartT[i];
            this.jobsFinishT[i + 1] = data.jobsFinishT[i];
        }
        for (int w = 0; w < W; w++) {
            for (int i = 0; i < tasksPerWorkerQualified[w].length; i++) {
                for (int j = 0; j < tasksPerWorkerQualified[w].length; j++) {
                    // 需要注意task的索引都要往后移动一个
                    int task1 = tasksPerWorkerQualified[w][i] + 1;
                    int task2 = tasksPerWorkerQualified[w][j] + 1;
                    if (jobsFinishT[task1] <= jobsStartT[task2]) {
                        arcs[w].add(new int[]{task1, task2});
                    }
                }
            }
        }


    }
}
