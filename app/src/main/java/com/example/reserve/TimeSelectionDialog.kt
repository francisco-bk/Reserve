package com.example.reserve

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TimeSelectionDialog : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.dialog_time_selection, container, false)


        Log.d("CONTEXTCLUES", context.toString())
        var am12 = view.findViewById<TableLayout>(R.id.am12)
        var am1 = view.findViewById<TableLayout>(R.id.am1)
        var am2 = view.findViewById<TableLayout>(R.id.am2)
        var am3 = view.findViewById<TableLayout>(R.id.am3)
        var am4 = view.findViewById<TableLayout>(R.id.am4)
        var am5 = view.findViewById<TableLayout>(R.id.am5)
        var am6 = view.findViewById<TableLayout>(R.id.am6)
        var am7 = view.findViewById<TableLayout>(R.id.am7)
        var am8 = view.findViewById<TableLayout>(R.id.am8)
        var am9 = view.findViewById<TableLayout>(R.id.am9)
        var am10 = view.findViewById<TableLayout>(R.id.am10)
        var am11 = view.findViewById<TableLayout>(R.id.am11)
        var pm12 = view.findViewById<TableLayout>(R.id.pm12)
        var pm1 = view.findViewById<TableLayout>(R.id.pm1)
        var pm2 = view.findViewById<TableLayout>(R.id.pm2)
        var pm3 = view.findViewById<TableLayout>(R.id.pm3)
        var pm4 = view.findViewById<TableLayout>(R.id.pm4)
        var pm5 = view.findViewById<TableLayout>(R.id.pm5)
        var pm6 = view.findViewById<TableLayout>(R.id.pm6)
        var pm7 = view.findViewById<TableLayout>(R.id.pm7)
        var pm8 = view.findViewById<TableLayout>(R.id.pm8)
        var pm9 = view.findViewById<TableLayout>(R.id.pm9)
        var pm10 = view.findViewById<TableLayout>(R.id.pm10)
        var pm11 = view.findViewById<TableLayout>(R.id.pm11)

        am12.setOnClickListener {
//            timeSelectButton.text = (it as Button).text
//            TimeSelectionDialog.dismiss()
        }

        return view
    }
}