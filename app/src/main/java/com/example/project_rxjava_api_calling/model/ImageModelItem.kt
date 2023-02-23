package com.example.project_rxjava_api_calling.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class ImageModelItem(
    val height: Int,
    val width: Int,
    val author: String?,
    val download_url: String?,
    val id: String?,
    val url: String?,
): Parcelable