package com.ise.virtualjukebox.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.ise.virtualjukebox.R
import kotlinx.android.synthetic.main.fragment1_layout.*
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnConnect.setOnClickListener {
            // set serverIP
            if(!(activity as MainActivity).loginh.setServerIP(txeServerIP.text.toString())) {

            }

            // set username
            (activity as MainActivity).loginh.setUserName(txeUsername.text.toString())

            // switch fragment
            (activity as MainActivity).switchFragment(Screens.Playlist)
        }
    }
}