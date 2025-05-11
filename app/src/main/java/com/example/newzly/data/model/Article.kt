package com.example.newzly.data.model

data class Article(
    val title: String = "",
    val url: String = "",
    val urlToImage: String? = "",
    val author: String? = "",
    val content: String? = "",
    val publishedAt: String = "",
    var isExpanded: Boolean = false,
)