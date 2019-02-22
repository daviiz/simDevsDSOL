package asw.soa.om5.atomicModel;

import asw.soa.om5.inportPort.SensorIn_MOVE_RESULT;
import asw.soa.om5.inportPort.SensorIn_THREAT_ENT_INFO;
import asw.soa.om5.outportPort.SensorOut_THREAT_INFO;
import asw.soa.om5.portType.ENT_INFO;
import asw.soa.om5.portType.MoveResult;
import asw.soa.util.SimUtil;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

public class Sensor extends AtomicModel<Double,Double, SimTimeDouble> {

    /**
     *  X
     */
    public SensorIn_MOVE_RESULT in_MOVE_RESULT;
    public SensorIn_THREAT_ENT_INFO in_THREAT_ENT_INFO;

    /**
     *  Y
     */
    public SensorOut_THREAT_INFO out_THREAT_INFO;

    /**
     * States
     */
    private Phase IDLE,DETECT;

    /**
     * private model's data
     */
    private MoveResult currentPos;
    private double detectRange;
    private ENT_INFO target;

    public Sensor(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel, double detectRange) {
        super(modelName, parentModel);
        /**
         * 模型成员变量实例化
         */
        in_MOVE_RESULT  = new SensorIn_MOVE_RESULT(this);
        in_THREAT_ENT_INFO = new SensorIn_THREAT_ENT_INFO(this);
        out_THREAT_INFO = new SensorOut_THREAT_INFO(this);
        IDLE = new Phase("IDLE");
        DETECT = new Phase("DETECT");
        currentPos = new MoveResult();
        this.detectRange = detectRange;
        target = new ENT_INFO();

        /**
         * 输入输出端口设置
         */
        try {
            this.addInputPort("MOVE_RESULT",in_MOVE_RESULT);
            this.addInputPort("THREAT_ENT_INFO",in_THREAT_ENT_INFO);
            this.addOutputPort("THREAT_INFO",out_THREAT_INFO);
        } catch (PortAlreadyDefinedException e) {
            SimLogger.always().error(e);
        }

        /**
         * 模型状态初始化：
         */
        IDLE.setLifeTime(Double.POSITIVE_INFINITY);
        DETECT.setLifeTime(8.0);
        this.phase = IDLE;
        initialize(this.elapsedTime);
    }

//    /**
//     *
//     * @param modelName
//     * @param simulator
//     * @param detectRange
//     */
//    public Sensor(String modelName, final DEVSSimulatorInterface<Double,Double, SimTimeDouble> simulator, double detectRange) {
//        super(modelName, simulator);
//
//        /**
//         * 模型成员变量实例化
//         */
//        in_MOVE_RESULT  = new SensorIn_MOVE_RESULT(this);
//        in_THREAT_ENT_INFO = new SensorIn_THREAT_ENT_INFO(this);
//        out_THREAT_INFO = new SensorOut_THREAT_INFO(this);
//        IDLE = new Phase("IDLE");
//        DETECT = new Phase("DETECT");
//        currentPos = new MoveResult();
//        this.detectRange = detectRange;
//        target = new ENT_INFO();
//
//        /**
//         * 输入输出端口设置
//         */
//        try {
//            this.addInputPort("MOVE_RESULT",in_MOVE_RESULT);
//            this.addInputPort("THREAT_ENT_INFO",in_THREAT_ENT_INFO);
//            this.addOutputPort("THREAT_INFO",out_THREAT_INFO);
//        } catch (PortAlreadyDefinedException e) {
//            SimLogger.always().error(e);
//        }
//
//        /**
//         * 模型状态初始化：
//         */
//        IDLE.setLifeTime(Double.POSITIVE_INFINITY);
//        DETECT.setLifeTime(8.0);
//        this.phase = IDLE;
//        initialize(this.elapsedTime);
//
//    }

    @Override
    protected void deltaInternal() {
        if(this.phase.getName().equals("IDLE")){
            this.phase = DETECT;
        }
        if(this.phase.getName().equals("DETECT")){

        }
    }

    @Override
    protected void deltaExternal(Double e, Object value) {
        System.out.println("Sensor received Input.......");
        if(this.phase.getLifeTime() != Double.POSITIVE_INFINITY){
            this.phase.setLifeTime(this.phase.getLifeTime()-e);
        }
        if(this.phase.getName().equals("IDLE")){
            this.phase = DETECT;
        }
        if(this.phase.getName().equals("DETECT")){
            if(this.activePort == in_MOVE_RESULT){

                //传感器接收自己的机动信息，决策依据：
                currentPos = (MoveResult) value;

            }else if(this.activePort == in_THREAT_ENT_INFO){

                //传感器接收环境信息，决策依据：
                ENT_INFO ent = (ENT_INFO) value;
                if (ent.belong != currentPos.belong) {
                    double distance = SimUtil.calcLength(currentPos.x, currentPos.y, ent.x, ent.y);
                    if (distance < this.detectRange)
                        target = ent;
                }

            }
        }
    }

    @Override
    protected void lambda() {
        if(this.phase.getName().equals("DETECT")){
            ENT_INFO result = new ENT_INFO(target);
            result.senderId = super.modelName;

            if(result.name.equals("0")) return;

            out_THREAT_INFO.send(result);
        }
    }

    @Override
    protected Double timeAdvance() {
        return this.phase.getLifeTime();
    }
}