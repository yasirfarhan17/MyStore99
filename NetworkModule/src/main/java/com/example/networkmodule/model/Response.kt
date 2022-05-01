package com.example.networkmodule.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Response(
	val products: List<ProductsItem?>? = null
) : Parcelable

@Parcelize
data class ProductsItem(
	val price: String? = null,
	val image_url: String? = null,
	val name: String,
	val rating: Int? = null
) : Parcelable{
//	fun  toCartTable():CartTable{
//		val obj=CartTable(
//			price=this.price,
//			image_url=this.image_url,
//			name=this.name,
//			quantity = 1
//		)
//		return obj
//	}
}
