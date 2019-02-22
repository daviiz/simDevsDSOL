package asw.soa.om5;

import asw.soa.data.ModelData;
import asw.soa.util.SimUtil;
import asw.soa.om5.inportPort.ManeuverIn_MOVE_CMD;
import asw.soa.om5.outportPort.ManeuverOut_MOVE_RESULT;
import asw.soa.om5.portType.ENT_INFO;
import asw.soa.om5.portType.MoveCmd;
import asw.soa.om5.portType.MoveResult;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.language.d3.CartesianPoint;

public class Maneuver extends AtomicModel<Double,Double, SimTimeDouble>
{
    /**
     * 模型状态集合 - States
     */
    private Phase IDLE,MOVE,FUEL;
    /**
     * 模型输入端口 - X
     */
    private ManeuverIn_MOVE_CMD MOVE_CMD;
    /**
     * 模型输出端口 - Y
     */
    private ManeuverOut_MOVE_RESULT  MOVE_RESULT;

    /**
     * 模型私有数据
     */
    private ModelData data;
    private ENT_INFO target;
    private MoveCmd moveCmd;

    /**
     * 模型构造器
     * @param modelName
     * @param simulator
     * @param data
     */
    public Maneuver(String modelName, final DEVSSimulatorInterface<Double,Double, SimTimeDouble> simulator, ModelData data){
        /**
         * 模型成员变量实例化
         */
        super(modelName,simulator);
        this.data = data;
        this.target = new ENT_INFO();
        this.moveCmd = new MoveCmd();
        MOVE_CMD = new ManeuverIn_MOVE_CMD(this);
        MOVE_RESULT = new  ManeuverOut_MOVE_RESULT(this);
        IDLE = new Phase("IDLE");
        IDLE.setLifeTime(Double.POSITIVE_INFINITY);
        MOVE = new Phase("MOVE");
        MOVE.setLifeTime(10.0);
        FUEL = new Phase("FUEL");
        FUEL.setLifeTime(0);


        /**
         * 输入输出端口设置
         */
        try {
            this.addInputPort("MOVE_CMD",MOVE_CMD);
            this.addOutputPort("MOVE_RESULT",MOVE_RESULT);
        } catch (PortAlreadyDefinedException e) {
            SimLogger.always().error(e);
        }
        /**
         * 模型状态初始化：
         */
        this.phase = MOVE;
        initialize(this.elapsedTime);
    }

    /**
     * 模型构造器-2
     * @param modelName
     * @param parentModel
     * @param data
     */
    public Maneuver(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel,ModelData data) {
        super(modelName, parentModel);

        this.data = data;
        this.target = new ENT_INFO();
        this.moveCmd = new MoveCmd();

        IDLE = new Phase("IDLE");
        IDLE.setLifeTime(Double.POSITIVE_INFINITY);

        MOVE = new Phase("MOVE");
        MOVE.setLifeTime(10.0);

        FUEL = new Phase("FUEL");
        FUEL.setLifeTime(0);

        this.phase = MOVE;

        try {
            MOVE_CMD = new ManeuverIn_MOVE_CMD(this);
            MOVE_RESULT = new  ManeuverOut_MOVE_RESULT(this);
            this.addInputPort("MOVE_CMD",MOVE_CMD);
            this.addOutputPort("MOVE_RESULT",MOVE_RESULT);
        } catch (PortAlreadyDefinedException e) {
            SimLogger.always().error(e);
        }
        initialize(this.elapsedTime);
    }

    /**
     * the delta internal function that should be implemented by the extending class.
     */
    @Override
    protected  void deltaInternal(){
        if(super.phase.getName().equals("MOVE")){
            this.data.origin = this.data.destination;

            if (!this.data.status) {
                this.data.destination = new CartesianPoint(data.destination.x, data.destination.y, 0);
            } else if (this.target.name.equals("0")) {
                data.destination = new CartesianPoint(data.destination.x + data.speed, data.destination.y + data.speed,
                        0);
            } else {

                boolean isFollow = this.moveCmd.cmd.equals("follow");
                //System.out.println(data.name+"----------"+isFollow+"--------------"+target.name);
                data.destination = SimUtil.nextPoint(data.origin.x, data.origin.y, target.x,
                        target.y, data.speed, isFollow);
            }
            data.startTime = this.simulator.getSimulatorTime();
            data.stopTime = data.startTime + this.phase.getLifeTime();
        }
    }

    /**
     * The user defined deltaExternal method that is defined in an extension of this class.
     * @param e R; the elapsed time since the last state transition
     * @param value Object; the value that has been passed through the port
     */
    @Override
    protected  void deltaExternal(Double e, Object value){
        if(this.phase.getLifeTime() != Double.POSITIVE_INFINITY){
            this.phase.setLifeTime(this.phase.getLifeTime()-e);
        }
    }

    /**
     * the lambda function that should be implemented by the extending class.
     */
    @Override
    protected  void lambda(){
        if(super.phase.getName().equals("MOVE")){
            MoveResult result = new MoveResult(data);
            MOVE_RESULT.send(result);
        }
    }

    /**
     * the time advance function that should be implemented by the extending class.
     * @return the ta, which is the time advance from one state to the next.
     */
    @Override
    protected  Double timeAdvance(){
        return super.phase.getLifeTime();
    }
}
