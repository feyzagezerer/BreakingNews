package com.example.breakingnews.data.repository

import com.example.breakingnews.data.local.ArticleDatabase
import com.example.breakingnews.data.model.Article
import com.example.breakingnews.data.remote.NewsApi
import com.example.breakingnews.util.Constants.Companion.API_KEY
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val database: ArticleDatabase,
    private val newsApi: NewsApi,
) {

    suspend fun getAllArticles(country: String,  pageNumber: Int) =
        newsApi.getNews(country,  pageNumber, API_KEY)

    fun getFavoriteArticles() = database.articleDao().getArticles()

    fun getCount(): Int = database.articleDao().getCount()

    suspend fun insert(article: Article) = database.articleDao().insert(article)

    suspend fun deleteArticle(article: Article) = database.articleDao().deleteArticle(article)


}