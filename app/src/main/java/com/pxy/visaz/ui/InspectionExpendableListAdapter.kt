package com.pxy.visaz.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.pxy.visaz.databinding.ItemGroupHeaderBinding
import com.pxy.visaz.databinding.ItemListHeaderBinding
import com.pxy.visaz.domain.model.InspectionDetails
import com.pxy.visaz.domain.model.InspectionItemType


class InspectionExpendableListAdapter(
    private val context: Context,
    private val expandableTitleList: List<String>,
    private val expandableDetailList: HashMap<String, MutableList<InspectionDetails>>
) : BaseExpandableListAdapter() {

    interface InspectionListClickListener {
        fun onObservationClicked(groupPosition: Int, childPosition: Int)
        fun onRemarksClicked(groupPosition: Int, childPosition: Int)
    }

    private var inspectionListClickListener: InspectionListClickListener? = null

    fun setInspectionListClickListener(listener: InspectionListClickListener) {
        inspectionListClickListener = listener
    }

    // Gets the data associated with the given child within the given group.
    override fun getChild(listPosition: Int, expandedListposition: Int): List<InspectionDetails> {
        return expandableDetailList[expandableTitleList[listPosition]] ?: arrayListOf()
    }

    fun updateInspectionDetails(groupPosition: Int, childPosition: Int, inspectionDetails: InspectionDetails) {
        expandableDetailList[expandableTitleList[groupPosition]]?.set(
            childPosition,
            inspectionDetails
        )
        // Notify the adapter that data has changed
        notifyDataSetChanged()
    }

    // Gets the ID for the given child within the given group.
    // This ID must be unique across all children within the group. Hence we can pick the child uniquely
    override fun getChildId(listPosition: Int, expandedListposition: Int): Long {
        return expandedListposition.toLong()
    }

    // Gets the number of children in a specified group.
    override fun getChildrenCount(listPosition: Int): Int {
        return expandableDetailList[expandableTitleList[listPosition]]?.size ?: 0
    }

    // Gets the data associated with the given group.
    override fun getGroup(listPosition: Int): String {
        return expandableTitleList[listPosition]
    }

    // Gets the number of groups.
    override fun getGroupCount(): Int {
        return expandableTitleList.size
    }

    // Gets the ID for the group at the given position. This group ID must be unique across groups.
    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val binding: ItemGroupHeaderBinding
        var view = convertView // Create a mutable variable for convertView

        if (view == null) {
            // Inflate the view using binding
            binding =
                ItemGroupHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            // Reuse the existing binding
            binding = view.tag as ItemGroupHeaderBinding
        }

        // Bind data to view
        val listTitle = getGroup(groupPosition) as String
        binding.tvInspectionTitle.text = listTitle

        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val binding: ItemListHeaderBinding
        var view = convertView // Create a mutable variable for convertView

        if (view == null) {
            // Inflate the view using binding
            binding =
                ItemListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            // Reuse the existing binding
            binding = view.tag as ItemListHeaderBinding
        }

        // Bind data to view
        val inspectionDetailsList = getChild(groupPosition, childPosition)
        with(binding) {
            inspectionDetailsList[childPosition].let {
                if (it.inspectionItemType == InspectionItemType.LIST_ITEM) {
                    tvSno.text = it.sno.toString()
                    tvDetails.text = it.details
                    tvRequired.text = it.required
                    tvObservation.text = it.observation
                    tvRemarks.text = it.remarks
                    tvObservation.setOnClickListener {
                        inspectionListClickListener?.onObservationClicked(
                            groupPosition,
                            childPosition
                        )
                        //Toast.makeText(context, "clicked on observation", Toast.LENGTH_SHORT).show()
                    }
                    tvRemarks.setOnClickListener {
                        //Toast.makeText(context, "clicked on remarks", Toast.LENGTH_SHORT).show()
                        inspectionListClickListener?.onRemarksClicked(groupPosition, childPosition)
                    }
                } else {
                    tvSno.text = it.sno
                    tvDetails.text = it.details
                    tvRequired.text = it.required
                    tvObservation.text = it.observation
                    tvRemarks.text = it.remarks
                }
            }
        }

        return view
    }


    // Indicates whether the child and group IDs are stable across changes to the underlying data.
    override fun hasStableIds(): Boolean {
        return false
    }

    // Whether the child at the specified position is selectable.
    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}
