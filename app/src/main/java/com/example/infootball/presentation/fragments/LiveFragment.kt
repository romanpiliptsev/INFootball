package com.example.infootball.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.infootball.databinding.FragmentLiveBinding

class LiveFragment : Fragment() {

    private val binding by lazy {
        FragmentLiveBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = LiveFragment()
    }
}