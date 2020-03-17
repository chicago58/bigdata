### 需求：
统计数据集中的每个单词在每篇文档中出现的次数。

### 实现：
Step One：统计单词在特定文档中出现的总次数。
```
MapTask输出: 
hello->a.txt 1
hello->a.txt 1
...

ReduceTask输出: 
hello->a.txt 3
tom->a.txt 1
hello->b.txt 2
hello->c.txt 1
...
```

Step Two：汇总单词在所有文档中出现的总次数。
```
MapTask输出: 
hello a.txt->3
hello b.txt->2
hello c.txt->1
tom a.txt->1 
...

ReduceTask输出:
hello a.txt->3 b.txt->2 c.txt->1
tom a.txt->1 b.txt->1 c.txt->1
...
```

