package asw.soa.om5.outportPort;

import asw.soa.om5.portType.ENT_INFO;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.OutputPort;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class EnvOut_ENT_INFO extends OutputPort<Double, Double, SimTimeDouble, ENT_INFO> {
    public EnvOut_ENT_INFO(CoupledModel<Double, Double, SimTimeDouble> coupledModel) {
        super(coupledModel);
    }

    public EnvOut_ENT_INFO(AtomicModel<Double, Double, SimTimeDouble> atomicModel) {
        super(atomicModel);
    }
}
