package asw.soa.om5.atomicModel;

import asw.soa.data.ModelData;
import asw.soa.om5.inportPort.ManeuverIn_MOVE_CMD;
import asw.soa.om5.outportPort.ManeuverOut_MOVE_RESULT;
import asw.soa.om5.portType.ENT_INFO;
import asw.soa.om5.portType.MoveCmd;
import asw.soa.om5.portType.MoveResult;
import asw.soa.util.SimUtil;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.language.d3.CartesianPoint;

public class Maneuver2 extends AtomicModel<Double, Double, SimTimeDouble> {

    /**
     * 模型输入端口 - X
     */
    public ManeuverIn_MOVE_CMD in_MOVE_CMD;
    /**
     * 模型输出端口 - Y
     */
    public ManeuverOut_MOVE_RESULT out_MOVE_RESULT;

    /**
     * 模型状态集合 - States
     */
    private Phase IDLE, MOVE, FUEL;

    /**
     * 模型私有数据
     */
    private ModelData data;
    private ENT_INFO target;
    private MoveCmd moveCmd;

    public Maneuver2(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel, ModelData data) {
        super(modelName, parentModel);
        this.data = data;
    }

    @Override
    public void initialize(Double e) {

        this.target = new ENT_INFO();
        this.moveCmd = new MoveCmd();
        in_MOVE_CMD = new ManeuverIn_MOVE_CMD(this);
        out_MOVE_RESULT = new ManeuverOut_MOVE_RESULT(this);
        IDLE = new Phase("IDLE");
        IDLE.setLifeTime(Double.POSITIVE_INFINITY);
        MOVE = new Phase("MOVE");
        MOVE.setLifeTime(30);
        FUEL = new Phase("FUEL");
        FUEL.setLifeTime(0);


        /**
         * 输入输出端口设置
         */
        try {
            this.addInputPort("MOVE_CMD", in_MOVE_CMD);
            this.addOutputPort("MOVE_RESULT", out_MOVE_RESULT);
        } catch (PortAlreadyDefinedException ex) {
            SimLogger.always().error(ex);
        }
        /**
         * 模型状态初始化：
         */
        this.phase = MOVE;
        //this.sigma = this.phase.getLifeTime();
        super.initialize(e);
    }
    //    /**
//     * 模型构造器
//     * @param modelName
//     * @param simulator
//     * @param data
//     */
//    public Maneuver(String modelName, final DEVSSimulatorInterface<Double,Double, SimTimeDouble> simulator, ModelData data){
//        /**
//         * 模型成员变量实例化
//         */
//        super(modelName,simulator);
//        this.data = data;
//        this.target = new ENT_INFO();
//        this.moveCmd = new MoveCmd();
//        in_MOVE_CMD = new ManeuverIn_MOVE_CMD(this);
//        out_MOVE_RESULT = new  ManeuverOut_MOVE_RESULT(this);
//        IDLE = new Phase("IDLE");
//        IDLE.setLifeTime(Double.POSITIVE_INFINITY);
//        MOVE = new Phase("MOVE");
//        MOVE.setLifeTime(10.0);
//        FUEL = new Phase("FUEL");
//        FUEL.setLifeTime(0);
//
//
//        /**
//         * 输入输出端口设置
//         */
//        try {
//            this.addInputPort("MOVE_CMD",in_MOVE_CMD);
//            this.addOutputPort("MOVE_RESULT",out_MOVE_RESULT);
//        } catch (PortAlreadyDefinedException e) {
//            SimLogger.always().error(e);
//        }
//        /**
//         * 模型状态初始化：
//         */
//        this.phase = MOVE;
//        initialize(this.elapsedTime);
//    }


    /**
     * the delta internal function that should be implemented by the extending class.
     */
    @Override
    protected void deltaInternal() {

        if (super.phase.getName().equals("IDLE")) {
            this.phase = MOVE;
        }
        if (super.phase.getName().equals("MOVE")) {
            this.data.origin = this.data.destination;

            if(this.moveCmd.cmd.equals("0")||this.moveCmd.threat.name.equals("0") || this.moveCmd.currentPos.name.equals("0") || this.target.name.equals("0") || this.data.status==false){
                this.data.destination = new CartesianPoint(data.destination.x, data.destination.y, 0);
            }else{
                boolean isFollow = this.moveCmd.cmd.equals("follow");
                data.destination = SimUtil.nextPoint(data.origin.x, data.origin.y, target.x,
                        target.y, data.speed, isFollow);
            }
            data.startTime = this.simulator.getSimulatorTime();
            data.stopTime = data.startTime + this.phase.getLifeTime();
        }
    }

    /**
     * The user defined deltaExternal method that is defined in an extension of this class.
     *
     * @param e     R; the elapsed time since the last state transition
     * @param value Object; the value that has been passed through the port
     */
    @Override
    protected synchronized void deltaExternal(Double e, Object value) {

        this.elapsedTime =  this.elapsedTime + e;

        if (this.phase.getName().equals("MOVE")) {
            this.moveCmd = (MoveCmd) value;

            this.target = new ENT_INFO(this.moveCmd.threat);
            //System.out.println("--" + this.modelName+" ---Input: "+moveCmd.toString()+", SimTime: " + this.simulator.getSimulatorTime());
        }
    }

    /**
     * the lambda function that should be implemented by the extending class.
     */
    @Override
    protected void lambda() {
        System.out.println("--" + this.modelName+" --Output: "+"lambda"+", SimTime: " + this.simulator.getSimulatorTime());
        if (this.phase.getName().equals("MOVE")) {
            MoveResult result = new MoveResult(data);
            result.senderId = super.modelName;

            if (result.name.equals("0")) return;

            //System.out.println("--" + this.modelName+" --Output: "+result.toString()+", SimTime: " + this.simulator.getSimulatorTime());
            out_MOVE_RESULT.send(result);
        }
    }

    /**
     * the time advance function that should be implemented by the extending class.
     *
     * @return the ta, which is the time advance from one state to the next.
     */
    @Override
    protected Double timeAdvance()
    {
        return this.phase.getLifeTime();
    }
}
