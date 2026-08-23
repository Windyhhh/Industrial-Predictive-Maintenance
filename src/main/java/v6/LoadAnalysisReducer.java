import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 负载等级与故障关联分析Reducer（V6版本）
 * 功能：聚合相同负载等级的数据，计算平均扭矩、故障率等统计指标
 */
public class LoadAnalysisReducer extends Reducer<Text, Text, Text, Text> {
    
    private Text result = new Text();
    
    /**
     * Reduce函数
     * @param key 输入键（负载等级）
     * @param values 输入值集合（扭矩|故障数据）
     * @param context MapReduce上下文
     */
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        
        double totalTorque = 0.0;
        int totalCount = 0;
        int faultCount = 0;
        
        // 遍历所有数据，计算统计指标
        for (Text val : values) {
            String[] parts = val.toString().split("\\|");
            if (parts.length == 2) {
                try {
                    double torque = Double.parseDouble(parts[0]);
                    int failure = Integer.parseInt(parts[1]);
                    
                    totalTorque += torque;
                    totalCount++;
                    
                    if (failure == 1) {
                        faultCount++;
                    }
                } catch (Exception e) {
                    // 忽略异常数据
                }
            }
        }
        
        // 计算平均扭矩和故障率
        if (totalCount > 0) {
            double avgTorque = totalTorque / totalCount;
            double faultRate = (double) faultCount / totalCount * 100;
            
            // 组织输出格式
            String output = String.format(
                "设备数量:%d\t平均扭矩:%.1fNm\t故障数:%d\t故障率:%.1f%%",
                totalCount, avgTorque, faultCount, faultRate
            );
            
            result.set(output);
            context.write(key, result);
        }
    }
}

