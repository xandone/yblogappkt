package com.xandone.manager2.model.base

/**
 * @author: xiao
 * created on: 2023/2/16 16:29
 * description:
 */
class Abean(val a: Int) {

    val age: Int

    init {
        age = a
    }

    constructor(a: Int, b: Int) : this(a) {

    }
}