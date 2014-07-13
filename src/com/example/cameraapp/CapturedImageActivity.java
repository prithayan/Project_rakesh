package com.example.cameraapp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

public class CapturedImageActivity extends Activity {

	public static String capturedImageUri = "capturedImage";

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reverse_image_search_layout);

		// showPhoto(getIntent().getExtras().getString(capturedImageUri));
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
			        new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			}
		reverseImageSearchGoogle(getIntent().getExtras().getString(
				capturedImageUri));
	}

	private void showPhoto(String photoUri) {
		if (photoUri == null)
			return;

		File imageFile = new File(photoUri);
		if (imageFile.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imageFile
					.getAbsolutePath());
			ImageView myImage = (ImageView) findViewById(R.id.showImage);
			myImage.setImageBitmap(bitmap);
			Log.d(MainActivity.TAG, "Success!!");
		} else {
			Log.d(MainActivity.TAG, "Hmm..");
		}
	}

	private void reverseImageSearchGoogle(String photoUri) {
		Log.d(MainActivity.TAG, "URL: " + photoUri);
		String charset = "UTF-8";
		String requestURL = "https://www.google.co.in/searchbyimage/upload";

		try {
			MultipartUtility multipart = new MultipartUtility(requestURL,
					charset);

//			multipart.addHeaderField("User-Agent", "CodeJava");
//			multipart.addHeaderField("Test-Header", "Header-Value");
//
//			multipart.addFormField("description", "Cool Pictures");
//			multipart.addFormField("keywords", "Java,upload,Spring");

			multipart.addFilePart("fileUpload", new File(photoUri));

			List<String> response = multipart.finish();

			
			WebView myWebView = (WebView) findViewById(R.id.webview);
//			WebSettings webSettings = myWebView.getSettings();
//			webSettings.setJavaScriptEnabled(true);

			myWebView.getSettings().setJavaScriptEnabled(true);
//			myWebView.loadUrl("http://www.google.com");
			myWebView.loadData(response.toString(), "text/html;", charset);
			
//            for (String line : response) {
//            	Log.d(MainActivity.TAG, line);
//            }
			Log.d(MainActivity.TAG, "Search logs completed");
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

}
