package com.example.breakingnews.ui.news
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import com.example.breakingnews.data.model.Article
import com.example.breakingnews.data.model.News
import com.example.breakingnews.data.repository.ArticleRepository
import com.example.breakingnews.util.Constants.Companion.DEFAULT_TOPIC
import com.example.breakingnews.util.Constants.Companion.STARTING_PAGE_INDEX
import com.example.breakingnews.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class NewsViewModel  @Inject constructor(private val articleRepository: ArticleRepository) : ViewModel() {

    val articles: MutableLiveData<Resource<News>> = MutableLiveData()
    private var newsResponse: News? = null
    var page = STARTING_PAGE_INDEX

    init {
        getAllArticles(DEFAULT_TOPIC)
    }
    private fun getAllArticles(country:String) = viewModelScope.launch {
        articles.postValue(Resource.Loading())
        val response = articleRepository.getAllArticles(country, page)
        articles.postValue(handleArticlesResponse(response))
    }

    private fun handleArticlesResponse(response: Response<News>): Resource<News> {
        if (response.isSuccessful) {
            response.body()?.let {
                page++
                if (newsResponse == null) {
                    newsResponse = it
                } else {
                    val oldArticles = newsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getFavoriteArticles() = articleRepository.getFavoriteArticles()
    fun addArticleToFavorites(article: Article) = viewModelScope.launch {
        articleRepository.insert(article)
    }
     fun getCount() = articleRepository.getCount()
    fun removeArticleFromFavorites(article: Article) = viewModelScope.launch {
        articleRepository.deleteArticle(article)
    }
}