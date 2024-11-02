package aop.fastcampus.part06.chapter01.model.restaurant.review

import android.net.Uri
import aop.fastcampus.part06.chapter01.model.CellType
import aop.fastcampus.part06.chapter01.model.Model

data class RestaurantReviewModel(
    override val id: Long,
    override val type: CellType = CellType.REVIEW_CELL,
    val title: String,
    val description:String,
    val grade: Float,
    val thumbnailImageUri: Uri? = null
): Model(id,type)
