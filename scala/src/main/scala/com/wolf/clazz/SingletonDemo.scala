package com.wolf.clazz

/**
  * @Description TODO
  * @Author wangqikang
  * @CreatedAt 2020-03-17 07:29
  **/
class SingletonDemo {
  println("abc")
  SingletonDemo.sayHi()
  SingletonDemo

  test()

  def test(): Unit = {
    println("test")
  }
}

object SingletonDemo {
  private val t = 123

  def sayHi(): Unit = {
    println("hi ~")
  }

  def main(args: Array[String]): Unit = {
    sayHi()

    println("123")
    println(t)

    val s1 = SingletonDemo.sayHi()
    println(s1)

    val s2 = SingletonDemo
    println("s2:" + s2)

    val s3 = new SingletonDemo
    println("s3:" + s3)

    val s4 = SingletonDemo
    println("s4:" + s4)
  }
}
