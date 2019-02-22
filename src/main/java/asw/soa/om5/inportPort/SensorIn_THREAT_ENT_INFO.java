package asw.soa.om5.inportPort;

import asw.soa.om5.portType.ENT_INFO;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.InputPort;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class SensorIn_THREAT_ENT_INFO extends InputPort<Double, Double, SimTimeDouble, ENT_INFO> {
    public SensorIn_THREAT_ENT_INFO(CoupledModel<Double, Double, SimTimeDouble> coupledModel) {
        super(coupledModel);
    }

    public SensorIn_THREAT_ENT_INFO(AtomicModel<Double, Double, SimTimeDouble> atomicModel) {
        super(atomicModel);
    }
}
