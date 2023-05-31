package com.example.infootball.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.infootball.InfootballApp
import com.example.infootball.databinding.FragmentMatchInfoBinding

private const val ARG_VENUE = "param_venue"
private const val ARG_REFEREES = "param_referees"

class MatchInfoFragment : Fragment() {

    private val component by lazy {
        (activity?.application as InfootballApp).component
    }

    private var paramVenue: String? = null
    private var paramReferees: ArrayList<String>? = null

    private val binding by lazy {
        FragmentMatchInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramVenue = it.getString(ARG_VENUE)
            paramReferees = it.getStringArrayList(ARG_REFEREES)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when {
            paramReferees?.isEmpty() == true && paramVenue == "" -> {
                binding.referee.visibility = View.GONE
                binding.whistle.visibility = View.GONE
                binding.stadium.visibility = View.GONE
                binding.stadiumImg.visibility = View.GONE
                binding.noInfo.visibility = View.VISIBLE
            }
            paramReferees?.isEmpty() == true -> {
                binding.stadium.text = "Stadium: $paramVenue"
                binding.referee.visibility = View.GONE
                binding.whistle.visibility = View.GONE
            }
            paramVenue == "" -> {
                binding.referee.text = "Referees: ${paramReferees?.joinToString(", ")}"
                binding.stadium.visibility = View.GONE
                binding.stadiumImg.visibility = View.GONE
            }
            else -> {
                binding.referee.text = "Referees: ${paramReferees?.joinToString(", ")}"
                binding.stadium.text = "Stadium: $paramVenue"
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(paramVenue: String, paramReferees: ArrayList<String>) =
            MatchInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_VENUE, paramVenue)
                    putStringArrayList(ARG_REFEREES, paramReferees)
                }
            }
    }
}