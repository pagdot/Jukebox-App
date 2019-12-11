package com.ise.virtualjukebox.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ise.virtualjukebox.R
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

        btnConnect.setOnClickListener {
            // set serverIP
            if(!(activity as MainActivityFragment).loginh.setServerIP(txeServerIP.text.toString())) {
                Toast.makeText(it.context, "Connect with server not successful.", Toast.LENGTH_SHORT)
                    .show()
            }

            // set username
            (activity as MainActivityFragment).loginh.setUserName(txeUsername.text.toString())

            // switch fragment
            (activity as MainActivityFragment).switchFragment(Screens.Playlist)
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}