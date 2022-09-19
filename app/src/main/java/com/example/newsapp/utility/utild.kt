package com.example.newsapp.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide


fun Context.toastMsg(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}

fun ImageView.imageFromUrl(context: Context,url:String?){
    Glide
        .with(context)
        .load(url)
        .centerCrop()
       // .placeholder(R.drawable.ic_dark_share)
        .into(this);
}


 fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val inputManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

 fun Context.shareTextUrl(url: String) {
    val share = Intent(Intent.ACTION_SEND)
    share.type = "text/plain"
    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)

    // Add data to the intent, the receiving app will decide
    // what to do with it.
    share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post")
    share.putExtra(Intent.EXTRA_TEXT, url)
    this.startActivity(Intent.createChooser(share,"Share link!"))
}