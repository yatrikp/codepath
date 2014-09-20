package com.codepath.googleimagesearch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.R.id;
import com.codepath.googleimagesearch.R.layout;
import com.codepath.googleimagesearch.R.menu;
import com.codepath.googleimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		// hide the actionbar
		getActionBar().hide();
		
		// pull the out the url for image to display
		ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra("result");
		// find the image view
		ImageView imageView = (ImageView)findViewById(R.id.ivImageResult);
		// load the image using picassa lib
		Picasso.with(this).load(imageResult.url).into(imageView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
