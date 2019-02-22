package asw.soa.om5;

import asw.soa.data.ModelData;
import asw.soa.om5.atomicModel.Environment;
import asw.soa.om5.atomicModel.Maneuver;
import asw.soa.om5.coupledModel.Fleet;
import asw.soa.om5.coupledModel.RootCoupledModel;
import asw.soa.om5.coupledModel.Submarine;
import asw.soa.view.Visual2dService;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
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
        ModelData s1Data = new ModelData("Sub_1");
        s1Data.origin = s1Data.destination = new CartesianPoint(200, 100, 0);

        new RootCoupledModel("root",simulator,s1Data,s1Data);

        //Maneuver m = new Maneuver("m", this.simulator, f1Data);
        //Maneuver m2 = new Maneuver("m", this.simulator, s1Data);

//        Fleet fleet = new Fleet(f1Data.name,this.simulator,f1Data);
//        Submarine sub = new Submarine(s1Data.name,this.simulator,s1Data);
//        Environment env = new Environment("env",this.simulator);


        // 视图组件注册：
        try {
            Visual2dService.getInstance().register(f1Data.name, simulator, f1Data);
            Visual2dService.getInstance().register(s1Data.name, simulator, s1Data);

        } catch (NamingException e) {
            SimLogger.always().error(e);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
