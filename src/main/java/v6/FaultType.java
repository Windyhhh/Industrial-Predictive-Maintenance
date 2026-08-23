/**
 * 故障类型枚举类（V6版本）
 * 使用Enum规范定义五种故障类型，提高代码可读性和可维护性
 */
public enum FaultType {
    TWF("Tool Wear Failure", "刀具磨损故障"),
    HDF("Heat Dissipation Failure", "散热故障"),
    OSF("Overstrain Failure", "过载故障"),
    PWF("Power Failure", "电力故障"),
    RNF("Random Failure", "随机故障");
    
    private final String englishName;
    private final String chineseName;
    
    /**
     * 构造函数
     * @param englishName 英文名称
     * @param chineseName 中文名称
     */
    FaultType(String englishName, String chineseName) {
        this.englishName = englishName;
        this.chineseName = chineseName;
    }
    
    /**
     * 获取英文名称
     * @return 英文名称
     */
    public String getEnglishName() {
        return englishName;
    }
    
    /**
     * 获取中文名称
     * @return 中文名称
     */
    public String getChineseName() {
        return chineseName;
    }
    
    /**
     * 根据字符串获取故障类型枚举
     * @param name 故障类型名称
     * @return 对应的枚举值，如果不存在则返回null
     */
    public static FaultType fromString(String name) {
        for (FaultType type : FaultType.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        return null;
    }
}

