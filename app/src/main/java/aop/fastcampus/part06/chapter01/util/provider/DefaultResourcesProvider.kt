package aop.fastcampus.part06.chapter01.util.provider

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class DefaultResourcesProvider(
    private val context: Context
) :ResourcesProvider{
    override fun getString(@StringRes resId: Int): String = context.getString(resId)

    override fun getString(@StringRes resId: Int, vararg formArgs: Any): String = context.getString(resId, *formArgs)

    override fun getColor(@StringRes resId: Int): Int = ContextCompat.getColor(context,resId)

    override fun getColorStateList(@StringRes resId: Int): ColorStateList = context.getColorStateList(resId)

}