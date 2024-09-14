package aop.fastcampus.part06.chapter01.extensions

import android.content.res.Resources

fun Float.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}