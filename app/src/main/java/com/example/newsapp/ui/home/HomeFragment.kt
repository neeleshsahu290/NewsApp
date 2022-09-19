package com.example.newsapp.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.utility.shareTextUrl
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class HomeFragment : Fragment(),HeadlineAdapter.onHeadlineAdapterClicked , CoroutineScope by MainScope(){

    lateinit var _binding: FragmentHomeBinding
    private var isLoaded=false
    lateinit var headlineAdapter: HeadlineAdapter


    // This property is only valid between onCreateView and
    // onDestroyView.
    lateinit var viewModel: HomeViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        init()
        setCollapsableBar()
        initViewModel()
        netConnection()
        setListners()
        return root
    }

    private fun setListners(){
        binding.errLyt.retryBtn.setOnClickListener {
            binding.errLyt.root.isVisible=false
            initViewModel()
        }


    }

    private fun isLoading(loading:Boolean){
        binding.progressLyt.root.isVisible=loading
    }

    private fun init(){
        headlineAdapter=HeadlineAdapter(requireContext(),this@HomeFragment)
        binding.headlineRecycler.apply {

            this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter=  headlineAdapter

        }
        isLoading(true)
    }

    private fun initViewModel(){
        viewModel.getHeadlines("in")
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
             //   requireContext().toastMsg("some error occurred")
               // isLoading(false)

                binding.errLyt.root.isVisible=true
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

                                if (!isLoaded) {
                                    initViewModel()
                                }


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

    private fun setCollapsableBar(){

        val collapsingToolbar = binding.collapsingToolbar
        val primaryColor = MaterialColors.getColor(binding.root, androidx.appcompat.R.attr.colorPrimary)
        binding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor( requireContext(),R.color.white))

        binding.appbarlayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (collapsingToolbar.height + verticalOffset < collapsingToolbar.scrimVisibleHeightTrigger) {
           //     binding.toolbar.setBackgroundColor(resources.getColor(R.color.primaryBackground));
                binding.toolbar.setBackgroundColor(primaryColor);

                binding.collapsingToolbar.title="Headlines"
              //  binding.toolbar.title="Headlines"
               // binding.txtCollapseBelow.text="Add New Contact"
            } else {
                binding.collapsingToolbar.title=""
                binding.toolbar.setBackgroundColor(ContextCompat.getColor( requireContext(),
                    R.color.transparent));

                // binding.txtCollapseBelow.text="Add New Contact"
              //  binding.toolbar.title=""

            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }

    override fun onShareButtonClicked(url: String) {
        requireContext().shareTextUrl(url)
    }
}