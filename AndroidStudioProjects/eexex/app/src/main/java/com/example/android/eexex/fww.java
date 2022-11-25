package com.example.android.eexex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class fww extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fww);



       Button bCreatetwo = (Button)findViewById(R.id.bottom);
        final EditText eActNametwo = (EditText)findViewById(R.id.edit);
        final EditText eActNametwo2 = (EditText)findViewById(R.id.edit2);
        final EditText eActNametwo3 = (EditText)findViewById(R.id.edit3);
        final EditText eActNametwo4 = (EditText)findViewById(R.id.edit4);

        bCreatetwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(Contract.PetEntry.alsoura,eActNametwo.getText().toString().trim());
                values.put(Contract.PetEntry.alaya, eActNametwo2.getText().toString().trim());
                values.put(Contract.PetEntry.algza, eActNametwo3.getText().toString().trim());
                values.put(Contract.PetEntry.alsafha, eActNametwo4.getText().toString().trim());
                Helper helper = new Helper(fww.this);
                SQLiteDatabase database = helper.getWritableDatabase();

                database.insert(Contract.PetEntry.TABLE_NAME,null,values);
                fww.this.notifyAll();

                Toast.makeText(fww.this, "تم الحفظ بنجاح ", Toast.LENGTH_SHORT).show();
                finish();



            }
        });







    }




}
