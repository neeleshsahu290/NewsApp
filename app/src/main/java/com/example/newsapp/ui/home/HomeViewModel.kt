package com.example.newsapp.ui.home

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

class HomeViewModel : ViewModel() {

    private val service = RetrofitHelper().retrofit.create(ApiService::class.java)

    private val _hasData: MutableLiveData<Boolean> = MutableLiveData()
    val hasData: LiveData<Boolean> get() = _hasData
    private val _data: MutableLiveData<List<Article>> = MutableLiveData()
    val data: LiveData<List<Article>> get() = _data


    fun getHeadlines(countryCode:String) {

        viewModelScope.launch {

            try {
                val response: Response<News> = service.getTopHeadlines(key = Constants.API_KEY,countryCode)

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