import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

/**
 * V4版本 - 故障统计Reducer
 * 聚合相同故障类型的计数
 * 输入：<故障类型, [1, 1, 1, ...]>
 * 输出：<故障类型, 总频次>
 */
public class FaultStatisticsReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    
    private IntWritable result = new IntWritable();
    
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        
        // 累加相同故障类型的计数
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        
        result.set(sum);
        context.write(key, result);
    }
}

