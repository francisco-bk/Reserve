package com.example.reserve

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.IOException


class HallFragment : Fragment() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : Adapter
    private lateinit var layoutManager : LinearLayoutManager
    private val roomList = mutableListOf<Room>()

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val roomJsonAdapter : JsonAdapter<Room> = moshi.adapter(Room::class.java)
    private val roomListType = Types.newParameterizedType(List::class.java, Room::class.java)
    private val roomListJsonAdapter : JsonAdapter<List<Room>> = moshi.adapter(roomListType)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hall, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)

        // temp data being added, waiting for backend
        for(i in 1..40) {
            roomList.add(Room("West", "UPS" + (i+200).toString(), "Upson", "", "11/20/2021", "Saturday"))
        }

        adapter = Adapter(roomList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HallFragment().apply {

            }
    }

    private fun populateRepository() {
        val getRequest = Request.Builder().url(Repository.BASE_URL + "rooms/").build()
        client.newCall(getRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("GET_ERROR", e.printStackTrace().toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.d("GET_ERROR", "Response unsuccessful")
                    }
                    val roomList = roomListJsonAdapter.fromJson(response.body!!.string())!!
                    adapter = Adapter(roomList)
                    activity!!.runOnUiThread {
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = layoutManager
                    }
                }
            }
        })

    }
}