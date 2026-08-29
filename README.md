<div align="center">

# 工业预测性维护 | Industrial-Predictive-Maintenance

### Hadoop MapReduce industrial fault analysis.

Three progressive MapReduce implementations (Array → HashMap → Enum) over the AI4I 2020 predictive-maintenance dataset.

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-8+-007396?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Hadoop](https://img.shields.io/badge/Hadoop-3-66CCFF?logo=apachehadoop&logoColor=black)](https://hadoop.apache.org/)

</div>

---

**Industrial-Predictive-Maintenance** performs industrial fault analysis with **Hadoop MapReduce** on the **AI4I 2020** predictive-maintenance dataset — featuring three progressively optimized implementations (**Array → HashMap → Enum**) across packages `v4`, `v5` and `v6`.

> [!NOTE]
> 中文项目：Hadoop MapReduce 工业故障分析——3 种版本（Array → HashMap → Enum），基于 AI4I 2020 数据集。

---

## Quickstart

```bash
git clone https://github.com/Windyhhh/Industrial-Predictive-Maintenance.git
cd Industrial-Predictive-Maintenance

mvn clean package

hadoop jar target/...jar com.hadoop.v6.FaultStatisticsDriver /input /output
```

Data (`ai4i2020.csv`) ships in `data/`.

---

## Features

- **Three implementations** — Array (`v4`), HashMap (`v5`), Enum (`v6`) to show optimization trade-offs.
- **AI4I 2020** — real predictive-maintenance dataset.
- **Fault & device analysis** — device parameters, fault statistics, load analysis jobs.

---

## Project Structure

```
Industrial-Predictive-Maintenance/
├── src/main/java/v4/    # DeviceParameter, FaultStatistics (Array)
├── src/main/java/v5/    # DeviceAnalysis, FaultAnalysis (HashMap)
├── src/main/java/v6/    # FaultStatistics, LoadAnalysis, FaultType (Enum)
├── data/ai4i2020.csv
└── requirements.txt
```

---

## 技术实现细节

### 架构概览

项目采用模块化设计，核心目录包括：**data, src**。

### 技术栈与依赖

**核心框架/库**：Hadoop

**主要 import**：
```python
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
```

### 实现要点

- 基于 Hadoop 构建，技术栈成熟稳定
- 代码结构清晰，模块间低耦合，便于扩展和维护

---
## License

MIT — free to use, modify and distribute.
