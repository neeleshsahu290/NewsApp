package com.example.newsapp.utility

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.format.DateUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


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

fun TextView.timeConvertFromString(timeString: String){
    val inputFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val date: Date = inputFormat.parse(timeString) as Date
    val niceDateStr = DateUtils.getRelativeTimeSpanString(
        date.time,
        Calendar.getInstance().timeInMillis,
        DateUtils.SECOND_IN_MILLIS
    )
    this.text=niceDateStr
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


 fun Context.saveImage(string: String) {
     CoroutineScope(Dispatchers.IO).launch {
         val url: URL = mStringToURL(string)!!
         val connection: HttpURLConnection?
         try {
             connection = url.openConnection() as HttpURLConnection
             connection.connect()
             val inputStream: InputStream = connection.inputStream
             val bufferedInputStream = BufferedInputStream(inputStream)
             val bitmap = BitmapFactory.decodeStream(bufferedInputStream)
           //  mSaveMediaToStorage(bitmap, requ)

             val filename = "${System.currentTimeMillis()}.jpg"
             var fos: OutputStream? = null
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                 contentResolver?.also { resolver ->
                     val contentValues = ContentValues().apply {
                         put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                         put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                         put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                     }
                     val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                     fos = imageUri?.let { resolver.openOutputStream(it) }
                 }
             } else {
                 val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                 val image = File(imagesDir, filename)
                 fos = FileOutputStream(image)
             }
             fos?.use {
                 bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                 Handler(Looper.getMainLooper()).post {
                     //code that runs in main
                     Toast.makeText(applicationContext , "Saved to Gallery" , Toast.LENGTH_SHORT).show()

                 }
             }
         } catch (e: IOException) {
             e.printStackTrace()
             Handler(Looper.getMainLooper()).post {
                 //code that runs in main
                 Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()

             }
         }
     }
}
private fun mStringToURL(string: String): URL? {
    try {
        return URL(string)
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    }
    return null
}



