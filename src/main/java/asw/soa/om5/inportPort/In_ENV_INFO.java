package asw.soa.om5.inportPort;

import asw.soa.om5.portType.ENT_INFO;
import asw.soa.om5.portType.MoveResult;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class In_ENV_INFO extends InputPort<Double, Double, SimTimeDouble, MoveResult> {
    public In_ENV_INFO(CoupledModel<Double, Double, SimTimeDouble> coupledModel) {
        super(coupledModel);
    }

    public In_ENV_INFO(AtomicModel<Double, Double, SimTimeDouble> atomicModel) {
        super(atomicModel);
    }
}
