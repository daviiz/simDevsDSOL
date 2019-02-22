package asw.soa.om5.inportPort;

import asw.soa.om5.portType.MoveCmd;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class ManeuverIn_MOVE_CMD extends InputPort<Double,Double, SimTimeDouble, MoveCmd> {

    public ManeuverIn_MOVE_CMD(CoupledModel coupledModel) {
        super(coupledModel);
    }

    public ManeuverIn_MOVE_CMD(AtomicModel atomicModel) {
        super(atomicModel);
    }
}
