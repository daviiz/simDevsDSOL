package asw.soa.om5;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSRealTimeClock;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.swing.animation.D2.AnimationPanel;
import nl.tudelft.simulation.dsol.swing.gui.DSOLApplication;
import nl.tudelft.simulation.dsol.swing.gui.DSOLPanel;

import javax.naming.NamingException;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;

public class ASWSwingApplication5 extends DSOLApplication {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param title String; the title
     * @param panel DSOLPanel&lt;Double,Double,SimTimeDouble&gt;; the panel
     */
    public ASWSwingApplication5(final String title, final DSOLPanel<Double, Double, SimTimeDouble> panel) {
        super(title, panel);
    }

    /**
     * @param args String[]; arguments, expected to be empty
     * @throws SimRuntimeException on error
     * @throws RemoteException     on error
     * @throws NamingException     on error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException {
        DEVSRealTimeClock.TimeDouble simulator = new DEVSRealTimeClock.TimeDouble(0.1);
        ASWModel5 model = new ASWModel5(simulator);

        Replication.TimeDouble<DEVSSimulatorInterface.TimeDouble> replication = Replication.TimeDouble.create("rep1",
                0.0, 0.0, 1000000.0, model);
        DSOLPanel<Double, Double, SimTimeDouble> panel = new DSOLPanel<Double, Double, SimTimeDouble>(model, simulator);
        panel.getTabbedPane().add("animation",
                new AnimationPanel(new Rectangle2D.Double(-100, -100, 400, 400), new Dimension(200, 200), simulator));
        panel.getTabbedPane().setSelectedIndex(1);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        new ASWSwingApplication5("Anti-Submarine-Warfare v0.5", panel);
    }

}
