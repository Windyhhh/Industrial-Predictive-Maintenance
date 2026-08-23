# 🏭 工业设备预测性维护数据分析 | Predictive Maintenance Analytics

> **Predictive Maintenance Analytics with Hadoop MapReduce**
>
> 基于 UCI AI4I 2020 数据集，使用 Hadoop MapReduce 完成工业设备故障分析与维护优化。包含 **V4/V5/V6 三个迭代版本**，从数组索引优化到 HashMap 动态配置，再到 Enum 枚举类型规范，逐步演进为企业级代码标准。
>
> Industrial equipment fault analysis and maintenance optimization using Hadoop MapReduce on the UCI AI4I 2020 dataset. Three iterative versions (V4/V5/V6) evolving from array indexing to HashMap to Enum standards.

---

## ✨ 核心亮点

| 维度 | 详情 |
|------|------|
| 📊 数据集 | UCI AI4I 2020，10,000 条设备记录 + 500+ 个性化补充 |
| 🔧 技术栈 | Hadoop MapReduce (Java) + Python 可视化 (numpy/matplotlib) |
| 📈 分析维度 | 故障类型统计、设备参数分析、负载-故障关联、温度风险 |
| 🏗️ 三版本演进 | V4 数组索引 → V5 HashMap → V6 Enum 枚举 |
| 🎯 核心发现 | 负载与故障强正相关 (r=0.89)，重负载故障率是轻负载的 2.96 倍 |
| 📉 数据清洗 | 清洗率 1.64%，有效数据 10,328+ 条 |

---

## 🏗️ 版本演进

### V4 — 数组索引优化

- **自定义字段**：`Maintenance_Cycle`（维护周期：L=30天 / M=45天 / H=60天）
- **技术特点**：数组索引 `FAULT_FIELD_INDEX = 12`，性能高效，内存占用少
- **可视化**：竖柱状图、堆积柱状图、散点图、箱线图
- **适用场景**：大规模数据处理，追求极致性能

### V5 — HashMap 动态配置

- **自定义字段**：`Temperature_Risk`（温度风险等级：低<298K / 中298-305K / 高>305K）
- **技术特点**：HashMap 配置，动态扩展，灵活配置
- **可视化**：热力图、雷达图、散点图、堆积柱状图
- **适用场景**：需要频繁修改配置的复杂交叉分析

### V6 — Enum 枚举规范（推荐）

- **自定义字段**：`Load_Level`（负载等级：轻≤35Nm / 中35-50Nm / 重>50Nm）
- **技术特点**：Java Enum 枚举类型规范定义，代码规范，类型安全
- **可视化**：饼图、折线图、分组柱状图
- **适用场景**：企业级应用开发，管理层决策支持

---

## 📊 核心发现

### 故障分布

| 故障类型 | 英文全称 | 中文说明 | 占比 |
|---------|---------|---------|------|
| **TWF** | Tool Wear Failure | 刀具磨损故障 | 最高频 |
| **HDF** | Heat Dissipation Failure | 散热故障 | 高频 |
| **OSF** | Overstrain Failure | 过载故障 | 高频 |
| PWF | Power Failure | 电力故障 | 较少 |
| RNF | Random Failure | 随机故障 | 最少 |

> 前 3 种故障合计占总故障的 **84.6%**，维护重点应放在刀具维护、散热系统和负载控制。

### 负载与故障关联（V6）

| 负载等级 | 扭矩范围 | 故障率 | 相对倍数 |
|---------|---------|-------|---------|
| 轻负载 (LIGHT) | ≤ 35 Nm | 7.2% | 1.0x |
| 中负载 (MEDIUM) | 35-50 Nm | 13.3% | 1.85x |
| **重负载 (HEAVY)** | **> 50 Nm** | **21.3%** | **2.96x** |

- **相关系数**：r = 0.89（强正相关）
- **最优运行区间**：中负载（35-50 Nm），兼顾产能与设备寿命

### 温度风险（V5）

- 高温设备故障率：**16.7%**（高于低温的 9.8%）
- 高温环境加速设备老化，建议加强散热监控

### 维护周期（V4）

| 设备类型 | 维护周期 | 说明 |
|---------|---------|------|
| L 型 | 30 天 | 低负载，维护间隔短 |
| M 型 | 45 天 | 中负载，标准周期 |
| H 型 | 60 天 | 高负载，但设备更耐用 |

---

## 🚀 快速开始

### 环境要求

```bash
Java >= 8
Hadoop >= 2.7 (HDFS + MapReduce)
Python >= 3.7
numpy >= 1.19
matplotlib >= 3.3
```

### 编译运行 V6（推荐）

