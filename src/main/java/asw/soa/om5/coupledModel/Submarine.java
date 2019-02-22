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

public class Submarine extends CoupledModel<Double, Double, SimTimeDouble> {

    public In_ENV_INFO in_ENV_INFO;
    public Out_ENT_INFO out_ENT_INFO;

    private Sensor s;
    private Maneuver m;
    private Controller c;

    public Submarine(String modelName, DEVSSimulatorInterface<Double, Double, SimTimeDouble> simulator, ModelData data) {
        super(modelName, simulator);

        s = new Sensor("s", simulator, 400);
        c = new Controller("c", simulator);
        m = new Maneuver("m", simulator, data);

        in_ENV_INFO = new In_ENV_INFO(this);
        out_ENT_INFO = new Out_ENT_INFO(this);

        this.addExternalInputCoupling(this.in_ENV_INFO,s.in_THREAT_ENT_INFO);

        this.addInternalCoupling(m.out_MOVE_RESULT,s.in_MOVE_RESULT);
        this.addInternalCoupling(m.out_MOVE_RESULT,c.in_MOVE_RESULT);
        this.addInternalCoupling(s.out_THREAT_INFO,c.in_THREAT_INFO);
        this.addInternalCoupling(c.out_MOVE_CMD,m.in_MOVE_CMD);

        this.addExternalOutputCoupling(m.out_MOVE_RESULT,this.out_ENT_INFO);
    }
}
