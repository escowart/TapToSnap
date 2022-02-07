package com.lab49.taptosnap.ui.recycler

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Edwin S. Cowart on 06 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
class SpacingItemDecorationOptions(
    @DimenRes outerMargin: Int? = null,
    @DimenRes val outerMarginStart: Int? = outerMargin,
    @DimenRes val outerMarginEnd: Int? = outerMargin,
    @DimenRes val outerMarginTop: Int? = outerMargin,
    @DimenRes val outerMarginBottom: Int? = outerMargin,
    @DimenRes innerMargin: Int? = null,
    @DimenRes val innerMarginHorizontal: Int? = innerMargin,
    @DimenRes val innerMarginVertical: Int? = innerMargin,
)
class SpacingItemDecoration(
    @RecyclerView.Orientation private val orientation: Int,
    private val spanCount: Int,
    private val options: SpacingItemDecorationOptions): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val total = state.itemCount
        val index = parent.getChildAdapterPosition(view)
        val getDimensionPixelSize: (id: Int?) -> Int = {
            it?.let {
                parent.context.resources.getDimensionPixelSize(it)
            } ?: 0
        }
        outRect.left = getLeftMarginInPx(getDimensionPixelSize, index)
        outRect.right = getRightMarginInPx(getDimensionPixelSize, index, total)
        outRect.top = getTopMarginInPx(getDimensionPixelSize, index)
        outRect.bottom = getBottomMarginInPx(getDimensionPixelSize, index, total)
    }

    private fun getLeftMarginInPx(getDimensionPixelSize: (id: Int?) -> Int, index: Int): Int {
        val isStart = when (orientation) {
            RecyclerView.VERTICAL -> (index % spanCount) == 0
            RecyclerView.HORIZONTAL -> index < spanCount
            else -> throw IllegalStateException("Unsupported orientation $orientation")
        }
        return if (isStart)
            getDimensionPixelSize(options.outerMarginStart)
        else
            getDimensionPixelSize(options.innerMarginHorizontal) / 2
    }

    private fun getRightMarginInPx(getDimensionPixelSize: (id: Int?) -> Int, index: Int, total: Int): Int {
        val isEnd = when (orientation) {
            RecyclerView.VERTICAL -> (index % spanCount) == (spanCount - 1)
            RecyclerView.HORIZONTAL -> index >= (total - spanCount)
            else -> throw IllegalStateException("Unsupported orientation $orientation")
        }
        return if (isEnd)
            getDimensionPixelSize(options.outerMarginEnd)
        else
            getDimensionPixelSize(options.innerMarginHorizontal) / 2
    }

    private fun getTopMarginInPx(getDimensionPixelSize: (id: Int?) -> Int, index: Int): Int {
        val isTop = when (orientation) {
            RecyclerView.VERTICAL -> index < spanCount
            RecyclerView.HORIZONTAL -> (index % spanCount) == 0
            else -> throw IllegalStateException("Unsupported orientation $orientation")
        }
        return if (isTop)
            getDimensionPixelSize(options.outerMarginTop)
        else
            getDimensionPixelSize(options.innerMarginVertical) / 2
    }

    private fun getBottomMarginInPx(getDimenInPx: (id: Int?) -> Int, index: Int, total: Int): Int {
        val isBottom = when (orientation) {
            RecyclerView.VERTICAL -> index >= (total - spanCount)
            RecyclerView.HORIZONTAL -> (index % spanCount) == (spanCount - 1)
            else -> throw IllegalStateException("Unsupported orientation $orientation")
        }
        return if (isBottom)
            getDimenInPx(options.outerMarginBottom)
        else
            getDimenInPx(options.innerMarginVertical) / 2
    }
}