```bash
# 进入 V6 目录
cd src/main/java/v6

# 编译 Java 代码
javac -cp $HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/* *.java

# 打包
jar -cvf LoadAnalysisV6.jar *.class

# 上传数据到 HDFS
hdfs dfs -put ../../../data/ai4i2020.csv /input/

# 运行故障统计
hadoop jar LoadAnalysisV6.jar FaultStatisticsDriver /input/ai4i2020.csv /output/fault_stats

# 运行负载分析
hadoop jar LoadAnalysisV6.jar LoadAnalysisDriver /input/ai4i2020.csv /output/load_analysis

# 查看结果
hdfs dfs -cat /output/fault_stats/part-r-00000
```

### 生成可视化图表

```bash
cd src/main/python
python visualization_v6.py
```

输出：
- `fault_distribution_pie.png` — 故障类型分布饼图
- `load_failure_rate_line.png` — 负载等级与故障率折线图
- `load_equipment_fault_bar.png` — 负载等级设备数量与故障数量分组柱状图

---

## 📁 项目结构

```
Industrial-Predictive-Maintenance/
├── README.md                          # 本文件
├── requirements.txt                   # Python 依赖
├── .gitignore                         # Git 忽略规则
├── data/
│   └── ai4i2020.csv                   # UCI AI4I 2020 数据集（10,000条）
├── src/main/java/
│   ├── v4/                            # V4 版本：数组索引优化
│   │   ├── FaultStatisticsMapper.java
│   │   ├── FaultStatisticsReducer.java
│   │   ├── FaultStatisticsDriver.java
│   │   ├── DeviceParameterMapper.java
│   │   ├── DeviceParameterReducer.java
│   │   └── DeviceParameterDriver.java
│   ├── v5/                            # V5 版本：HashMap 动态配置
│   │   ├── FaultAnalysisMapper.java
│   │   ├── FaultAnalysisReducer.java
│   │   ├── FaultAnalysisDriver.java
│   │   ├── DeviceAnalysisMapper.java
│   │   ├── DeviceAnalysisReducer.java
│   │   └── DeviceAnalysisDriver.java
│   └── v6/                            # V6 版本：Enum 枚举规范（推荐）
│       ├── FaultType.java             # 故障类型枚举
│       ├── LoadLevel.java             # 负载等级枚举
│       ├── FaultStatisticsMapper.java
│       ├── FaultStatisticsReducer.java
│       ├── FaultStatisticsDriver.java
│       ├── LoadAnalysisMapper.java
│       ├── LoadAnalysisReducer.java
│       └── LoadAnalysisDriver.java
├── src/main/python/
│   ├── visualization_v4.py            # V4 可视化脚本
│   ├── visualization_v5.py            # V5 可视化脚本
│   └── visualization_v6.py            # V6 可视化脚本
└── docs/
    ├── v4_experiment_report.md        # V4 实验报告
    ├── v5_experiment_report.md        # V5 实验报告
    └── v6_experiment_report.md        # V6 实验报告
```

---

## 📋 数据说明

### AI4I 2020 数据集字段

| 字段 | 类型 | 说明 |
|------|------|------|
| UDI | int | 唯一设备标识符 |
| Product ID | string | 产品编号（L/M/H 前缀表示质量等级） |
| Type | string | 设备类型（L=低质量, M=中等, H=高质量） |
| Air temperature [K] | float | 环境温度（开尔文） |
| Process temperature [K] | float | 工艺温度（开尔文） |
| Rotational speed [rpm] | float | 转速（转/分钟） |
| Torque [Nm] | float | 扭矩（牛米） |
| Tool wear [min] | float | 刀具磨损时间（分钟） |
| Machine failure | int | 机器故障总标签（0/1） |
| TWF | int | 刀具磨损故障（0/1） |
| HDF | int | 散热故障（0/1） |
| PWF | int | 电力故障（0/1） |
| OSF | int | 过载故障（0/1） |
| RNF | int | 随机故障（0/1） |

### 数据统计

- 总记录数：10,000
- 故障设备数：339（3.39%）
- 无故障设备：9,661（96.61%）
- 个性化补充数据：500+ 条
- 清洗后有效数据：10,328+ 条

---

## 🎯 应用场景

- ✅ **预测性维护** — 基于设备参数预测故障风险
- ✅ **维护周期优化** — 按设备类型制定差异化维护计划
- ✅ **负载管理** — 识别最优运行区间，降低故障率
- ✅ **温度监控** — 高温预警，防止设备过热老化
- ✅ **故障根因分析** — 多维度交叉分析故障原因

---

## 📄 许可证

MIT License — 可自由使用、修改和分发。

---

## 🤝 引用

```bibtex
@misc{predictive-maintenance-hadoop2025,
  title={Industrial Predictive Maintenance Analytics with Hadoop MapReduce},
  author={Windyhhh},
  year={2025},
  howpublished={\url{https://github.com/Windyhhh/Industrial-Predictive-Maintenance}}
}
```

---

<div align="center">

**🏭 数据驱动的智能维护，让每一台设备健康运转 🏭**

[报告问题](https://github.com/Windyhhh/Industrial-Predictive-Maintenance/issues) · [提出建议](https://github.com/Windyhhh/Industrial-Predictive-Maintenance/issues)

</div>
