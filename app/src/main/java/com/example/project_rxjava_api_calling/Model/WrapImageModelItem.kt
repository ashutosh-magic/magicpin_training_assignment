package com.example.project_rxjava_api_calling.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WrapImageModelItem (
    var viewType: Int,
    var apiData: ImageModelItem

): Parcelable