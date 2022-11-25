/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.android_me.ui;


import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.android.android_me.R;
import com.example.android.android_me.data.AndroidImageAssets;

// This activity will display a custom Android image composed of three body parts: head, body, and legs
public class AndroidMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_me);

        if (savedInstanceState == null) {

            Bundle b = getIntent().getExtras();
            int headIndex = b.getInt("headindex");
            int bodyindex = b.getInt("bodyindex");
            int legindex = b.getInt("legindex");

            BodyPartFragment headPartFragment = new BodyPartFragment();
            BodyPartFragment bodyPartFragment = new BodyPartFragment();
            BodyPartFragment legPartFragment = new BodyPartFragment();

            headPartFragment.setmImageId(AndroidImageAssets.getHeads());
            headPartFragment.setmListIndex(headIndex);

            bodyPartFragment.setmImageId(AndroidImageAssets.getBodies());
            bodyPartFragment.setmListIndex(bodyindex);

            legPartFragment.setmImageId(AndroidImageAssets.getLegs());
            legPartFragment.setmListIndex(legindex);

            FragmentManager manager = getSupportFragmentManager();

            manager.beginTransaction().add(R.id.head_container, headPartFragment).commit();
            manager.beginTransaction().add(R.id.body_container, bodyPartFragment).commit();
            manager.beginTransaction().add(R.id.leg_container, legPartFragment).commit();

        }
    }
}
