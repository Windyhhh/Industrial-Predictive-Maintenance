import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

/**
 * V4版本 - 故障统计Mapper
 * 使用数组索引方式处理故障类型，提高性能
 * 输入：CSV格式的设备数据
 * 输出：<故障类型, 1>
 */
public class FaultStatisticsMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    
    private Text faultType = new Text();
    private final IntWritable one = new IntWritable(1);
    
    // 故障字段索引（V4版本使用数组索引而非Map）
    private static final int FAULT_FIELD_INDEX = 12;
    
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
            
            // 数据验证：确保字段数足够
            if (fields.length <= FAULT_FIELD_INDEX) {
                return;
            }
            
            // 提取故障字段
            String fault = fields[FAULT_FIELD_INDEX].trim();
            
            // 只处理非空的故障类型
            if (fault != null && !fault.isEmpty() && !fault.equals("0")) {
                faultType.set(fault);
                context.write(faultType, one);
            }
            
        } catch (Exception e) {
            // 忽略格式错误的行
        }
    }
}

