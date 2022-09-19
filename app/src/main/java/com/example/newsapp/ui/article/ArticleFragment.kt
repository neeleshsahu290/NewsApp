package com.example.newsapp.ui.article

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.newsapp.activity.SearchActivity
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.ui.home.HeadlineAdapter
import com.example.newsapp.ui.home.HomeViewModel
import com.example.newsapp.utility.shareTextUrl
import com.example.newsapp.utility.toastMsg
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ArticleFragment : Fragment() ,CoroutineScope by MainScope(),HeadlineAdapter.onHeadlineAdapterClicked{


    private var isLoaded=false
   lateinit  var _binding: FragmentArticleBinding
    lateinit var viewModel: ArticleViewModel
    lateinit var headlineAdapter: HeadlineAdapter

    var itemValue = "tesla"

    var resultLauncher: ActivityResultLauncher<Intent>?=null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ArticleViewModel::class.java]
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        init()
     //   setCollapsableBar()
        initViewModel(itemValue)
        netConnection()
        setListeners()
        return root
    }


    private fun setListeners(){
        binding.searchLyt.setOnClickListener{
            val intent = Intent(activity, SearchActivity::class.java)
            this@ArticleFragment.resultLauncher?.launch(intent)

        }
        binding.errLyt.retryBtn.setOnClickListener {
            binding.errLyt.root.isVisible=false
            initViewModel(itemValue)
        }
    }
    private fun isLoading(loading:Boolean){
        binding.progressLyt.root.isVisible=loading
    }

    private fun init(){
         resultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val value=data?.getStringExtra("result")
                if (value != null) {
                    itemValue=value
                }
                isLoading(true)
                isLoaded=false
                Log.d("data_get",value.toString())
                initViewModel(itemValue)




            }
        }

        headlineAdapter=HeadlineAdapter(requireContext(),this@ArticleFragment)
        binding.headlineRecycler.apply {

            this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter=  headlineAdapter

        }
        isLoading(true)
    }

    private fun initViewModel(item:String){
        viewModel.getNews(itemQuery = item, from = "2022-08-18",shortBy = "publishedAt")
        viewModel.hasData.observe(viewLifecycleOwner){hasData->
            if (hasData){
                viewModel.data.observe(viewLifecycleOwner){
                    if (it!=null){
                        isLoaded=true
                        headlineAdapter.addList(it)
                        isLoading(false)
                    }
                }
            }else{
            //    requireContext().toastMsg("some error occurred")
                binding.errLyt.root.isVisible=true
               // isLoading(false)
            }

        }
    }

    private fun netConnection() {
        launch(Dispatchers.IO) {


            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    it.registerDefaultNetworkCallback(object :
                        ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {

                            Handler(Looper.getMainLooper()).post {
                                //code that runs in main
                                binding.errorLayout.root.isVisible = false
                                initViewModel("tesla")

                            }
                            //take action when network connection is gained
                        }

                        override fun onLost(network: Network) {
                            Handler(Looper.getMainLooper()).post {
                                //code that runs in main


                                if (!isLoaded) {

                                    binding.errorLayout.root.isVisible = true
                                }
                            }
                        }
                    })
                }
            }
        }
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }

    override fun onShareButtonClicked(url: String) {
        requireContext().shareTextUrl(url)

    }
}