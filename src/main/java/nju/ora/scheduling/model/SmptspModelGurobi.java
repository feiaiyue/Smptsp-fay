package nju.ora.scheduling.model;

import gurobi.*;
import nju.ora.scheduling.data.Smptsp;

/**
 * @author FeiAiYue
 * @date 2024年11月07日 22:30
 * @description
 */
public class SmptspModelGurobi {
    public Smptsp instance;
    public GRBEnv env;
    public GRBModel model;
    public GRBVar[][][] x;
    public GRBModel reducedModel; // model after presolve
    public double timeOnModel = 0.0;
    public double timeOnOptimize = 0.0;

    public SmptspModelGurobi(Smptsp instance) throws GRBException {
        this.instance = instance;
        this.env = new GRBEnv();
        this.x = new GRBVar[instance.W][instance.J][instance.J];
    }

    /**
     *
     * @throws GRBException
     */
    public void build() throws GRBException {
        model = new GRBModel(env);
        for (int w = 0; w < instance.W; w++) {
            for (int i = 0; i < instance.J; i++) {
                for (int j = 0; j < instance.J; j++) {
                    x[w][i][j] = model.addVar(0, 1, 1, GRB.BINARY, "x_" + w + i + j);
                }
            }

        }

    }

    public void run() {

    }
}
