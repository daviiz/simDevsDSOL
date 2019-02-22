package asw.soa.om5.inportPort;

import asw.soa.om5.portType.MoveResult;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class SensorIn_MOVE_RESULT extends InputPort<Double, Double, SimTimeDouble, MoveResult> {
    public SensorIn_MOVE_RESULT(CoupledModel<Double, Double, SimTimeDouble> coupledModel) {
        super(coupledModel);
    }

    public SensorIn_MOVE_RESULT(AtomicModel<Double, Double, SimTimeDouble> atomicModel) {
        super(atomicModel);
    }
}
