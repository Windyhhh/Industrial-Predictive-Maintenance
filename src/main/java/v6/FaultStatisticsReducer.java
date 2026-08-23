import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 故障类型统计Reducer（V6版本）
 * 功能：聚合相同故障类型的值，统计每种故障的出现频次
 */
public class FaultStatisticsReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    
    private IntWritable result = new IntWritable();
    
    /**
     * Reduce函数
     * @param key 输入键（故障类型）
     * @param values 输入值集合（所有1的集合）
     * @param context MapReduce上下文
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        
        int sum = 0;
        
        // 累加所有值
        for (IntWritable val : values) {
            sum += val.get();
        }
        
        // 输出<故障类型, 频次>
        result.set(sum);
        context.write(key, result);
    }
}

