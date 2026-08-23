import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

/**
 * V5版本 - 设备分析Reducer
 * 计算设备的综合统计指标
 * 输入：<设备类型, [温度|转速|扭矩|磨损|故障, ...]>
 * 输出：<设备类型, 平均温度|平均转速|平均扭矩|平均磨损|故障数|计数>
 */
public class DeviceAnalysisReducer extends Reducer<Text, Text, Text, Text> {
    
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        
        double sumTemp = 0;
        double sumSpeed = 0;
        double sumTorque = 0;
        double sumWear = 0;
        int faultCount = 0;
        int totalCount = 0;
        
        // 遍历所有设备数据
        for (Text val : values) {
            String[] parts = val.toString().split("\\|");
            
            if (parts.length == 5) {
                try {
                    sumTemp += Double.parseDouble(parts[0]);
                    sumSpeed += Double.parseDouble(parts[1]);
                    sumTorque += Double.parseDouble(parts[2]);
                    sumWear += Double.parseDouble(parts[3]);
                    faultCount += Integer.parseInt(parts[4]);
                    totalCount++;
                } catch (NumberFormatException e) {
                    // 忽略格式错误
                }
            }
        }
        
        // 计算平均值和统计指标
        if (totalCount > 0) {
            double avgTemp = sumTemp / totalCount;
            double avgSpeed = sumSpeed / totalCount;
            double avgTorque = sumTorque / totalCount;
            double avgWear = sumWear / totalCount;
            double faultRate = (double) faultCount / totalCount * 100;
            
            String result = String.format("%.2f|%.2f|%.2f|%.2f|%d|%d|%.2f%%", 
                    avgTemp, avgSpeed, avgTorque, avgWear, faultCount, totalCount, faultRate);
            
            context.write(key, new Text(result));
        }
    }
}

