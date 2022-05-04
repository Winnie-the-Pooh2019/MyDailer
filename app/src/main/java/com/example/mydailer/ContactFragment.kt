package com.example.mydailer

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydailer.adapter.DayAdapter
import com.example.mydailer.domain.objects.Phone
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    
    private val currentContext by lazy { 
        activity!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        CoroutineScope(Dispatchers.Main).launch {
            val phoneList: Array<Phone> =
                try {
                    Gson().fromJson(downloadJson(), Array<Phone>::class.java)
                } catch (e: Exception) {
                    Timber.e(e)
                    emptyArray()
                }

            val recyclerView = currentContext.findViewById<RecyclerView>(R.id.recycler_view).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = DayAdapter(phoneList.toList())
            }

            val search = currentContext.findViewById<EditText>(R.id.text_search)
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

                    val sharedPreferences = currentContext.getPreferences(Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        Timber.w(p0.toString())
                        putString(getString(R.string.preferences_key), p0.toString())
                        apply()
                    }
                }
            })
            search.setText(currentContext.getPreferences(Context.MODE_PRIVATE).let {
                return@let it.getString(this@ContactFragment.getString(R.string.preferences_key), "")!!
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    private fun Phone.searchPhone(): Boolean {
        val searchString = currentContext.findViewById<TextView>(R.id.text_search).text

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String? = null, param2: String? = null) =
            ContactFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}