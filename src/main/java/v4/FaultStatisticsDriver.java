import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * V4版本 - 故障统计Driver
 * 配置并执行MapReduce任务
 * 输入路径：/user/hadoop/bigdata/uci/cleandata/
 * 输出路径：/user/hadoop/bigdata/uci/analysis/fault_statistics/
 */
public class FaultStatisticsDriver {
    
    public static void main(String[] args) throws Exception {
        
        // 创建配置对象
        Configuration conf = new Configuration();
        
        // 创建Job对象
        Job job = Job.getInstance(conf, "Fault Statistics Analysis");
        
        // 设置主类
        job.setJarByClass(FaultStatisticsDriver.class);
        
        // 设置Mapper和Reducer
        job.setMapperClass(FaultStatisticsMapper.class);
        job.setReducerClass(FaultStatisticsReducer.class);
        
        // 设置输出键值类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        // 设置输入输出路径
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        // 提交任务并等待完成
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

