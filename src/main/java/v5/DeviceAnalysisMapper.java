import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

/**
 * V5版本 - 设备分析Mapper
 * 提取设备的多维度信息用于综合分析
 * 输入：CSV格式的设备数据
 * 输出：<设备类型, 温度|转速|扭矩|磨损|故障>
 */
public class DeviceAnalysisMapper extends Mapper<LongWritable, Text, Text, Text> {
    
    private Text deviceType = new Text();
    private Text deviceInfo = new Text();
    
    // 字段索引定义
    private static final int TYPE_INDEX = 2;
    private static final int AIR_TEMP_INDEX = 3;
    private static final int SPEED_INDEX = 5;
    private static final int TORQUE_INDEX = 6;
    private static final int WEAR_INDEX = 7;
    private static final int FAILURE_INDEX = 11;
    
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
            if (fields.length <= FAILURE_INDEX) {
                return;
            }
            
            // 提取设备类型
            String type = fields[TYPE_INDEX].trim();
            
            // 提取多维度信息
            double temperature = Double.parseDouble(fields[AIR_TEMP_INDEX].trim());
            double speed = Double.parseDouble(fields[SPEED_INDEX].trim());
            double torque = Double.parseDouble(fields[TORQUE_INDEX].trim());
            double wear = Double.parseDouble(fields[WEAR_INDEX].trim());
            int failure = Integer.parseInt(fields[FAILURE_INDEX].trim());
            
            // 参数验证
            if (temperature < 280 || temperature > 320 || 
                speed < 1000 || speed > 2000 || 
                torque < 0 || torque > 100 || 
                wear < 0 || wear > 300) {
                return;
            }
            
            // 组织输出格式：温度|转速|扭矩|磨损|故障
            String info = String.format("%.2f|%.2f|%.2f|%.2f|%d", 
                    temperature, speed, torque, wear, failure);
            
            deviceInfo.set(info);
            deviceType.set(type);
            
            context.write(deviceType, deviceInfo);
            
        } catch (Exception e) {
            // 忽略格式错误的行
        }
    }
}

