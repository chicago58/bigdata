package com.wolf.bigdata.spark.customsort

/**
  * @Description 隐式转换中定义排序规则
  * @Author wangqikang
  * @Date 2019-07-20 15:15
  */
object OrderContext {

  /**
    * 隐式转换：定义隐式规则放入Object中，使用时导入即可。（原数据样式没有改变，只是传入了比较规则）
    */
  implicit val girlOrdering = new Ordering[Girl] {
    override def compare(x: Girl, y: Girl): Int = {
      if (x.faceValue > y.faceValue) 1
      else if (x.faceValue == y.faceValue) {
        if (x.age > y.age) -1 else 1
      } else -1
    }
  }

}
