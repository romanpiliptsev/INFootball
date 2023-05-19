package com.example.infootball.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.infootball.databinding.FragmentTeamInfoBinding

private const val ARG_PARAM_ADDRESS = "param_address"
private const val ARG_PARAM_WEBSITE = "param_website"
private const val ARG_PARAM_FOUNDED = "param_founded"
private const val ARG_PARAM_COLORS = "param_colors"
private const val ARG_PARAM_VENUE = "param_venue"

class TeamInfoFragment : Fragment() {

    private var paramAddress: String? = null
    private var paramWebsite: String? = null
    private var paramFounded: Int? = null
    private var paramColors: String? = null
    private var paramVenue: String? = null

    private val binding by lazy {
        FragmentTeamInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramAddress = it.getString(ARG_PARAM_ADDRESS)
            paramWebsite = it.getString(ARG_PARAM_WEBSITE)
            paramFounded = it.getInt(ARG_PARAM_FOUNDED)
            paramColors = it.getString(ARG_PARAM_COLORS)
            paramVenue = it.getString(ARG_PARAM_VENUE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            stadiumName.text = paramVenue
            address.text = paramAddress
            website.text = paramWebsite
            founded.text = paramFounded.toString()
            colors.text = paramColors
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(
            paramAddress: String,
            paramWebsite: String,
            paramFounded: Int,
            paramColors: String,
            paramVenue: String
        ) = TeamInfoFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM_ADDRESS, paramAddress)
                putString(ARG_PARAM_WEBSITE, paramWebsite)
                putInt(ARG_PARAM_FOUNDED, paramFounded)
                putString(ARG_PARAM_COLORS, paramColors)
                putString(ARG_PARAM_VENUE, paramVenue)
            }
        }
    }
}