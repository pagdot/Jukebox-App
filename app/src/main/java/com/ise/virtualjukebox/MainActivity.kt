package com.ise.virtualjukebox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.text.method.ScrollingMovementMethod
import android.util.Log

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Call
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


import java.io.IOException


class MainActivity : AppCompatActivity() {

    var etGitHubUser // This will be a reference to our GitHub username input.
            : EditText? = null
    var btnGetRepos // This is a reference to the "Get Repos" button.
            : Button? = null
    var tvRepoList // This will reference our repo list text box.
            : TextView? = null
    /*var requestQueue // This is our requests queue to process our HTTP requests.
            : RequestQueue? = null*/
    var client = OkHttpClient()

    var baseUrl =
        "https://api.github.com/users/" // This is the API base URL (GitHub API)

    var url // This will hold the full URL which will include the username entered in the etGitHubUser.
            : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etGitHubUser =
            findViewById(R.id.et_github_user) as EditText // Link our github user text box.
        btnGetRepos =
            findViewById(R.id.btn_get_repos) as Button // Link our clicky button.
        tvRepoList =
            findViewById(R.id.tv_repo_list) as TextView // Link our repository list text output box.
        tvRepoList!!.movementMethod =
            ScrollingMovementMethod() // This makes our text box scrollable, for those big GitHub contributors with lots of repos :)
    }

    private fun clearRepoList() { // This will clear the repo list (set it as a blank string).
        tvRepoList!!.text = ""
    }

    private fun addToRepoList(repoName: String, lastUpdated: String) { // This will add a new repo to our list.
// It combines the repoName and lastUpdated strings together.
// And then adds them followed by a new line (\n\n make two new lines).
        val strRow = "$repoName / $lastUpdated"
        val currentText = tvRepoList!!.text.toString()
        setRepoListText(currentText + "\n\n" + strRow)
    }

    private fun setRepoListText(str: String) { // This is used for setting the text of our repo list box to a specific string.
// We will use this to write a "No repos found" message if the user doens't have any.
        this.runOnUiThread(java.lang.Runnable {
            tvRepoList!!.text = str
        })
    }

    private fun getRepoList(username: String) { // First, we insert the username into the repo url.
// The repo url is defined in GitHubs API docs (https://developer.github.com/v3/repos/).
        url = baseUrl + username + "/repos"
        // Next, we create a new JsonArrayRequest. This will use Volley to make a HTTP request
// that expects a JSON Array Response.
// To fully understand this, I'd recommend readng the office docs: https://developer.android.com/training/volley/index.html

        val request = Request.Builder()
            .method("GET", null)
            .url(url.toString())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                setRepoListText("Error while calling REST API")
                Log.e("Volley", e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                if (response != null && response.code == 200) { // The user does have repos, so let's loop through them all.
                    //Log.e("Volley", "VALID Object.")
                    val jsonDataString = response.body?.string()
                    val jsonDataArray = JSONArray(jsonDataString)

                    for (i in 0 until jsonDataArray.length()) {
                        try {  // For each repo, add a new line to our repo list.
                            val jsonObj = jsonDataArray?.getJSONObject(i)
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
            }
        })
    }

    fun getReposClicked(v: View?) { // Clear the repo list (so we have a fresh screen to add to)
        clearRepoList()
        // Call our getRepoList() function that is defined above and pass in the
// text which has been entered into the etGitHubUser text input field.
        getRepoList(etGitHubUser!!.text.toString())
    }
}
