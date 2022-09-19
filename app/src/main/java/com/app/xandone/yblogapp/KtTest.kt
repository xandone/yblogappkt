package com.app.xandone.yblogapp

import kotlinx.coroutines.*
import org.junit.Test

/**
 * @author: xiao
 * created on: 2022/9/6 10:11
 * description:
 */
object KtTest {

    private const val TAG: String = "Kttest"

    private const val s1: String = "s111"

    @JvmStatic
    fun main(arr: Array<String>) {

//        t1()
//        t2()
//        t3()
//        t4(Day.THURSDAY, false)
//        t5(Day.MONDAY)
//        t6()
//        t7("1", "2", "3")
//        *字操作符，将数组转化为可变参数的变量，作用等同于 t7("1", "2", "3")
//        t7(*arrayOf("4", "5", "6"))
//        t8()
//        t9()
//        t10()
        Test1.test1()
        Test1.test2()
    }

    /**
     * 数组
     */
    fun t1() {
        val arr1 = intArrayOf(1, 2, 3)
        val arr2 = arrayOf(4, 5, 6)
        println("arr1 [1]= ${arr1[1]}  arr2[1]=${arr2[1]}")


        for (a in 1..3) {
            print(a)
        }

        for (b in arr1) {
            print(b)
        }

        println()
        arr1.forEach { i -> print(i) }
        println()
    }

    /**
     * 集合
     */
    fun t2() {
        val list1 = listOf(1, 2, 3)
        val list2 = mutableListOf(1, 2, 3)

        println(list1.contains(4))
        list2.add(4)
        list2.add(4)
        list2.add(4)
        list2.add(4)
        println(" " + list2[0] + " " + list1.size + "  " + list2.contains(4))

        //String? 元素可以为null
        val list3: MutableList<String?> = mutableListOf("1", null)
//        val list31:MutableList<String> =mutableListOf("1",null)

        //下标
        for (i in list3.indices) {
            print(i)
        }
        //元素
        for (i in list3) {
            print(i)
        }
        list3.indices.forEach { i -> print(i) }
    }

    fun t3() {
        val map1 = mutableMapOf<String, String>()
        map1["key1"] = "a"
        map1["key2"] = "b"
        for (i in map1.values) {
            print(i)
        }
        for (i in map1.keys) {
            print(i)
        }
    }

    fun t4(day: Day, weekend: Boolean) {
        //多条件
        when {
            day == Day.MONDAY -> println("monday " + day.getToday())
            day == Day.TUESDAY -> println()
            weekend -> println("周末")
            //else 类似default
            else -> println("平时")
        }
        //单条件
        when (day) {
            Day.MONDAY -> println("monday-" + day.getToday())
            Day.TUESDAY -> println("tuesday-" + day.getToday())
            Day.WEDNESDAY -> println("wednesday-" + day.getToday())
            Day.THURSDAY -> println("thursday-" + day.getToday())
            //else 类似default
            else -> println("其他")
        }
    }

    fun t5(day: Day) = when {
        day == Day.MONDAY -> println("上班")
        else -> println("下班")
    }


    enum class Day(private val day: Int) {
        MONDAY(1),
        TUESDAY(2),
        WEDNESDAY(3),
        THURSDAY(4),
        Friday(5);

        fun getToday(): Int {
            return day
        }
    }

    /**
     * in操作符
     */
    fun t6() {
        for (i in 'A'..'z') {
//            print("${i}${i.toInt()}\t")
        }
        //间隔2步输出
        for (i in 1..10 step 2) {
//            print(i)
        }

        val ishave1 = "a" in "abc"
        val ishave2 = "a" in "a".."z"
        val ishave3 = "abcd".contains("a")
        print("${ishave1}${ishave2}${ishave3}")
    }

    /**
     * 多参数函数，类似java8的t7(String.. s)
     */
    fun t7(vararg arr: String) {
        arr.forEach { a -> print(a) }
//        it可作为默认的
        arr.forEach { print(it) }
    }

    /**
     * 字符串操作
     */
    fun t8() {
        val str = "abcdefg"
        val str2: String?
        str2 = str.filter { c -> c in 'a'..'d' }
        print(str2)
    }

    fun t9() {
        val bird1 = Bird();
        bird1.printAge()
        val bird2 = Bird(11.1);
        bird2.printAge()
        bird2.printWeight()
    }

    /**
     * 构造函数可以给默认参数
     */
    class Bird(private val age: Int = 5) {
        private var mWeight: Double? = null
        fun printAge() {
            println(age)
        }

        //其他构造函数需要先调用"基本构造->Bird(private val age: Int = 5)"，除非去除掉"基本构造"，
        constructor(weight: Double) : this() {
            mWeight = weight
        }

        fun printWeight() {
            println(mWeight)
        }

        //        "基本构造"在类体之外，无法写代码，所以委托给init执行
        init {
            println("初始化了")
        }
    }

    fun t10() {
        GlobalScope.launch(context = Dispatchers.IO) {
            t10_1()
            t10_2()
        }
    }

    fun t10_1() {
//        delay(1000)
        println("t10_1" + Thread.currentThread().name)
    }

    suspend fun t10_2() {
        delay(2000)
        println("t10_2" + Thread.currentThread().name)
    }

    class Test1 {
        val a: Int = 1

        /**
         * object声明（一个类）是延迟加载的，只有当第一次被访问时才会初始化，所以被用来实现单例
         * companion object是当包含它的类被加载时就初始化了的
         */
        companion object {
            @JvmStatic
            fun test1() {
                println("1230")
            }
            fun test2() {
                println("1232")
            }
        }

    }

}
