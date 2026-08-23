import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * V5版本 - 故障分析Mapper
 * 使用HashMap缓存故障配置，支持动态扩展
 * 输入：CSV格式的设备数据
 * 输出：<故障类型|设备类型, 1>
 */
public class FaultAnalysisMapper extends Mapper<LongWritable, Text, Text, Text> {
    
    private Text outputKey = new Text();
    private Text outputValue = new Text();
    
    // 故障类型配置（V5版本使用HashMap）
    private static final Map<String, String> FAULT_CONFIG = new HashMap<>();
    static {
        FAULT_CONFIG.put("TWF", "刀具磨损");
        FAULT_CONFIG.put("HDF", "散热故障");
        FAULT_CONFIG.put("OSF", "过载故障");
        FAULT_CONFIG.put("PWF", "电力故障");
        FAULT_CONFIG.put("RNF", "随机故障");
    }
    
    // 字段索引
    private static final int TYPE_INDEX = 2;
    private static final int FAULT_INDEX = 12;
    
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        
        String line = value.toString();
        
        // 跳过CSV头部
        if (line.startsWith("UDI")) {
            return;
        }
        
        try {
            String[] fields = line.split(",");
            
            // 数据验证
            if (fields.length <= FAULT_INDEX) {
                return;
            }
            
            String deviceType = fields[TYPE_INDEX].trim();
            String fault = fields[FAULT_INDEX].trim();
            
            // 检查故障是否在配置中
            if (FAULT_CONFIG.containsKey(fault)) {
                String faultDesc = FAULT_CONFIG.get(fault);
                outputKey.set(fault + "|" + deviceType);
                outputValue.set(faultDesc);
                context.write(outputKey, outputValue);
            }
            
        } catch (Exception e) {
            // 忽略格式错误的行
        }
    }
}

