import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 故障类型统计Mapper（V6版本）
 * 使用Enum枚举类型规范定义故障类型
 * 功能：读取设备数据，提取故障类型，输出<故障类型, 1>键值对
 */
public class FaultStatisticsMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    
    private Text faultType = new Text();
    private final static IntWritable one = new IntWritable(1);
    
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
        
        // 数据格式验证（至少14个字段）
        if (fields.length < 14) {
            return;
        }
        
        try {
            // 检查五种故障类型（索引9-13）
            // TWF（Tool Wear Failure）- 索引9
            if (fields[9].trim().equals("1")) {
                faultType.set(FaultType.TWF.name());
                context.write(faultType, one);
            }
            
            // HDF（Heat Dissipation Failure）- 索引10
            if (fields[10].trim().equals("1")) {
                faultType.set(FaultType.HDF.name());
                context.write(faultType, one);
            }
            
            // PWF（Power Failure）- 索引11
            if (fields[11].trim().equals("1")) {
                faultType.set(FaultType.PWF.name());
                context.write(faultType, one);
            }
            
            // OSF（Overstrain Failure）- 索引12
            if (fields[12].trim().equals("1")) {
                faultType.set(FaultType.OSF.name());
                context.write(faultType, one);
            }
            
            // RNF（Random Failure）- 索引13
            if (fields[13].trim().equals("1")) {
                faultType.set(FaultType.RNF.name());
                context.write(faultType, one);
            }
            
        } catch (Exception e) {
            // 忽略异常数据
            System.err.println("Error processing line: " + line);
        }
    }
}

