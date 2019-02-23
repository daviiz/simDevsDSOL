package asw.soa.om5.coupledModel;

import asw.soa.data.ModelData;
import asw.soa.om5.atomicModel.Environment;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class RootCoupledModel extends CoupledModel<Double,Double, SimTimeDouble> {

    Fleet fleet;
    Submarine sub;
    Environment env;

    public RootCoupledModel(String modelName) {
        super(modelName);
    }

    public void constructModel(ModelData f1Data, ModelData s1Data){
        sub = new Submarine("Submarine",this);
        sub.constructModel(s1Data);
        fleet = new Fleet("Fleet",this);
        fleet.construcModel(f1Data);
        env = new Environment("env",this);
       env.initialize(0.0);

        this.addInternalCoupling(fleet.out_ENT_INFO,env.in_move_result);
        this.addInternalCoupling(sub.out_ENT_INFO,env.in_move_result);
        this.addInternalCoupling(env.out_ent_info,fleet.in_ENV_INFO);
        this.addInternalCoupling(env.out_ent_info,sub.in_ENV_INFO);
    }

    public RootCoupledModel(String modelName, DEVSSimulatorInterface<Double, Double, SimTimeDouble> simulator, ModelData f1Data, ModelData s1Data) {
        super(modelName, simulator);

//        sub = new Submarine(s1Data.name,this,s1Data);
//        fleet = new Fleet(f1Data.name,this,f1Data);
//        env = new Environment("env",this);
//
//        this.addInternalCoupling(fleet.out_ENT_INFO,env.in_move_result);
//        this.addInternalCoupling(sub.out_ENT_INFO,env.in_move_result);
//        this.addInternalCoupling(env.out_ent_info,fleet.in_ENV_INFO);
//        this.addInternalCoupling(env.out_ent_info,sub.in_ENV_INFO);

    }
}
