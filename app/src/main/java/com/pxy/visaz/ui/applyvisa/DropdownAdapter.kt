package com.pxy.visaz.ui.applyvisa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.pxy.visaz.R
import com.pxy.visaz.core.model.visa.Country


class DropdownAdapter(
    private val mContext: Context,
    list: ArrayList<Country>
) : ArrayAdapter<Country?>(mContext, 0, list as List<Country?>) {
    private var moviesList: List<Country> = ArrayList()

    init {
        moviesList = list
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var listItem = convertView
        if (listItem == null) listItem =
            LayoutInflater.from(mContext).inflate(R.layout.item_dropdown, parent, false)

        val currentMovie = moviesList[position]

        val name = listItem?.findViewById<View>(R.id.tvName) as TextView
        name.text = currentMovie.name

        return listItem
    }
}