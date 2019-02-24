package asw.soa.om5.coupledModel;

import asw.soa.data.ModelData;
import asw.soa.om5.atomicModel.*;
import asw.soa.om5.inportPort.In_ENV_INFO;
import asw.soa.om5.outportPort.Out_ENT_INFO;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class Submarine extends CoupledModel<Double, Double, SimTimeDouble> {

    public In_ENV_INFO in_ENV_INFO;
    public Out_ENT_INFO out_ENT_INFO;

    private Sensor2 s;
    private Maneuver2 m;
    private Controller2 c;

    public Submarine(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel) {
        super(modelName, parentModel);

    }

    public void constructModel(ModelData data) {

        s = new Sensor2(data.name + "_sensor", this, data.detectRange);
        s.initialize(0.0);
        c = new Controller2(data.name + "_controller", this);
        c.initialize(0.0);
        m = new Maneuver2(data.name + "_maneuver", this, data);
        m.initialize(0.0);


        in_ENV_INFO = new In_ENV_INFO(this);
        out_ENT_INFO = new Out_ENT_INFO(this);

        this.addExternalInputCoupling(this.in_ENV_INFO, s.in_THREAT_ENT_INFO);

        this.addInternalCoupling(m.out_MOVE_RESULT, s.in_MOVE_RESULT);
        this.addInternalCoupling(m.out_MOVE_RESULT, c.in_MOVE_RESULT);
        this.addInternalCoupling(s.out_THREAT_INFO, c.in_THREAT_INFO);
        this.addInternalCoupling(c.out_MOVE_CMD, m.in_MOVE_CMD);

        this.addExternalOutputCoupling(m.out_MOVE_RESULT, this.out_ENT_INFO);
    }

//    public Submarine(String modelName, DEVSSimulatorInterface<Double, Double, SimTimeDouble> simulator, ModelData data) {
//        super(modelName, simulator);
//
//        s = new Sensor(data.name, simulator, data.detectRange);
//        c = new Controller(data.name, simulator);
//        m = new Maneuver(data.name, simulator, data);
//
//        in_ENV_INFO = new In_ENV_INFO(this);
//        out_ENT_INFO = new Out_ENT_INFO(this);
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
