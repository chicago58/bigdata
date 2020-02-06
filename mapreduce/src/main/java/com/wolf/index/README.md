### 需求：
统计数据集中的每个单词在每篇文档中出现的次数，并建立索引。

### 实现：
Step One：
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

Step Two：
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

