import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 负载等级与故障关联分析Driver（V6版本）
 * 功能：配置并启动MapReduce任务2（负载等级与故障关联分析）
 */
public class LoadAnalysisDriver {
    
    /**
     * 主函数
     * @param args 命令行参数：[输入路径, 输出路径]
     */
    public static void main(String[] args) throws Exception {
        
        // 参数验证
        if (args.length != 2) {
            System.err.println("Usage: LoadAnalysisDriver <input path> <output path>");
            System.exit(1);
        }
        
        // 创建配置对象
        Configuration conf = new Configuration();
        
        // 创建Job对象
        Job job = Job.getInstance(conf, "Load Level Analysis");
        
        // 设置主类
        job.setJarByClass(LoadAnalysisDriver.class);
        
        // 设置Mapper和Reducer
        job.setMapperClass(LoadAnalysisMapper.class);
        job.setReducerClass(LoadAnalysisReducer.class);
        
        // 设置输出键值类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        // 设置输入输出路径
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        // 提交任务并等待完成
        int exitCode = job.waitForCompletion(true) ? 0 : 1;
        
        System.exit(exitCode);
    }
}

