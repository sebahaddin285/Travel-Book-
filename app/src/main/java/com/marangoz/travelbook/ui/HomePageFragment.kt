package com.marangoz.travelbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.marangoz.travelbook.R
import com.marangoz.travelbook.databinding.FragmentHomePageBinding


class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)

        binding.addButton.setOnClickListener() {
            Navigation.findNavController(it).navigate(R.id.passToSaveFragment)
        }


        return binding.root
    }


}