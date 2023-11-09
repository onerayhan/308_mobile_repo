package com.example.start2.home

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.start2.databinding.FragmentProfileBinding



class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }


    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hard - coded values
        val usernameText = "John Doe"
        val emailText = "john.doe@example.com"
        val followersCount = 100
        val followingCount = 50

        // List of favorite songs
        // List of favorite songs
        val favoriteSongs = arrayOf(
            "Danza Kuduro",
            "Toca Toca",
            "Black Day",
            "Nectarines",
            "Your Mind"
        )
        binding.username.setText(usernameText)
        binding.username.setText(emailText)
        binding.username.setText("Followers: " + followersCount)
        binding.username.setText("Following: " + followingCount)


        val adapter = activity?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_list_item_1,
                favoriteSongs
            )
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(BlankViewModel::class.java)
        // TODO: Use the ViewModel
    }

}