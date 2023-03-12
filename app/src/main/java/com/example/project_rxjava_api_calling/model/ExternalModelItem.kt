package com.example.project_rxjava_api_calling.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExternalModelItem(
	val filePath: String?,
	val type: Int
) : Parcelable
