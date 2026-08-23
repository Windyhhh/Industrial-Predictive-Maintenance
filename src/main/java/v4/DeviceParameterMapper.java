import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

/**
 * V4版本 - 设备参数Mapper
 * 按设备类型分组，提取温度、转速、扭矩等参数
 * 输入：CSV格式的设备数据
 * 输出：<设备类型, 温度|转速|扭矩>
 */
public class DeviceParameterMapper extends Mapper<LongWritable, Text, Text, Text> {
    
    private Text deviceType = new Text();
    private Text parameters = new Text();
    
    // 字段索引定义
    private static final int TYPE_INDEX = 2;
    private static final int AIR_TEMP_INDEX = 3;
    private static final int SPEED_INDEX = 5;
    private static final int TORQUE_INDEX = 6;
    
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
            if (fields.length <= TORQUE_INDEX) {
                return;
            }
            
            // 提取设备类型
            String type = fields[TYPE_INDEX].trim();
            
            // 提取参数
            double temperature = Double.parseDouble(fields[AIR_TEMP_INDEX].trim());
            double speed = Double.parseDouble(fields[SPEED_INDEX].trim());
            double torque = Double.parseDouble(fields[TORQUE_INDEX].trim());
            
            // 参数验证
            if (temperature < 280 || temperature > 320 || 
                speed < 1000 || speed > 2000 || 
                torque < 0 || torque > 100) {
                return;
            }
            
            // 组织输出格式
            String paramStr = String.format("%.2f|%.2f|%.2f", temperature, speed, torque);
            parameters.set(paramStr);
            deviceType.set(type);
            
            context.write(deviceType, parameters);
            
        } catch (Exception e) {
            // 忽略格式错误的行
        }
    }
}

