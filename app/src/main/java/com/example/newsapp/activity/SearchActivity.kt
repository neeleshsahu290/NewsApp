package com.example.newsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.newsapp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity(),SearchAdapter.onSearchAdapterClicked {
    private var searchAdapter:SearchAdapter?=null
    lateinit var binding:ActivitySearchBinding
    private var searchList:ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setRecyclerView()
        setListners()

    }
    private fun setListners(){
        binding.seaechBtn.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(msg)
                return false
            }
        })
    }

    private fun init() {
      searchList.add("tesla")
        searchList.add("bitcoin")
        searchList.add("india")
        searchList.add("tiger")
        searchList.add("goa")
        searchList.add("mumbai")
        searchList.add("dehli")
    //    searchList.add("narendra modi")
    }

    private fun setRecyclerView(){
        searchAdapter= SearchAdapter(this,searchList,this)
        binding.headlineRecycler.adapter = searchAdapter
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<String> = ArrayList()

        // running a for loop to compare elements.
        for (item in searchList) {
            // checking if the entered string matched with any item of our recycler view.
            item.let {
                if (item.toLowerCase().contains(text.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    filteredlist.add(item)
                }
            }

        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
         //   Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.

            searchAdapter?.addFilterList(filteredlist)
        }
    }

    override fun onItemClicked(item: String) {

        val returnIntent = intent
        returnIntent.putExtra("result", item)
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}