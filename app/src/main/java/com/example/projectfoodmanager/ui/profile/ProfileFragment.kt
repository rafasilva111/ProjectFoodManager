package com.example.projectfoodmanager.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.projectfoodmanager.R
import com.example.projectfoodmanager.databinding.FragmentProfileBinding

import com.example.projectfoodmanager.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    val authViewModel: AuthViewModel by viewModels()
    val TAG: String = "ProfileFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,

    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        Log.d(TAG, "olaaa: ")
        Log.d(TAG, "olaaa: "+findNavController())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logoutIB.setOnClickListener {
            authViewModel.logout {

                findNavController().navigate(R.id.action_recipeListingFragment_to_loginFragment)
                val teste = view.findViewById<View>(R.id.bottomNavigationView)
                Log.d(TAG, "onViewCreated: ")

            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}