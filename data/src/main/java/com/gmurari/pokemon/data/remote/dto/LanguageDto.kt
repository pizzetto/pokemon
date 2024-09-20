package com.gmurari.pokemon.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LanguageDto(
    val name: String,
    val url: String
)
