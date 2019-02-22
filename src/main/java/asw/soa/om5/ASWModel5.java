package asw.soa.om5;

import asw.soa.data.ModelData;
import asw.soa.view.Visual2dService;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.language.d3.CartesianPoint;

import javax.naming.NamingException;
import java.rmi.RemoteException;

public class ASWModel5 extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>{

    public ASWModel5(final DEVSSimulatorInterface.TimeDouble simulator) {
        super(simulator);
    }

    @Override
    public void constructModel() throws SimRuntimeException {

        //模型初始化：
        ModelData f1Data = new ModelData("Fleet_1");
        f1Data.origin = f1Data.destination = new CartesianPoint(-200, -50, 0);
//        CoupledModel<Double,Double,SimTimeDouble> c = new CoupledModel<Double, Double, SimTimeDouble>("ss",this.simulator) {
//            @Override
//            public void printModel(String space) {
//                super.printModel(space);
//            }
//        };
        Maneuver m = new Maneuver("m", this.simulator, f1Data);

        // 视图组件注册：
        try {
            Visual2dService.getInstance().register(f1Data.name, simulator, f1Data);

        } catch (NamingException e) {
            SimLogger.always().error(e);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
