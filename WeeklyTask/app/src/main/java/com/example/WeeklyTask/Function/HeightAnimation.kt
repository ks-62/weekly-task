package com.example.WeeklyTask.Function

import android.view.animation.Animation
import android.opengl.ETC1.getWidth
import android.view.View
import android.view.animation.Transformation


class HeightAnimation: Animation() {
    var targetHeight: Int = 0
    var startHeight: Int = 0

    lateinit var view: View

    fun HeightAnimation(view: View, startHeight: Int, targetHeight: Int) {
        this.view = view
        this.targetHeight = targetHeight
        this.startHeight = startHeight
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val newHeight = (startHeight + (targetHeight - startHeight) * interpolatedTime).toInt()
        view.getLayoutParams().height = newHeight
        view.requestLayout()
    }

    override fun initialize(
        width: Int, height: Int, parentWidth: Int,
        parentHeight: Int
    ) {
        super.initialize(width, height, (view.getParent() as View).getWidth(), parentHeight)
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}