package com.ise.virtualjukebox.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ise.virtualjukebox.R

class MainActivityFragment : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private val testFragment = SearchFragment.newInstace()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_layout)

        switchFragment(Screens.Login)

        //btnOne.setOnClickListener{
        //    val fragmentTransaction = fragmentManager.beginTransaction()
        //    fragmentTransaction.replace(R.id.myFragment, testFragment)
        //    fragmentTransaction.commit()
        //}

        //val fragmentTransaction = fragmentManager.beginTransaction()
        //fragmentTransaction.replace(R.id.myFragment, testFragment)
        //fragmentTransaction.commit()
    }


    // switch current fragment on screen with other fragment
    fun switchFragment(screenName: Screens) {

        val fragmentTransaction = fragmentManager.beginTransaction()
        val transaction = supportFragmentManager.beginTransaction()

        when (screenName) {
            Screens.Login -> {
                // Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentContainer, LoginFragment())
            }
            Screens.Playlist -> {
                // Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentContainer, PlaylistFragment())
            }
            Screens.Search -> {
                // Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentContainer, SearchFragment())
            }
            Screens.Settings -> {
                // Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentContainer, SettingsFragment())
            }
            else -> {
                // Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.fragmentContainer, LoginFragment())
            }
        }

        // Commit the transaction
        fragmentTransaction.commit()
    }
}
