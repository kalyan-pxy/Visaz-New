package com.pxy.visaz.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.pxy.visaz.R
import com.pxy.visaz.core.BaseFragment
import com.pxy.visaz.databinding.FragmentDashboardBinding


class DashboardFragment : BaseFragment() {

    private lateinit var binding: FragmentDashboardBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // Set up NavController
        val navController =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        navController?.let {
            binding.bottomNavigationView.setupWithNavController(navController)
            it.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.home ||
                    destination.id == R.id.profile
                ) {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                } else {
                    binding.bottomNavigationView.visibility = View.GONE
                }
            }
        }

        return binding.root
    }
}