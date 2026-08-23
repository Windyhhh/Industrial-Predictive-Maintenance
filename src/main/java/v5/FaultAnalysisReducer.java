import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

/**
 * V5版本 - 故障分析Reducer
 * 统计故障类型与设备类型的关联
 * 输入：<故障类型|设备类型, [描述, ...]>
 * 输出：<故障类型|设备类型, 频次>
 */
public class FaultAnalysisReducer extends Reducer<Text, Text, Text, Text> {
    
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        
        int count = 0;
        String faultDesc = "";
        
        // 统计频次并获取故障描述
        for (Text val : values) {
            count++;
            if (faultDesc.isEmpty()) {
                faultDesc = val.toString();
            }
        }
        
        // 输出格式：故障类型|设备类型 -> 频次|描述
        String result = count + "|" + faultDesc;
        context.write(key, new Text(result));
    }
}

