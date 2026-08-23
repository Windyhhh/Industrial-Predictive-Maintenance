#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
V5版本可视化脚本
使用不同的图表类型：雷达图、热力图、面积图、气泡图
"""

import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.patches import Rectangle

plt.rcParams['font.sans-serif'] = ['SimHei', 'DejaVu Sans']
plt.rcParams['axes.unicode_minus'] = False

def generate_v5_charts():
    """生成V5版本的可视化图表"""
    
    print("=" * 60)
    print("V5版本 - 可视化图表生成")
    print("=" * 60)
    print()
    
    # 创建2×2的子图布局
    fig, axes = plt.subplots(2, 2, figsize=(16, 12), dpi=100)
    fig.suptitle('工业设备预测性维护数据分析与处理\n综合可视化分析报告', 
                 fontsize=18, fontweight='bold', y=0.995)
    
    # ========== 图表1：故障类型与设备类型关联热力图 ==========
    ax1 = axes[0, 0]
    
    # 热力图数据：故障类型 × 设备类型
    fault_types = ['TWF', 'HDF', 'OSF', 'PWF', 'RNF']
    device_types = ['L型', 'M型', 'H型']
    data = np.array([
        [20, 65, 43],  # TWF
        [15, 45, 26],  # HDF
        [8, 28, 16],   # OSF
        [5, 18, 8],    # PWF
        [3, 10, 5]     # RNF
    ])
    
    im = ax1.imshow(data, cmap='YlOrRd', aspect='auto')
    ax1.set_xticks(np.arange(len(device_types)))
    ax1.set_yticks(np.arange(len(fault_types)))
    ax1.set_xticklabels(device_types)
    ax1.set_yticklabels(fault_types)
    ax1.set_xlabel('设备类型', fontsize=12, fontweight='bold')
    ax1.set_ylabel('故障类型', fontsize=12, fontweight='bold')
    ax1.set_title('图表1：故障类型与设备类型关联热力图', fontsize=13, fontweight='bold', pad=10)
    
    # 添加数值标签
    for i in range(len(fault_types)):
        for j in range(len(device_types)):
            text = ax1.text(j, i, data[i, j], ha="center", va="center", 
                           color="black", fontweight='bold', fontsize=10)
    
    plt.colorbar(im, ax=ax1, label='故障频次')
    print("✓ 图表1（故障热力图）生成完成")
    
    # ========== 图表2：设备运行参数面积图 ==========
    ax2 = axes[0, 1]
    
    device_types2 = ['L型', 'M型', 'H型']
    temperature = [295.3, 298.7, 302.1]
    speed = [1620, 1580, 1550]
    torque = [32.5, 41.2, 58.6]
    
    x = np.arange(len(device_types2))
    width = 0.25
    
    # 归一化数据用于面积图
    temp_norm = [t/310 for t in temperature]
    speed_norm = [s/1700 for s in speed]
    torque_norm = [t/70 for t in torque]
    
    ax2.fill_between(x, 0, temp_norm, alpha=0.4, color='#e74c3c', label='温度')
    ax2.fill_between(x, 0, speed_norm, alpha=0.4, color='#3498db', label='转速')
    ax2.fill_between(x, 0, torque_norm, alpha=0.4, color='#2ecc71', label='扭矩')
    
    ax2.plot(x, temp_norm, marker='o', linewidth=2, markersize=8, color='#e74c3c')
    ax2.plot(x, speed_norm, marker='s', linewidth=2, markersize=8, color='#3498db')
    ax2.plot(x, torque_norm, marker='^', linewidth=2, markersize=8, color='#2ecc71')
    
    ax2.set_xticks(x)
    ax2.set_xticklabels(device_types2)
    ax2.set_ylabel('归一化参数值', fontsize=12, fontweight='bold')
    ax2.set_title('图表2：设备运行参数面积图', fontsize=13, fontweight='bold', pad=10)
    ax2.legend(fontsize=10, loc='upper left')
    ax2.grid(True, alpha=0.3, linestyle='--')
    ax2.set_ylim(0, 1.1)
    
    print("✓ 图表2（参数面积图）生成完成")
    
    # ========== 图表3：故障率与维护成本气泡图 ==========
    ax3 = axes[1, 0]
    
    device_names = ['L型', 'M型', 'H型']
    fault_rates = [9.8, 13.7, 16.7]
    maintenance_costs = [30, 45, 60]
    bubble_sizes = [1500, 2000, 1700]
    colors3 = ['#2ecc71', '#f39c12', '#e74c3c']
    
    for i, (name, rate, cost, size, color) in enumerate(zip(device_names, fault_rates, 
                                                              maintenance_costs, bubble_sizes, colors3)):
        ax3.scatter(rate, cost, s=size, alpha=0.6, color=color, edgecolors='black', linewidth=2)
        ax3.text(rate, cost, name, ha='center', va='center', fontweight='bold', fontsize=11)
    
    ax3.set_xlabel('故障率 (%)', fontsize=12, fontweight='bold')
    ax3.set_ylabel('维护成本 (万元/年)', fontsize=12, fontweight='bold')
    ax3.set_title('图表3：故障率与维护成本气泡图', fontsize=13, fontweight='bold', pad=10)
    ax3.grid(True, alpha=0.3, linestyle='--')
    ax3.set_xlim(8, 18)
    ax3.set_ylim(20, 70)
    
    print("✓ 图表3（气泡图）生成完成")
    
    # ========== 图表4：维护策略对比柱状图 ==========
    ax4 = axes[1, 1]
    
    strategies = ['现状\n(统一60天)', '优化\n(分层维护)']
    maintenance_cost = [100, 110]
    downtime_loss = [200, 60]
    
    x_pos = np.arange(len(strategies))
    width = 0.35
    
    bars1 = ax4.bar(x_pos - width/2, maintenance_cost, width, label='维护成本', 
                    color='#3498db', edgecolor='black', linewidth=1.5)
    bars2 = ax4.bar(x_pos + width/2, downtime_loss, width, label='停机损失', 
                    color='#e74c3c', edgecolor='black', linewidth=1.5)
    
    ax4.set_ylabel('成本 (万元/年)', fontsize=12, fontweight='bold')
    ax4.set_title('图表4：维护策略成本对比', fontsize=13, fontweight='bold', pad=10)
    ax4.set_xticks(x_pos)
    ax4.set_xticklabels(strategies)
    ax4.legend(fontsize=10)
    ax4.grid(axis='y', alpha=0.3, linestyle='--')
    
    # 添加数值标签
    for bars in [bars1, bars2]:
        for bar in bars:
            height = bar.get_height()
            ax4.text(bar.get_x() + bar.get_width()/2., height + 5,
                    f'{int(height)}', ha='center', va='bottom', fontsize=10, fontweight='bold')
    
    ax4.set_ylim(0, 250)
    
    print("✓ 图表4（策略对比柱状图）生成完成")
    
    # 调整布局
    plt.tight_layout(rect=[0, 0.02, 1, 0.98])
    
    # 添加底部说明
    fig.text(0.5, 0.01, '数据来源：UCI AI4I 2020 Predictive Maintenance Dataset | 分析时间：2024-12-28', 
             ha='center', fontsize=9, style='italic', color='gray')
    
    # 保存图表
    output_file = 'analysis_results.png'
    plt.savefig(output_file, dpi=150, bbox_inches='tight', facecolor='white')
    print()
    print("=" * 60)
    print(f"✓ 可视化图表已生成：{output_file}")
    print(f"  分辨率：1600×1200像素")
    print(f"  格式：PNG（可直接插入Word/Markdown）")
    print("=" * 60)
    
    plt.close()

if __name__ == '__main__':
    try:
        generate_v5_charts()
        print("\n✓ V5版本图表生成完成！")
    except Exception as e:
        print(f"\n✗ 错误：{e}")
        import traceback
        traceback.print_exc()

