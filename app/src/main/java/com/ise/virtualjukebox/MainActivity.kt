package com.ise.virtualjukebox

//import android.R

import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
/*
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException*/

import androidx.appcompat.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.Response
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONException


class MainActivity : AppCompatActivity() {
    var etGitHubUser // This will be a reference to our GitHub username input.
            : EditText? = null
    var btnGetRepos // This is a reference to the "Get Repos" button.
            : Button? = null
    var tvRepoList // This will reference our repo list text box.
            : TextView? = null
    var requestQueue // This is our requests queue to process our HTTP requests.
            : RequestQueue? = null

    var baseUrl =
        "https://api.github.com/users/" // This is the API base URL (GitHub API)

    var url // This will hold the full URL which will include the username entered in the etGitHubUser.
            : String? = null
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // This is some magic for Android to load a previously saved state for when you are switching between actvities.
        setContentView(R.layout.activity_main) // This links our code to our layout which we defined earlier.
        etGitHubUser =
            findViewById(R.id.et_github_user) as EditText // Link our github user text box.
        btnGetRepos =
            findViewById(R.id.btn_get_repos) as Button // Link our clicky button.
        tvRepoList =
            findViewById(R.id.tv_repo_list) as TextView // Link our repository list text output box.
        tvRepoList!!.movementMethod =
            ScrollingMovementMethod() // This makes our text box scrollable, for those big GitHub contributors with lots of repos :)
        requestQueue =
            Volley.newRequestQueue(this) // This setups up a new request queue which we will need to make HTTP requests.
    }

    private fun clearRepoList() { // This will clear the repo list (set it as a blank string).
        tvRepoList!!.text = ""
    }

    private fun addToRepoList(
        repoName: String,
        lastUpdated: String
    ) { // This will add a new repo to our list.
// It combines the repoName and lastUpdated strings together.
// And then adds them followed by a new line (\n\n make two new lines).
        val strRow = "$repoName / $lastUpdated"
        val currentText = tvRepoList!!.text.toString()
        tvRepoList!!.text = currentText + "\n\n" + strRow
    }

    private fun setRepoListText(str: String) { // This is used for setting the text of our repo list box to a specific string.
// We will use this to write a "No repos found" message if the user doens't have any.
        tvRepoList!!.text = str
    }

    private fun getRepoList(username: String) { // First, we insert the username into the repo url.
// The repo url is defined in GitHubs API docs (https://developer.github.com/v3/repos/).
        url = baseUrl + username + "/repos"
        // Next, we create a new JsonArrayRequest. This will use Volley to make a HTTP request
// that expects a JSON Array Response.
// To fully understand this, I'd recommend readng the office docs: https://developer.android.com/training/volley/index.html
        val arrReq = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener<JSONArray?> {
                response ->// Check the length of our response (to see if the user has any repose
                    if (response != null) { // The user does have repos, so let's loop through them all.
                        for (i in 0 until response?.length()) {
                            try {  // For each repo, add a new line to our repo list.
                                val jsonObj = response?.getJSONObject(i)
                                if(jsonObj != null){
                                    val repoName = jsonObj["name"].toString()
                                    val lastUpdated = jsonObj["updated_at"].toString()
                                    addToRepoList(repoName, lastUpdated)
                                }
                                //Log.e("Volley", response.toString())

                            } catch (e: JSONException) { // If there is an error then output this to the logs.
                                Log.e("Volley", "Invalid JSON Object.")
                            }
                        }
                    } else { // The user didn't have any repos.
                        setRepoListText("No repos found.")
                    }

            },
            Response.ErrorListener {
                error ->// If there a HTTP error then add a note to our repo list.
                    setRepoListText("Error while calling REST API")
                    Log.e("Volley", error.toString())

            }
        )
        // Add the request we just defined to our request queue.
// The request queue will automatically handle the request as soon as it can.
        requestQueue!!.add<JSONArray>(arrReq)
    }

    fun getReposClicked(v: View?) { // Clear the repo list (so we have a fresh screen to add to)
        clearRepoList()
        // Call our getRepoList() function that is defined above and pass in the
// text which has been entered into the etGitHubUser text input field.
        getRepoList(etGitHubUser!!.text.toString())
    }
}

