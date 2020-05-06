package com.wolf.clazz

/**
  * @Description TODO
  * @Author wangqikang
  * @CreatedAt 2020-03-16 22:39
  **/
class Person {
  // val修饰只读属性，有getter但没有setter
  val id = "9527"

  // var修饰的变量既有getter又有setter
  var age: Int = 18

  // 私有变量只能在类的内部或伴生对象中使用
  private var name: String = "唐伯虎"

  // 对象私有属性只能在内部方法中访问
  private[this] var pet = "小强"

  def sayHi(): Unit = {
    println(pet)
  }
}

object Person {
  // 伴生对象中定义的变量相当于Java的static修饰变量
  val i = 1

  def main(args: Array[String]): Unit = {
    val p = new Person
    println(p.id)

    //    p.id = "123"

    println(p.age)

    p.age = 20
    println(p.age)

    println(p.name)
    p.name = "abc"
    println(p.name)

    //    p.pet
  }
}

//object Test {
//  def main(args: Array[String]): Unit = {
//    val p = new Person
//    p.name
//  }
//}
