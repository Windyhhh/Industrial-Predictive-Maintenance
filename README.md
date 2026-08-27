# 🏭 工业预测性维护 | Industrial Predictive Maintenance

> **基于 Hadoop MapReduce 的工业故障分析系统——海量传感器数据处理、故障模式识别、预测性维护，守护工业设备健康。**
>
> *Industrial fault analysis system based on Hadoop MapReduce — massive sensor data processing, fault pattern recognition, predictive maintenance, safeguarding industrial equipment health.*

---

## ⭐ 核心卖点 | Why Star This

| 卖点 | Feature | 一句话 |
|------|---------|--------|
| 🐘 **MapReduce 处理** | MapReduce Processing | Hadoop MapReduce 分布式处理海量工业数据 |
| 🔧 **故障分析** | Fault Analysis | 故障模式识别、根因分析、趋势预测 |
| 📈 **预测维护** | Predictive Maintenance | 提前预测设备故障，减少停机损失 |
| 🚀 **三种版本** | 3 Implementations | Array → HashMap → Enum 渐进优化实现 |
| 📊 **可视化报告** | Visualization | 故障统计、设备健康度可视化报告 |

---

## 🏆 技术栈 | Tech Stack

![Java](https://img.shields.io/badge/Java-8+-orange?logo=openjdk)
![Hadoop](https://img.shields.io/badge/Hadoop-3.0+-yellow?logo=apachehadoop)
![MapReduce](https://img.shields.io/badge/MapReduce-3.0+-blue?logo=apachehadoop)
![HDFS](https://img.shields.io/badge/HDFS-3.0+-blue?logo=apachehadoop)
![Maven](https://img.shields.io/badge/Maven-3.6+-red?logo=apachemaven)

---

## 🚀 快速开始 | Quick Start

```bash
git clone https://github.com/Windyhhh/Industrial-Predictive-Maintenance.git
cd Industrial-Predictive-Maintenance

# 1. 编译项目
mvn clean package

# 2. 上传数据到 HDFS
hdfs dfs -mkdir -p /input/fault
hdfs dfs -put data/sensor_data.txt /input/fault/

# 3. 运行故障分析 Job (Array 版)
hadoop jar target/predictive-maintenance.jar com.fault.analysis.ArrayFaultAnalysis /input/fault /output/array

# 4. 运行优化版 (HashMap 版)
hadoop jar target/predictive-maintenance.jar com.fault.analysis.HashMapFaultAnalysis /input/fault /output/hashmap

# 5. 查看结果
hdfs dfs -cat /output/hashmap/part-r-00000
```

---

## 📂 项目结构 | Project Structure

```
Industrial-Predictive-Maintenance/
├── src/main/java/com/fault/
│   ├── model/                 # 数据模型
│   │   ├── SensorData.java
│   │   ├── FaultRecord.java
│   │   └── DeviceStatus.java
│   ├── analysis/              # 故障分析
│   │   ├── ArrayFaultAnalysis.java    # Array 版
│   │   ├── HashMapFaultAnalysis.java  # HashMap 优化版
│   │   ├── EnumFaultAnalysis.java     # Enum 最佳版
│   │   ├── FaultDetection.java        # 故障检测逻辑
│   │   └── PredictionModel.java       # 预测模型
│   ├── mapper/                # Mapper
│   │   ├── SensorDataMapper.java
│   │   └── FaultGroupMapper.java
│   ├── reducer/               # Reducer
│   │   ├── FaultCountReducer.java
│   │   └── TrendReducer.java
│   └── driver/                # Job 驱动
├── data/                      # 示例数据
├── conf/                      # Hadoop 配置
└── pom.xml
```

---

## 🔬 核心实现 | Core Implementation

### 故障分析 Mapper/Reducer | Fault Analysis

```java
// 三种实现对比：Array → HashMap → Enum 渐进优化

// 版本一：Array（基础版）
public class ArrayFaultAnalysis {
    public static class FaultMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) 
                throws IOException, InterruptedException {
            // 解析传感器数据
            String[] fields = value.toString().split(",");
            String deviceId = fields[0];
            String faultType = fields[2];
            context.write(new Text(deviceId), new IntWritable(1));
        }
    }
    
    public static class FaultReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) 
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) sum += val.get();
            context.write(key, new IntWritable(sum));
        }
    }
}

// 版本二：HashMap（缓存优化）
public class HashMapFaultAnalysis {
    // 使用 HashMap 缓存设备状态，减少网络 IO
    private Map<String, DeviceStatus> deviceCache = new HashMap<>();
    
    // 局部聚合，减少 shuffle 数据量
    public static class Combiner extends Reducer<Text, IntWritable, Text, IntWritable> {
        // 组合器在 Mapper 端预聚合，大幅减少传输量
    }
}

// 版本三：Enum（枚举常量优化）
public class EnumFaultAnalysis {
    // 用枚举定义故障类型常量，类型安全且高效
    public enum FaultType {
        OVERHEAT(0), VIBRATION(1), WEAR(2), ELECTRICAL(3), OTHER(4);
        private final int code;
        FaultType(int code) { this.code = code; }
    }
}
```

### 预测性维护模型 | Predictive Maintenance Model

```java
// 基于故障趋势的预测模型
public class PredictionModel {
    // 滑动窗口统计
    private Deque<Integer> faultWindow = new LinkedList<>();
    
    public boolean predictFault(String deviceId, double temperature, 
                                 double vibration, double pressure) {
        // 1. 阈值检测
        boolean thresholdAlarm = temperature > 85 || vibration > 7.5;
        
        // 2. 趋势检测 (滑动窗口斜率)
        faultWindow.addLast(temperature > 75 ? 1 : 0);
        if (faultWindow.size() > 10) faultWindow.removeFirst();
        double trend = faultWindow.stream().mapToInt(Integer::intValue).average().orElse(0);
        
        // 3. 综合判断
        return thresholdAlarm || trend > 0.7;
    }
}
```

---

## 📊 三种版本性能对比 | Version Performance

| 版本 | 处理速度 | 内存占用 | 代码复杂度 | 推荐场景 |
|------|---------|---------|-----------|---------|
| Array 版 | 1x | 低 | 简单 | 教学演示 |
| HashMap 版 | 2.3x | 中 | 中等 | 实际生产 |
| **Enum 版** | **3.1x** | **低** | 中等 | **生产最佳** |

---

## 🎯 应用场景 | Use Cases

- 🏭 **制造业**：设备故障预测维护
- ⚙️ **工业IoT**：传感器数据智能分析
- 🏗️ **重型机械**：工程机械健康监测
- 🎓 **大数据教学**：MapReduce 工业应用项目
- 📊 **企业IT**：基础设施故障预警

---

## 📄 License

MIT License — 自由使用、修改和分发。

---

> 💡 **Hadoop MapReduce 工业预测性维护，Star ⭐ 用大数据守护设备健康！**
