package asw.soa.om5.atomicModel;

import asw.soa.om5.inportPort.ControllerIn_MOVE_RESULT;
import asw.soa.om5.inportPort.ControllerIn_THREAT_INFO;
import asw.soa.om5.outportPort.ControllerOut_MOVE_CMD;
import asw.soa.om5.portType.ENT_INFO;
import asw.soa.om5.portType.MoveCmd;
import asw.soa.om5.portType.MoveResult;
import asw.soa.om5.portType.ThreatInfo;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Controller extends AtomicModel<Double,Double, SimTimeDouble> {

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
    private Phase WAIT,IDENTIFICATION;

    /**
     * model's private data
     */
    private MoveResult currentPos;
    private ThreatInfo target;

    public Controller(String modelName, final DEVSSimulatorInterface<Double,Double, SimTimeDouble> simulator) {
        super(modelName, simulator);
        in_MOVE_RESULT = new ControllerIn_MOVE_RESULT(this);
        in_THREAT_INFO = new ControllerIn_THREAT_INFO(this);
        out_MOVE_CMD = new ControllerOut_MOVE_CMD(this);
        WAIT = new Phase("WAIT"); WAIT.setLifeTime(Double.POSITIVE_INFINITY);
        IDENTIFICATION = new Phase("IDENTIFICATION");IDENTIFICATION.setLifeTime(7.0);

        currentPos = new MoveResult();
        target = new ThreatInfo();

        try {
            this.addInputPort("MOVE_RESULT",in_MOVE_RESULT);
            this.addInputPort("THREAT_INFO",in_THREAT_INFO);
            this.addOutputPort("MOVE_CMD",out_MOVE_CMD);
        } catch (PortAlreadyDefinedException e) {
            SimLogger.always().error(e);
        }

        this.phase = WAIT;
        initialize(this.elapsedTime);

    }

    @Override
    protected void deltaInternal() {
        if(this.phase.getName().equals("WAIT")){
            this.phase = IDENTIFICATION;
        }
        if(this.phase.getName().equals("IDENTIFICATION")){

        }
    }

    @Override
    protected void deltaExternal(Double e, Object value) {
        if(this.phase.getLifeTime() != Double.POSITIVE_INFINITY){
            this.phase.setLifeTime(this.phase.getLifeTime()-e);
        }
        if(this.phase.getName().equals("IDENTIFICATION")){
            if(this.activePort == in_MOVE_RESULT){
                //控制器接收自己的机动信息，决策依据
                currentPos = (MoveResult) value;
            }else if(this.activePort == in_THREAT_INFO){
                target = new ThreatInfo((ENT_INFO) value);
            }
        }

    }

    @Override
    protected void lambda() {
        if(this.phase.getName().equals("IDENTIFICATION")){
            if (target.name.equals("0") || currentPos.name.equals("0")) {

            } else {
                MoveCmd msg = new MoveCmd(currentPos, target, "follow");
                msg.senderId = super.modelName;
                out_MOVE_CMD.send(msg);
            }
        }
    }

    @Override
    protected Double timeAdvance() {
        return this.phase.getLifeTime();
    }
}
