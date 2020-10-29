package com.app.xandone.yblogapp.model.event

/**
 * author: Admin
 * created on: 2020/9/29 10:02
 * description:
 */
class SwitchEvent(var tag: Int) {

    companion object {
        const val MANAGER_LOGIN_RAG = 1
        const val MANAGER_DATA_RAG = 2
    }

}