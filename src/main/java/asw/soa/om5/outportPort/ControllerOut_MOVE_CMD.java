package asw.soa.om5.outportPort;

import asw.soa.om5.portType.MoveCmd;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class ControllerOut_MOVE_CMD extends OutputPort<Double, Double, SimTimeDouble, MoveCmd> {
    public ControllerOut_MOVE_CMD(CoupledModel<Double, Double, SimTimeDouble> coupledModel) {
        super(coupledModel);
    }

    public ControllerOut_MOVE_CMD(AtomicModel<Double, Double, SimTimeDouble> atomicModel) {
        super(atomicModel);
    }
}
