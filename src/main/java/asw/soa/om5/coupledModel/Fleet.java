package asw.soa.om5.coupledModel;

import asw.soa.data.ModelData;
import asw.soa.om5.atomicModel.Controller;
import asw.soa.om5.atomicModel.Maneuver;
import asw.soa.om5.atomicModel.Sensor;
import asw.soa.om5.inportPort.In_ENV_INFO;
import asw.soa.om5.outportPort.Out_ENT_INFO;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Fleet extends CoupledModel<Double,Double, SimTimeDouble> {

    public In_ENV_INFO in_ENV_INFO;
    public Out_ENT_INFO out_ENT_INFO;


    private Sensor s;
    private Maneuver m;
    private Controller c;

    public Fleet(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel, ModelData data) {
        super(modelName, parentModel);
        in_ENV_INFO = new In_ENV_INFO(this);
        out_ENT_INFO = new Out_ENT_INFO(this);


        s = new Sensor(data.name,this,data.detectRange);
        c = new Controller(data.name,this);
        m = new Maneuver(data.name,this,data);

        this.addExternalInputCoupling(this.in_ENV_INFO,s.in_THREAT_ENT_INFO);

        this.addInternalCoupling(m.out_MOVE_RESULT,s.in_MOVE_RESULT);
        this.addInternalCoupling(m.out_MOVE_RESULT,c.in_MOVE_RESULT);
        this.addInternalCoupling(s.out_THREAT_INFO,c.in_THREAT_INFO);
        this.addInternalCoupling(c.out_MOVE_CMD,m.in_MOVE_CMD);

        this.addExternalOutputCoupling(m.out_MOVE_RESULT,this.out_ENT_INFO);
    }

//    public Fleet(String modelName, DEVSSimulatorInterface<Double, Double, SimTimeDouble> simulator, ModelData data) {
//        super(modelName, simulator);
//
//        in_ENV_INFO = new In_ENV_INFO(this);
//        out_ENT_INFO = new Out_ENT_INFO(this);
//
//
//        s = new Sensor(data.name,simulator,data.detectRange);
//        c = new Controller(data.name,simulator);
//        m = new Maneuver(data.name,simulator,data);
//
//        this.addExternalInputCoupling(this.in_ENV_INFO,s.in_THREAT_ENT_INFO);
//
//        this.addInternalCoupling(m.out_MOVE_RESULT,s.in_MOVE_RESULT);
//        this.addInternalCoupling(m.out_MOVE_RESULT,c.in_MOVE_RESULT);
//        this.addInternalCoupling(s.out_THREAT_INFO,c.in_THREAT_INFO);
//        this.addInternalCoupling(c.out_MOVE_CMD,m.in_MOVE_CMD);
//
//        this.addExternalOutputCoupling(m.out_MOVE_RESULT,this.out_ENT_INFO);
//    }
}
