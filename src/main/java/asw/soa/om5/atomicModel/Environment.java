package asw.soa.om5.atomicModel;

import asw.soa.om5.inportPort.EnvIn_MOVE_RESULT;
import asw.soa.om5.outportPort.EnvOut_ENT_INFO;
import asw.soa.om5.portType.ENT_INFO;
import asw.soa.om5.portType.MoveResult;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.AtomicModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.CoupledModel;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.Phase;
import nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS.exceptions.PortAlreadyDefinedException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

public class Environment extends AtomicModel<Double, Double, SimTimeDouble> {

    public EnvIn_MOVE_RESULT in_move_result;
    public EnvOut_ENT_INFO out_ent_info;

    MoveResult result;

    private Phase INFINITY;

    public Environment(String modelName, CoupledModel<Double, Double, SimTimeDouble> parentModel) {
        super(modelName, parentModel);
    }

    @Override
    public void initialize(Double e) {
        this.in_move_result = new EnvIn_MOVE_RESULT(this);
        this.out_ent_info = new EnvOut_ENT_INFO(this);
        INFINITY = new Phase("INFINITY");
        INFINITY.setLifeTime(1.0);

        result = new MoveResult();

        /**
         * 输入输出端口设置
         */
        try {
            this.addInputPort("in_move_result", in_move_result);
            this.addOutputPort("out_ent_info", out_ent_info);
        } catch (PortAlreadyDefinedException ex) {
            SimLogger.always().error(ex);
        }
        this.phase = INFINITY;
        this.sigma = this.phase.getLifeTime();
        super.initialize(e);
    }
    //    public Environment(String modelName, DEVSSimulatorInterface<Double, Double, SimTimeDouble> simulator) {
//        super(modelName, simulator);
//        this.in_move_result = new EnvIn_MOVE_RESULT(this);
//        this.out_ent_info = new EnvOut_ENT_INFO(this);
//        INFINITY = new Phase("INFINITY");INFINITY.setLifeTime(1.0);
//
//        result = new MoveResult();
//
//        /**
//         * 输入输出端口设置
//         */
//        try {
//            this.addInputPort("in_move_result",in_move_result);
//            this.addOutputPort("out_ent_info",out_ent_info);
//        } catch (PortAlreadyDefinedException e) {
//            SimLogger.always().error(e);
//        }
//        /**
//         * 模型状态初始化：
//         */
//        this.phase = INFINITY;
//        initialize(this.elapsedTime);
//    }

    @Override
    protected void deltaInternal() {

    }

    @Override
    protected void deltaExternal(Double e, Object value) {
        if (this.phase.getLifeTime() != Double.POSITIVE_INFINITY) {
            this.sigma = (this.phase.getLifeTime() - e);
        }

        if (this.phase.getName().equals("INFINITY")) {
            this.result = (MoveResult) value;
        }
    }

    @Override
    protected void lambda() {
        if (this.phase.getName().equals("INFINITY") && result != null) {
            ENT_INFO s = new ENT_INFO(result);
            result.senderId = super.modelName;
            if (result.name.equals("0")) return;
            out_ent_info.send(s);
        }
    }

    @Override
    protected Double timeAdvance() {
        return this.sigma;
    }
}
