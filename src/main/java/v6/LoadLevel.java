/**
 * 负载等级枚举类（V6版本）
 * 使用Enum规范定义三种负载等级，基于扭矩（Torque）划分
 */
public enum LoadLevel {
    LIGHT("轻负载", 0, 35),
    MEDIUM("中负载", 35, 50),
    HEAVY("重负载", 50, 100);
    
    private final String chineseName;
    private final double minTorque;
    private final double maxTorque;
    
    /**
     * 构造函数
     * @param chineseName 中文名称
     * @param minTorque 最小扭矩（Nm）
     * @param maxTorque 最大扭矩（Nm）
     */
    LoadLevel(String chineseName, double minTorque, double maxTorque) {
        this.chineseName = chineseName;
        this.minTorque = minTorque;
        this.maxTorque = maxTorque;
    }
    
    /**
     * 获取中文名称
     * @return 中文名称
     */
    public String getChineseName() {
        return chineseName;
    }
    
    /**
     * 获取最小扭矩
     * @return 最小扭矩（Nm）
     */
    public double getMinTorque() {
        return minTorque;
    }
    
    /**
     * 获取最大扭矩
     * @return 最大扭矩（Nm）
     */
    public double getMaxTorque() {
        return maxTorque;
    }
    
    /**
     * 根据扭矩值确定负载等级
     * @param torque 扭矩值（Nm）
     * @return 对应的负载等级枚举
     */
    public static LoadLevel fromTorque(double torque) {
        if (torque <= 35) {
            return LIGHT;
        } else if (torque <= 50) {
            return MEDIUM;
        } else {
            return HEAVY;
        }
    }
    
    /**
     * 检查扭矩是否在当前负载等级范围内
     * @param torque 扭矩值（Nm）
     * @return 如果在范围内返回true，否则返回false
     */
    public boolean isInRange(double torque) {
        return torque > minTorque && torque <= maxTorque;
    }
}

