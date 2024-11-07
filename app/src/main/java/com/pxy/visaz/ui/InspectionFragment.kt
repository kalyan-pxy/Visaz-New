package com.pxy.visaz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.pxy.visaz.databinding.FragmentInspectionBinding
import com.pxy.visaz.domain.model.Inspections
import org.koin.androidx.viewmodel.ext.android.viewModel


class InspectionFragment : Fragment() {

    private lateinit var binding: FragmentInspectionBinding
    private var expandableListAdapter: InspectionExpendableListAdapter? = null

    companion object {
        fun newInstance() = InspectionFragment()
    }

    private val inspectionViewModel: InspectionViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        with(binding) {
            progressBar.isVisible = true
            inspectionViewModel.getInspections()

            ivRefresh.setOnClickListener {
                progressBar.isVisible = true
                inspectionViewModel.refreshInspections()
            }
            btnSubmit.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "Inspections submitted successfully",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun initObservers() {
        inspectionViewModel.inspectionsObserver.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            displayInspectionList(it)
        }
        inspectionViewModel.inspectionDetailsObserver.observe(viewLifecycleOwner) {
            expandableListAdapter?.updateInspectionDetails(
                it.groupPosition,
                it.childPosition,
                it.inspectionDetails
            )
        }
    }

    private fun displayInspectionList(inspections: Inspections) {
        with(binding) {
            //inspection headers
            inspections.inspectionHeaderOptions?.let {
                tvHeaderOne.text = it.option1
                tvHeaderTwo.text = it.option2
                tvHeaderThree.text = it.option3
                tvHeaderFour.text = it.option4
            }
            //inspection list
            inspections.inspectionDetails?.let { it ->
                val expandableTitleList = ArrayList<String>(it.keys)
                expandableListAdapter =
                    InspectionExpendableListAdapter(requireContext(), expandableTitleList, it)
                expandableListViewSample.setAdapter(expandableListAdapter)

                expandableListAdapter?.setInspectionListClickListener(
                    object : InspectionExpendableListAdapter.InspectionListClickListener {
                        override fun onObservationClicked(groupPosition: Int, childPosition: Int) {
                            val childList = (it[expandableTitleList[groupPosition]]
                                ?: arrayListOf()).toMutableList()
                            val inspectionDetails = childList[childPosition]
                            ObservationEntryDialog.showDialog(
                                parentFragmentManager,
                                inspectionDetails,
                                positiveListener = {
                                    inspectionViewModel.updateInspection(
                                        groupPosition,
                                        childPosition,
                                        it
                                    )
                                },
                                negativeListener = {}
                            )
                        }

                        override fun onRemarksClicked(groupPosition: Int, childPosition: Int) {
                            val childList = (it[expandableTitleList[groupPosition]]
                                ?: arrayListOf()).toMutableList()
                            val inspectionDetails = childList[childPosition]
                            ObservationEntryDialog.showDialog(
                                parentFragmentManager,
                                inspectionDetails,
                                positiveListener = {
                                    inspectionViewModel.updateInspection(
                                        groupPosition,
                                        childPosition,
                                        it
                                    )
                                },
                                negativeListener = {}
                            )
                        }

                    }
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInspectionBinding.inflate(inflater, container, false)
        return binding.root
    }
}