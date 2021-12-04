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

    private val locationArr = arrayOf('C', 'N', 'W', 'E')

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val roomJsonAdapter : JsonAdapter<Room> = moshi.adapter(Room::class.java)
    private val roomListType = Types.newParameterizedType(List::class.java, Room::class.java)
    private val roomListJsonAdapter : JsonAdapter<List<Room>> = moshi.adapter(roomListType)

    private var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hall, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)

        layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)



        populateRepository()

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            HallFragment().apply {
                index = position
            }
    }

    private fun populateRepository() {
        val getRequest = Request.Builder().url(Repository.BASE_URL + "rooms/").build()
        client.newCall(getRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("NETWORK DEBUG", "Room GET error: " + e.printStackTrace().toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.d("NETWORK_DEBUG", "Room GET unsuccessful: $response")
                    }
                    val roomList = roomListJsonAdapter.fromJson(response.body!!.string())!!

                    var sortedRoomList = mutableListOf<Room>()

                    for (room in roomList) {
                        if (room.location[0] == locationArr[index]){
                            sortedRoomList.add(room)
                        }
                    }

                    adapter = Adapter(sortedRoomList)
                    activity!!.runOnUiThread {
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = layoutManager
                    }
                }
            }
        })

    }
}