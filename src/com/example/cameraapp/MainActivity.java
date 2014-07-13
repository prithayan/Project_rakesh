package com.example.cameraapp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final int CAPTURE_IMAGE_CAPTURE_CODE = 0;
	Intent i;
	private ImageButton ib;
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	static String TAG = "CameraApp";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);
		ib = (ImageButton) findViewById(R.id.buttonToast);

		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				// create a file to save the image
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				
				// set the image file name
				i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

				startActivityForResult(i, CAPTURE_IMAGE_CAPTURE_CODE);
			}
		});
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");

		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		Log.d(TAG, "Image-path:" + mediaFile);
		return mediaFile;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_CAPTURE_CODE) {
			if (resultCode == RESULT_OK) {
				Uri photoUri = null;
				if (data == null) {
					photoUri = fileUri;
					// A known bug here! The image should have saved in fileUri
					Toast.makeText(this, "Image saved successfully",Toast.LENGTH_LONG).show();
				} else {
					photoUri = data.getData();
					Toast.makeText(this, "Image saved successfully in: " + data.getData(), 	Toast.LENGTH_LONG).show();
				}
				Toast.makeText(this, "Image Captured", Toast.LENGTH_LONG).show();
				 showPhoto(photoUri);
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Callout for image capture failed!", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_menu, menu);
		return true;
	}
	
	private void showPhoto(Uri photoUri) {
		Intent i = new Intent(getBaseContext(), CapturedImageActivity.class);
		i.putExtra(CapturedImageActivity.capturedImageUri, photoUri.getPath());
		startActivity(i);
	}
}
