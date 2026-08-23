import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

/**
 * V4版本 - 设备参数Reducer
 * 计算每种设备类型的平均温度、转速、扭矩
 * 输入：<设备类型, [温度|转速|扭矩, ...]>
 * 输出：<设备类型, 平均温度|平均转速|平均扭矩|计数>
 */
public class DeviceParameterReducer extends Reducer<Text, Text, Text, Text> {
    
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        
        double sumTemp = 0;
        double sumSpeed = 0;
        double sumTorque = 0;
        int count = 0;
        
        // 遍历所有参数值
        for (Text val : values) {
            String[] params = val.toString().split("\\|");
            
            if (params.length == 3) {
                try {
                    sumTemp += Double.parseDouble(params[0]);
                    sumSpeed += Double.parseDouble(params[1]);
                    sumTorque += Double.parseDouble(params[2]);
                    count++;
                } catch (NumberFormatException e) {
                    // 忽略格式错误
                }
            }
        }
        
        // 计算平均值
        if (count > 0) {
            double avgTemp = sumTemp / count;
            double avgSpeed = sumSpeed / count;
            double avgTorque = sumTorque / count;
            
            String result = String.format("%.2f|%.2f|%.2f|%d", 
                    avgTemp, avgSpeed, avgTorque, count);
            
            context.write(key, new Text(result));
        }
    }
}

