package com.example.android.instgramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo);

        setTitle(ParseUser.getCurrentUser().getUsername()+"'s photos");

        final LinearLayout linearLayout = findViewById(R.id.lin);

//        ListView listView = findViewById(R.id.photoListView);
//        final ArrayList<Bit> bitmaps = new ArrayList<Bit>();
//        final BitmapAdapter adapter = new BitmapAdapter(this,bitmaps);

        String username = getIntent().getStringExtra("username");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("username",username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null){
                    if (objects.size()>0 && objects!=null){
                        for (ParseObject object : objects ){
                            ParseFile file = (ParseFile) object.get("Image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);

                                        ImageView imageView = new ImageView(getApplicationContext());
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT) );
                                        imageView.setImageBitmap(bitmap);
                                        linearLayout.addView(imageView);


                                    }
                                }
                            });

                        }
                    }

                }
            }
        });
//        listView.setAdapter(adapter);



    }
}
