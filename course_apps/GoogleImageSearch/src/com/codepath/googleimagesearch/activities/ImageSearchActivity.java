package com.codepath.googleimagesearch.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.codepath.googleimagesearch.ImageFilterActivity;
import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.adapters.ImageResultAdapter;
import com.codepath.googleimagesearch.models.ImageFilter;
import com.codepath.googleimagesearch.models.ImageResult;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ImageSearchActivity extends Activity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener {
	
	private StaggeredGridView gvResults;
	private ArrayList<ImageResult> imageResults;
	private ImageResultAdapter aImageResults;
	private Context context;
	private String query = "android";
	private SearchView searchView;
	private ImageFilter imageFilter;
	
	public static final String COLOR_KEY = "imgcolor";
	public static final String SIZE_KEY = "imgsz";
	public static final String TYPE_KEY = "imgtype";
	public static final String SITE_KEY = "as_sitesearch";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_search);
				
		context = this;
		imageFilter = new ImageFilter();
		// creates the data source		
		imageResults = new ArrayList<ImageResult>();
		setupViews();
	}

	private void setupViews() {
		gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
		
		// attach the datasource to the adapter
		aImageResults = new ImageResultAdapter(this, imageResults);
		// link the adapter to the adapterview
		gvResults.setAdapter(aImageResults);
		
		// OnItemClickListener
		gvResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// creating the intent
				Intent i = new Intent(ImageSearchActivity.this, ImageDisplayActivity.class);
				// get the image result to display
				ImageResult imageResult = imageResults.get(position);
				// pass image result into the intent
				i.putExtra("result", imageResult);
				// launch the activity
				startActivity(i);
			}			
		});		
		
		// OnItemLongClickListener
		gvResults.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
	        public boolean onItemLongClick(AdapterView<?> parent, View v, int position, 
	        		long id) {
				// share the image
//	        	onShareItem(v);
	        	return true;
	        }
	    });
		
		// OnScrollListener
		gvResults.setOnScrollListener(new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {
	        	if(page<=8){
	        		fetchImages(query, page);
	        	}
	        }
        });
		fetchImages(query, 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_search, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
	    searchView = (SearchView) searchItem.getActionView();
	    try{
	    	searchView.setOnQueryTextListener(new OnQueryTextListener() {
			       @Override
			       public boolean onQueryTextSubmit(String q) {
			    	   query = q;
			    	   imageResults.clear();
			    	   fetchImages(query, 0);
			           return true;
			       }

			       @Override
			       public boolean onQueryTextChange(String newText) {
			           return false;
			       }
			   });
	    }catch(Exception ex){
	    	System.out.println(ex.getStackTrace());
	    }
	   
		return true;
	}
	
	// dsiplays the Image Filter with advanced filter options
	private void showImageFilter() {
		Intent i = new Intent(ImageSearchActivity.this, ImageFilterActivity.class);
		i.putExtra("result", imageFilter);
		startActivityForResult(i, 26);
	}
	
	private RequestParams constructRequestParam(){
		RequestParams params = new RequestParams();
		
		if(!isEmpty(imageFilter.getColor())){
			params.put(COLOR_KEY, imageFilter.getColor());
		}
		if(!isEmpty(imageFilter.getSize())){
			params.put(SIZE_KEY, imageFilter.getSize());
		}
		if(!isEmpty(imageFilter.getType())){
			params.put(TYPE_KEY, imageFilter.getType());
		}
		if(!isEmpty(imageFilter.getSite())){
			params.put(SITE_KEY, imageFilter.getSite());
		}
		
		return params;
	}
	
	private boolean isEmpty(String str){
		if(str == null || str.trim().length() == 0 || str.equals("") || str.equals("any")){
			return true;
		}
		return false;
	}
	
	// Handle on button click event for Search Button
	public void onImageSearch(View v){
//		query = etQuery.getText().toString();
		fetchImages(query, 0);
		StaggeredGridView gridView;
	}
	
	
	private void fetchImages(String query, int pageNo){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams requestParams = constructRequestParam();
		String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8&start=" + pageNo;
		client.get(searchUrl, requestParams, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Log.d("debug", response.toString());
				JSONArray imageResultsJson = null;
				try{
					imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
//					imageResults.clear();
					aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
					
				}
				catch(JSONException e){
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// Handle the failure and alert the user to retry
			   Toast.makeText(context, "Network failure : "+ throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();				  
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_filter:
	        	showImageFilter();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 26){
			if(resultCode == RESULT_OK){
				imageFilter = (ImageFilter) data.getSerializableExtra("result");
				fetchImages(query, 0);
			}
		}
	}
}
