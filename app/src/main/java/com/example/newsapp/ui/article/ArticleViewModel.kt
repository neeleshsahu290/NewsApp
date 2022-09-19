package com.example.newsapp.ui.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Article
import com.example.newsapp.models.News
import com.example.newsapp.network.ApiService
import com.example.newsapp.network.RetrofitHelper
import com.example.newsapp.utility.Constants
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.create
import retrofit2.http.Query

class ArticleViewModel : ViewModel() {
    private val _hasData: MutableLiveData<Boolean> = MutableLiveData()
    val hasData: LiveData<Boolean> get() = _hasData
    private val _data :MutableLiveData<List<Article>> = MutableLiveData()
    val data: LiveData<List<Article>> get() =_data
    private val service= RetrofitHelper().retrofit.create(ApiService::class.java)

    fun getNews(itemQuery:String,from:String?, shortBy:String?){

        viewModelScope.launch {
            try {
                val response: Response<News> = service.getEverything(itemQuery=itemQuery, key = Constants.API_KEY,  shortBy = shortBy)

                // val response: Response<News> = service.getHeadlines()

                Log.d("response_code",response.code().toString())
                if(response.isSuccessful ){
                    _hasData.postValue(true)
                    val result= response.body()
                    result?.let {
                        it.articles?.let {art->
                            _data.postValue(art)
                        }
                    }
                    Log.d("headline_data",result.toString())

                }else{

                    _hasData.postValue(false)
                    Log.d("headline_err",response.message().toString())

                    Log.e("headline_error",response.body().toString())
                }
            }catch (e:Exception){
                _hasData.postValue(false)
                Log.e("headline_error",e.toString())

            }
        }

    }
}