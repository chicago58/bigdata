package com.wolf.bigdata.spark.customsort

/**
  * @Description 隐式转换中定义排序规则
  * @Author wangqikang
  * @Date 2019-07-20 15:15
  */
object OrderContext {

  implicit val girlOrdering = new Ordering[Girl] {
    override def compare(x: Girl, y: Girl): Int = {
      if (x.faceValue > y.faceValue) 1
      else if (x.faceValue == y.faceValue) {
        if (x.age > y.age) -1 else 1
      } else -1
    }
  }
  
}
