package com.example.newsaggregatorapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

class ImageLoad extends AsyncTask<String, Void, Bitmap> {
        ImageView img;
        MainActivity mainActivity;
        public ImageLoad(ImageView img, MainActivity mainActivity) {
            this.img = img;
            this.mainActivity = mainActivity;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
               img.setImageBitmap(result);
            }
            if (result == null) {
               img.setImageResource(mainActivity.getResources().getIdentifier("brokenimage", "drawable", mainActivity.getPackageName()));
            }
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitMap;
            try {
                InputStream inputStr = new java.net.URL(strings[0]).openStream();
                bitMap = BitmapFactory.decodeStream(inputStr);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                bitMap = null;
                e.printStackTrace();
            }
            return bitMap;
        }

}
