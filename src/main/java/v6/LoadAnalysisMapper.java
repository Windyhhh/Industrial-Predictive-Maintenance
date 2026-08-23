import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 负载等级与故障关联分析Mapper（V6版本）
 * 使用Enum枚举类型规范定义负载等级
 * 功能：读取设备数据，提取负载等级、扭矩、故障状态，输出<负载等级, 扭矩|故障>键值对
 */
public class LoadAnalysisMapper extends Mapper<LongWritable, Text, Text, Text> {
    
    private Text loadLevel = new Text();
    private Text loadInfo = new Text();
    
    /**
     * Map函数
     * @param key 输入键（行号）
     * @param value 输入值（CSV行数据）
     * @param context MapReduce上下文
     */
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        
        String line = value.toString();
        
        // 跳过CSV头部
        if (line.startsWith("UDI")) {
            return;
        }
        
        // 分割CSV行数据
        String[] fields = line.split(",");
        
        // 数据格式验证（至少9个字段）
        if (fields.length < 9) {
            return;
        }
        
        try {
            // 提取扭矩（索引6）和故障状态（索引8）
            double torque = Double.parseDouble(fields[6].trim());
            int failure = Integer.parseInt(fields[8].trim());
            
            // 根据扭矩确定负载等级
            LoadLevel level = LoadLevel.fromTorque(torque);
            
            // 组织输出格式：扭矩|故障
            String info = String.format("%.2f|%d", torque, failure);
            
            loadInfo.set(info);
            loadLevel.set(level.name());
            
            // 输出<负载等级, 扭矩|故障>
            context.write(loadLevel, loadInfo);
            
        } catch (Exception e) {
            // 忽略异常数据
            System.err.println("Error processing line: " + line);
        }
    }
}

