package com.app.xandone.widgetlib.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * author: Admin
 * created on: 2018/10/9 14:59
 * description:
 */
class SpacesItemDecoration : ItemDecoration {
    private var itemSpaceH: Int
    private var itemSpaceHLeft = 0
    private var itemSpaceV: Int

    /**
     * @param itemSpaceH 横向
     * @param itemSpaceV 纵向
     */
    constructor(context: Context, itemSpaceH: Int, itemSpaceV: Int) {
        this.itemSpaceH = SizeUtils.dp2px(context, itemSpaceH.toFloat())
        this.itemSpaceV = SizeUtils.dp2px(context, itemSpaceV.toFloat())
    }

    constructor(
        context: Context,
        itemSpaceHL: Int,
        itemSpaceHR: Int,
        itemSpaceV: Int
    ) {
        itemSpaceH = SizeUtils.dp2px(context, itemSpaceHR.toFloat())
        itemSpaceHLeft = SizeUtils.dp2px(context, itemSpaceHL.toFloat())
        this.itemSpaceV = SizeUtils.dp2px(context, itemSpaceV.toFloat())
    }

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = itemSpaceV
        outRect.right = itemSpaceH
        if (itemSpaceHLeft > 0) {
            outRect.left = itemSpaceHLeft
        }
    }
}