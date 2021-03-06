package asw.soa.om5.atomicModel;

import asw.soa.om5.inportPort.ControllerIn_MOVE_RESULT;
import asw.soa.om5.inportPort.ControllerIn_THREAT_INFO;
import asw.soa.om5.outportPort.ControllerOut_MOVE_CMD;
import asw.soa.om5.portType.ENT_INFO;
import asw.soa.om5.portType.MoveCmd;
import asw.soa.om5.portType.MoveResult;
import asw.soa.om5.portType.ThreatInfo;
import asw.soa.util.SimUtil;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class Controller2 extends AtomicModel<Double, Double, SimTimeDouble> {

    /**
     * X
     */
    public ControllerIn_MOVE_RESULT in_MOVE_RESULT;
    public ControllerIn_THREAT_INFO in_THREAT_INFO;
    /**
     * Y
     */
    public ControllerOut_MOVE_CMD out_MOVE_CMD;

    /**
     * States
     */
    private Phase WAIT, IDENTIFICATION;

    /**
     * model's private data
     */
    private MoveResult currentPos;
    private ThreatInfo target;

    public Controller2(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel) {
        super(modelName, parentModel);
    }

    @Override
    public void initialize(Double e) {
        in_MOVE_RESULT = new ControllerIn_MOVE_RESULT(this);
        in_THREAT_INFO = new ControllerIn_THREAT_INFO(this);
        out_MOVE_CMD = new ControllerOut_MOVE_CMD(this);
        WAIT = new Phase("WAIT");
        WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        IDENTIFICATION = new Phase("IDENTIFICATION");
        IDENTIFICATION.setLifeTime(30.0);

        currentPos = new MoveResult();
        target = new ThreatInfo();

        try {
            this.addInputPort("MOVE_RESULT", in_MOVE_RESULT);
            this.addInputPort("THREAT_INFO", in_THREAT_INFO);
            this.addOutputPort("MOVE_CMD", out_MOVE_CMD);
        } catch (PortAlreadyDefinedException ex) {
            SimLogger.always().error(ex);
        }

        this.phase = IDENTIFICATION;
        this.sigma = this.phase.getLifeTime();
        super.initialize(e);
    }
    //    public Controller(String modelName, final DEVSSimulatorInterface<Double,Double, SimTimeDouble> simulator) {
//        super(modelName, simulator);
//        in_MOVE_RESULT = new ControllerIn_MOVE_RESULT(this);
//        in_THREAT_INFO = new ControllerIn_THREAT_INFO(this);
//        out_MOVE_CMD = new ControllerOut_MOVE_CMD(this);
//        WAIT = new Phase("WAIT"); WAIT.setLifeTime(Double.POSITIVE_INFINITY);
//        IDENTIFICATION = new Phase("IDENTIFICATION");IDENTIFICATION.setLifeTime(7.0);
//
//        currentPos = new MoveResult();
//        target = new ThreatInfo();
//
//        try {
//            this.addInputPort("MOVE_RESULT",in_MOVE_RESULT);
//            this.addInputPort("THREAT_INFO",in_THREAT_INFO);
//            this.addOutputPort("MOVE_CMD",out_MOVE_CMD);
//        } catch (PortAlreadyDefinedException e) {
//            SimLogger.always().error(e);
//        }
//
//        this.phase = WAIT;
//        initialize(this.elapsedTime);
//
//    }

    @Override
    protected void deltaInternal() {

    }

    @Override
    protected void deltaExternal(Double e, Object value) {
        this.elapsedTime =  e;
        System.out.println("--" + this.modelName+" --Input: "+"deltaExternal"+", SimTime: " + this.simulator.getSimulatorTime());

        if (this.phase.getName().equals("WAIT")) {
            this.phase = IDENTIFICATION;
        }
        if (this.phase.getName().equals("IDENTIFICATION")) {
            if (this.activePort == in_MOVE_RESULT) {
                //控制器接收自己的机动信息，决策依据
                currentPos = (MoveResult) value;
                //System.out.println("--" + this.modelName+" --Input: "+currentPos.toString()+", SimTime: " + this.simulator.getSimulatorTime());
            } else if (this.activePort == in_THREAT_INFO) {
                target = new ThreatInfo((ENT_INFO) value);
                //System.out.println("--" + this.modelName+" --Input: "+target.toString()+", SimTime: " + this.simulator.getSimulatorTime());
            }
        }

    }

    @Override
    protected synchronized void lambda() {
        //System.out.println("--" + this.modelName+" --Output: "+"lambda"+", SimTime: " + this.simulator.getSimulatorTime());
        //System.out.println("---" + this.modelName+"---lambda, SimTime: " + this.simulator.getSimulatorTime());
        if (this.phase.getName().equals("IDENTIFICATION")) {

            MoveCmd msg = new MoveCmd(currentPos, target, "follow");
            msg.senderId = super.modelName;
            //System.out.println("--" + this.modelName+" --Output: "+msg.toString()+", SimTime: " + this.simulator.getSimulatorTime());
            out_MOVE_CMD.send(msg);

        }
    }

    @Override
    protected Double timeAdvance() {
        return this.phase.getLifeTime()+this.elapsedTime;
    }
}
