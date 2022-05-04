package com.example.mydailer

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydailer.adapter.DayAdapter
import com.example.mydailer.domain.objects.Phone
import com.google.gson.Gson
import kotlinx.coroutines.*
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        setSupportActionBar(findViewById(R.id.toolbar))

        CoroutineScope(Dispatchers.Main).launch {
            val phoneList: Array<Phone> =
                try {
                    Gson().fromJson(downloadJson(), Array<Phone>::class.java)
                } catch (e: Exception) {
                    Timber.e(e)
                    emptyArray()
                }

            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = DayAdapter(phoneList.toList())
            }

            val search = findViewById<EditText>(R.id.text_search)
            search.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val adapter = recyclerView.adapter!! as DayAdapter
                    adapter.phoneList = adapter.original.filter { it.searchPhone() }
                    adapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0 == null) {
                        Timber.e("no edit text found")
                        return
                    }

                    val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        Timber.w(p0.toString())
                        putString(getString(R.string.preferences_key), p0.toString())
                        apply()
                    }
                }
            })
            search.setText(getPreferences(Context.MODE_PRIVATE).let {
                return@let it.getString(this@MainActivity.getString(R.string.preferences_key), "")!!
            })
        }
    }

    private fun Phone.searchPhone(): Boolean {
        val searchString = findViewById<TextView>(R.id.text_search).text

        return this.phone.contains(searchString) || this.name.contains(searchString)
                || this.type.contains(searchString) || searchString.isEmpty()
    }

    private suspend fun downloadJson(): String = withContext(Dispatchers.IO) {
        val connection = URL(getString(R.string.download_link))
            .openConnection() as HttpURLConnection
        try {
            connection.inputStream.bufferedReader().readText()
        } finally {
            connection.disconnect()
        }
    }
}