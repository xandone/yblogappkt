package com.app.xandone.yblogapp.model.bean

/**
 * author: Admin
 * created on: 2020/11/14 10:53
 * description:
 */
data class ArtInfoBean
/**
 * typeBeans : [{"name":"编程","count":20},{"name":"杂文","count":21}]
 * artTypeBeans : [{"count":20,"typeName":"全部","type":-1},{"count":2,"typeName":"编程","type":0},{"count":7,"typeName":"Android","type":1},{"count":2,"typeName":"Java","type":2},{"count":3,"typeName":"前端","type":3},{"count":2,"typeName":"设计模式","type":4},{"count":3,"typeName":"算法","type":5},{"count":0,"typeName":"Python","type":6},{"count":1,"typeName":"Canvas","type":7},{"count":0,"typeName":"Game","type":8}]
 * yearArtData : [{"year":"2012","codeCount":0,"essayCount":1},{"year":"2013","codeCount":0,"essayCount":9},{"year":"2014","codeCount":0,"essayCount":2},{"year":"2015","codeCount":0,"essayCount":3},{"year":"2016","codeCount":3,"essayCount":1},{"year":"2017","codeCount":4,"essayCount":0},{"year":"2018","codeCount":4,"essayCount":0},{"year":"2019","codeCount":6,"essayCount":0},{"year":"2020","codeCount":3,"essayCount":5}]
 * commentCounts : 2
 */
    (val commentCounts: Int = 0,
     val typeBeans: List<TypeBeansBean>? = null,
     val artTypeBeans: List<ArtTypeBeansBean>? = null,
     val yearArtData: List<YearArtDataBean>? = null)

data class TypeBeansBean(
    /**
     * name : 编程
     * count : 20
     */
    val name: String? = null,
    val count: Int = 0

)

data class ArtTypeBeansBean(
    /**
     * count : 20
     * typeName : 全部
     * type : -1
     */
    val count: Int = 0,
    val typeName: String? = null,
    val type: Int = 0

)

data class YearArtDataBean(
    /**
     * year : 2012
     * codeCount : 0
     * essayCount : 1
     */
    val year: String? = null,
    val codeCount: Int = 0,
    val essayCount: Int = 0

)
