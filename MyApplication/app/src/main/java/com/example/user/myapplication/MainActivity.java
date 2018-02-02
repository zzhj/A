package com.example.user.myapplication;

import android.annotation.SuppressLint;
import android.app.VoiceInteractor;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView textView1;
    private EditText username;
    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.

    private String baseUrl = "http://172.20.114.216:8010/users/";  // This is the API base URL (GitHub API)
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButton = (Button)findViewById(R.id.button);;
        textView1 = (TextView) findViewById(R.id.textview1);
        username = findViewById(R.id.editText);
        firstname = findViewById(R.id.editText1);
        lastname = findViewById(R.id.editText2);
        email = findViewById(R.id.editText3);
        requestQueue = Volley.newRequestQueue(this);
        textView1.setMovementMethod(new ScrollingMovementMethod());
    }

    private void clearRepoList() {
        // This will clear the repo list (set it as a blank string).
        this.textView1.setText("");
    }

    @SuppressLint("SetTextI18n")
    private void addToRepoList( String username, String first_name, String last_name, String email,int x) {
        // This will add a new repo to our list.
        // It combines the repoName and lastUpdated strings together.
        // And then adds them followed by a new line (\n\n make two new lines).
        String Cur = textView1.getText().toString();
        String strRow =  username + " / " + first_name + " / " + last_name + " / " + email + x;
        this.textView1.setText(Cur+ "\n\n"+ strRow );
    }

    private void setRepoListText(String str) {
        // This is used for setting the text of our repo list box to a specific string.
        // We will use this to write a "No repos found" message if the user doens't have any.
        this.textView1.setText(str);
    }

    private void getRepoList(String username) {
        this.url = this.baseUrl;
        final JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Check the length of our response (to see if the user has any repos)
                        textView1.setText(response.toString());
                        if (response.length() > 0) {

                            // The user does have repos, so let's loop through them all.

                         /*   for (int i = 0; i <= response.length(); i++) {
                                try {
                                //    Toast.makeText( MainActivity.this,i, Toast.LENGTH_SHORT).show();
                                    JSONObject jsonObj = response.getJSONObject(i);
                                //    String data = jsonObj.get("data").toString();
                                    String username1 = jsonObj.get("username").toString();
                                    String first_name1 = jsonObj.get("first_name").toString();
                                    String last_name1 = jsonObj.get("last_name").toString();
                                    String email1 = jsonObj.get("email").toString();
                                    addToRepoList( username1,first_name1,last_name1,email1,response.length());
                                } catch (JSONException e) {
                                    // If there is an error then output this to the logs.
                                    Log.e("Volley", "Invalid JSON Object.");
                                }

                            }*/

                        } else {
                            // The user didn't have any repos.
                            setRepoListText("No repos found.");
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        setRepoListText("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
                }
        );
        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(arrReq);
    }

    private void postRepoList(final String user, final String first, final String last, final String email){
        this.url = this.baseUrl;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                       // Log.d("Error.Response", response);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", user);
                params.put("first_name", first);
                params.put("last_name",last);
                params.put("email",email);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }




    public void getReposClicked(View v) {
        // Clear the repo list (so we have a fresh screen to add to)
        clearRepoList();
        // Call our getRepoList() function that is defined above and pass in the
        // text which has been entered into the etGitHubUser text input field.
        getRepoList(username.getText().toString());
    }
    public void postReposClicked(View v){
        Toast.makeText(MainActivity.this,username.getText().toString(),Toast.LENGTH_SHORT).show();
        postRepoList(username.getText().toString(),firstname.getText().toString(),lastname.getText().toString(),email.getText().toString());
    }
}
