package com.example.developer.n001;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private ListView mListView;
    private ListViewNewsAdapter listViewNewsAdapter;
    private ArrayList<ListViewNewsItem> listViewNewsItems;

    private JSONParser jsonParser = new JSONParser();

    private String READNEWS_URL ="http://csprojects.esy.es/mydb/readnews.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.action_refresh:
                AddNewsActivity addNewsActivity = new AddNewsActivity(this);
                addNewsActivity.show();
                return true;
            case R.id.action_add:
                new GetNewsTask().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {

    }

    private class GetNewsTask extends AsyncTask<Void, Void, Boolean>
    {
        private ProgressDialog mProgressDialog;

        private JSONObject jsonObjectResult = null;

        private String error;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            listViewNewsItems = new ArrayList<ListViewNewsItem>();
            mProgressDialog = ProgressDialog.show(MainActivity.this,
                    "Processing...", "Get last news", false, false);
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            jsonObjectResult=jsonParser.makeHtttpRequest(READNEWS_URL,null);

            if (jsonObjectResult == null)
            {
                error = "Error int the connection";
                return false;
            }

            try
            {
                if (jsonObjectResult.getInt("success") == 1)
                {
                    JSONArray jsonArray = jsonObjectResult.getJSONArray("posts");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject news = jsonArray.getJSONObject(i);

                        ListViewNewsItem listViewNewsItem = new ListViewNewsItem
                                (
                                        news.getInt("news_id"),
                                        news.getString("news_title"),
                                        news.getString("news_date"),
                                        news.getString("news_body")
                                );
                        listViewNewsItems.add(listViewNewsItem);
                    }
                    return true;
                }
                else
                    error = jsonObjectResult.getString("message");

            }
            catch (Exception ex)
            {

            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            mProgressDialog.dismiss();
            if (aBoolean)
            {
                listViewNewsAdapter = new ListViewNewsAdapter(getApplicationContext(),
                        listViewNewsItems);
                mListView.setAdapter(listViewNewsAdapter);
            }
            else
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
        }
    }
}
