package com.kotlin.test

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter

/**
 * Created by MZ on 2017/12/14.
 * 反射
 */
//扩展属性
val String.lastChar: Char
    get() = this[length - 1]

class Reflect {
    val x = 1
    var y = 1

    fun test1() {
        val c = Reflect::class  //类引用
        println(c)

        val j = c.java //java类引用
        println(j)

        //类引用创建实例
        val reflect: Reflect = Reflect::class.createInstance()
        println(reflect)

        //函数引用
        fun isOdd(x: Int) = x % 2 != 0

        val numbers = listOf(1, 2, 3)
        println(numbers.filter(::isOdd))

        //函数重载
        fun isOdd(s: String) = s == "brillig" || s == "slithy" || s == "tove"
        println(numbers.filter(::isOdd))

        //函数重载和赋值
        val predicate: (String) -> Boolean = ::isOdd

        //函数组合
        fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C {
            return { x -> f(g(x)) }
        }

        fun length(s: String) = s.length
        val oddLength = compose(::isOdd, ::length)
        val strings = listOf("a", "ab", "abc")
        println(strings.filter(oddLength))

        //属性引用
        println(::x)
        println(::x.name + "=" + ::x.get())

        ::y.set(2)
        println(::y)
        println(::y.name + "=" + y)

        //属性引用可以用在不需要参数的函数处
        println(strings.map(String::length))

        //访问属于类的成员的属性
        val prop = A::p //属性引用
        println(prop.get(A(2)))

        //使用扩展属性
        println(String::lastChar.get("abc"))

        //java反射
        println(A::p.javaGetter)
        println(A::p.javaField)

        //获得对应于 Java 类的 Kotlin 类
        println(getKClass(JavaTest::class))

        //构造函数引用
        fun function(factory: () -> Foo) {
            val x: Foo = factory()
        }
        function(::Foo)

        //引用特定对象的实例方法
        val numberRegex = "\\d+".toRegex()
        println(numberRegex.matches("29"))
        val isNumber = numberRegex::matches
        println(isNumber("29x"))

        //方法引用可以用在不需要参数的函数处
        println(strings.filter(numberRegex::matches))

        //函数引用绑定
        val isNumber2: (CharSequence) -> Boolean = numberRegex::matches
        val matches: (Regex, CharSequence) -> Boolean = Regex::matches

        //属性引用绑定
        val prop2 = "abc"::length
        println(prop2.get())
    }

    fun getKClass(o: Any): KClass<Any> = o.javaClass.kotlin

    class A(val p: Int)

    class Foo
}