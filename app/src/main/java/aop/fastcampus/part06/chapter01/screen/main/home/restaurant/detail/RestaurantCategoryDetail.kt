package aop.fastcampus.part06.chapter01.screen.main.home.restaurant.detail

import androidx.annotation.StringRes
import aop.fastcampus.part06.chapter01.R

enum class RestaurantCategoryDetail(
    @StringRes val categoryNameId: Int
) {

    MENU(R.string.menu), REVIEW(R.string.review)
}