package com.example.android.android_me.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.android_me.R;
import com.example.android.android_me.data.AndroidImageAssets;

public class MainActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener {

    int mHeadIndex ;
    int mBodyInde ;
    int mLegIndex ;
    private boolean mTwoPane ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(findViewById(R.id.android_me_linear_layout) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;

            // Change the GridView to space out the images more on tablet
            GridView gridView = (GridView) findViewById(R.id.images_grid_view);
            gridView.setNumColumns(2);

            // Getting rid of the "Next" button that appears on phones for launching a separate activity
            Button nextButton = (Button) findViewById(R.id.button);
            nextButton.setVisibility(View.GONE);

            if(savedInstanceState == null) {
                // In two-pane mode, add initial BodyPartFragments to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Creating a new head fragment
                BodyPartFragment headFragment = new BodyPartFragment();
                headFragment.setmImageId(AndroidImageAssets.getHeads());
                // Add the fragment to its container using a transaction
                fragmentManager.beginTransaction()
                        .add(R.id.head_container, headFragment)
                        .commit();

                // New body fragment
                BodyPartFragment bodyFragment = new BodyPartFragment();
                bodyFragment.setmImageId(AndroidImageAssets.getBodies());
                fragmentManager.beginTransaction()
                        .add(R.id.body_container, bodyFragment)
                        .commit();

                // New leg fragment
                BodyPartFragment legFragment = new BodyPartFragment();
                legFragment.setmImageId(AndroidImageAssets.getLegs());
                fragmentManager.beginTransaction()
                        .add(R.id.leg_container, legFragment)
                        .commit();
            }
        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
        }

    }


    @Override
    public void onImageSelected(int position) {
        Toast.makeText(this, "position:"+position, Toast.LENGTH_SHORT).show();

        int bodyPartNumber = position / 12 ;

        int listIndex = position - 12 * bodyPartNumber  ;




        if (mTwoPane){
            BodyPartFragment newFreagment = new BodyPartFragment();

            switch (bodyPartNumber) {
                case 0:
                    newFreagment.setmImageId(AndroidImageAssets.getHeads());
                    newFreagment.setmListIndex(listIndex);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.head_container,newFreagment).commit();
                    break;
                case 1:
                    newFreagment.setmImageId(AndroidImageAssets.getBodies());
                    newFreagment.setmListIndex(listIndex);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.body_container,newFreagment).commit();
                    break;
                case 2:
                    newFreagment.setmImageId(AndroidImageAssets.getLegs());
                    newFreagment.setmListIndex(listIndex);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.leg_container,newFreagment).commit();
                    break;
                default:
                    break;
            }
        }else {

            switch (bodyPartNumber) {
                case 0:
                    mHeadIndex = listIndex;
                    break;
                case 1:
                    mBodyInde = listIndex;
                    break;
                case 2:
                    mLegIndex = listIndex;
                    break;
                default:
                    break;
            }

            Bundle b = new Bundle();
            b.putInt("headindex", mHeadIndex);
            b.putInt("bodyindex", mBodyInde);
            b.putInt("legindex", mLegIndex);

            final Intent intent = new Intent(this, AndroidMeActivity.class);
            intent.putExtras(b);

            Button button = findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent);
                }
            });
        }
    }
}
