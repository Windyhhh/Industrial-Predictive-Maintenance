import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * V5版本 - 故障分析Driver
 * 配置并执行MapReduce任务
 * 支持故障类型与设备类型的关联分析
 */
public class FaultAnalysisDriver {
    
    public static void main(String[] args) throws Exception {
        
        Configuration conf = new Configuration();
        
        Job job = Job.getInstance(conf, "Fault Analysis with Device Type");
        
        job.setJarByClass(FaultAnalysisDriver.class);
        job.setMapperClass(FaultAnalysisMapper.class);
        job.setReducerClass(FaultAnalysisReducer.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

