### 需求：
挖掘两两之间有共同好友的人，以及他们之间的共同好友。

#### 实现：
Step One
```
MapTask: 
f -> p

ReduceTask: 
f1 -> p1, p2, p3 ...
f2 -> p1, p2, p4 ...
```

- Step Two
```
MapTask: 
p1-p2 f1
p1-p3 f1
p2-p3 f1
p1-p2 f2
...

ReduceTask:
p1-p2 f1 f2
p1-p3 f1
p2-p3 f1
...
```
