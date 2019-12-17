package com.ise.virtualjukebox.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ise.virtualjukebox.R
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Login GUI fragment
 *
 */
class LoginFragment : Fragment() {

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    /**
     * on view creation pass arguments
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    /**
     * After View creation
     *
     * @param view created view
     * @param savedInstanceState save state from previous view
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnConnect.setOnClickListener {
            (activity as MainActivity).mainl.disconnectAllServer()
            if(!(activity as MainActivity).loginh.setUserName(txeUsername.text.toString())) {
                Toast.makeText(it!!.context, "Username can not be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!(activity as MainActivity).loginh.setServerIP(txeServerIP.text.toString())) {
                Toast.makeText(it!!.context, "Server IP address can not be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tmpRetClass = (activity as MainActivity).loginh.createConnection()
            if (tmpRetClass.success) {
                Toast.makeText(it!!.context, "Connection successfully created.", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(it!!.context, "Connection failed to create (${tmpRetClass.errorMessage}).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            (activity as MainActivity).switchFragment(Screens.Playlist)
        }
    }
}