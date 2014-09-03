package com.android.volley;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tv.ui.metro.R;
import org.json.JSONObject;

public class VolleyTestMainActivity extends Activity {

	private TextView txtDisplay;
	private RequestQueue mRequestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main);

		txtDisplay = (TextView) findViewById(R.id.txtDisplay);

		// Instantiate the cache
		Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
		// Set up the network to use HttpURLConnection as the HTTP client.
		Network network = new BasicNetwork(new HurlStack());
		// Instantiate the RequestQueue with the cache and network.
		mRequestQueue = new RequestQueue(cache, network);
		// Start the queue
		mRequestQueue.start();
		
		String url = "http://172.27.9.104:9300/testdata/1/1/1/zh/CN?api=index";

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						txtDisplay.setText("Response => " + response.toString());
						findViewById(R.id.progressBar1).setVisibility(View.GONE);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						txtDisplay.setText("VolleyError => " + error.toString());
						findViewById(R.id.progressBar1).setVisibility(View.GONE);
					}
				});

		mRequestQueue.add(jsObjRequest);

	}

}
