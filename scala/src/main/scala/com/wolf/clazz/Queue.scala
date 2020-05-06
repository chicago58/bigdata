package com.wolf.clazz

/**
  * @Description 私有构造器
  * @Author wangqikang
  * @CreatedAt 2020-03-16 23:12
  **/
//若构造器参数没有val或var修饰，且至少被一个方法使用，则该参数保存为属性字段
//类名后private修饰即为私有
class Queue private(val name: String, prop: Array[String], private var age: Int = 18) {

  println(prop.size)

  def description = name + " is " + age + " years old with " + prop.toBuffer

}

object Queue {

  def main(args: Array[String]): Unit = {
    //私有构造器只在器伴生对象中使用
    val q = new Queue("hatano", Array("蜡烛", "皮鞭"), 20)
    println(q.description)
  }

}
