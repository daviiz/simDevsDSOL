package asw.soa.om5.portType;

import asw.soa.data.ModelData;

/**
 * @author daiwenzhi
 */
public class ENT_INFO implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 230299176521011753L;
    /**
     * 红蓝方归属：1 红方，0 中立方，-1 蓝方
     */
    public int belong;
    /**
     * 生存状态： true 生存，false 阵亡
     */
    public boolean status;
    /**
     * 模型实体位置 -x
     */
    public double x;
    /**
     * 模型实体位置 -y
     */
    public double y;

    /**
     * 发送者标识ID
     */
    public String senderId;

    /**
     * 模型实体名称
     */
    public String name;

    public ENT_INFO() {
        this.name = "0";
    }

    public ENT_INFO(String name) {
        this.name = name;
    }

    public ENT_INFO(String name, int _belong, boolean _status, double _x, double _y) {
        this.name = name;
        this.belong = _belong;
        this.status = _status;
        this.x = _x;
        this.y = _y;
    }

    public ENT_INFO(ENT_INFO tmp) {
        this.name = tmp.name;
        this.belong = tmp.belong;
        this.status = tmp.status;
        this.x = tmp.x;
        this.y = tmp.y;
    }

    public ENT_INFO(ModelData tmp) {
        this.name = tmp.name;
        this.belong = tmp.belong;
        this.status = tmp.status;
        this.x = tmp.origin.x;
        this.y = tmp.origin.y;
    }

    public ENT_INFO(MoveResult tmp) {
        this.name = tmp.name;
        this.belong = tmp.belong;
        this.status = true;
        this.x = tmp.x;
        this.y = tmp.y;
    }

    public ENT_INFO(ThreatInfo tmp) {
        this.name = tmp.name;
        this.belong = tmp.belong;
        this.status = true;
        this.x = tmp.x;
        this.y = tmp.y;
    }

}
