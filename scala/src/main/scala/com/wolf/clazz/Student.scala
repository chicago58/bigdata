package com.wolf.clazz

/**
  * @Description TODO
  * @Author wangqikang
  * @CreatedAt 2020-03-16 22:58
  **/
class Student(val name: String, var age: Int, faceValue: Double = 99.99, private var height: Int = 18) { // 柱构造器和类名交织在一起

  private[this] var gender: String = null

  def show(): Unit = {
    //    faceValue = 1000
    println(faceValue)
  }

  /**
    * 辅助构造器
    *
    * @param name
    * @param age
    * @param gender
    */
  def this(name: String, age: Int, gender: String) {
    // 辅助构造器第一行一定要先调用主构造器
    this(name, age)
    this.gender = gender
  }

}

object Student {

  def main(args: Array[String]): Unit = {
    val stu = new Student("zhangsan", 30, 100, 180)
    println(stu.age)
    println(stu.name)

    stu.age = 19
    println(stu.age)

    println(stu.height)

    val per = new Person
    println(per)
  }
}

//object Main {
//
//  def main(args: Array[String]): Unit = {
//    val s = new Student("zhangsan", 30, 100)
//    println(s.height) // 私有变量只能在类的内部或伴生对象中使用
//  }
//
//}
