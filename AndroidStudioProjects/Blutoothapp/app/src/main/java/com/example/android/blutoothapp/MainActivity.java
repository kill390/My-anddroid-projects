package com.example.android.blutoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayList<String> addresses = new ArrayList<String>();
    ArrayAdapter<String> adapter ;

    Button searchButton ;
    TextView textView ;
    ListView list ;
    BluetoothAdapter bluetoothAdapter ;



    final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("here", action);
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                textView.setText("Finished");
                searchButton.setEnabled(true);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String RSSI = String.valueOf(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                if (!addresses.contains(address)) {
                    addresses.add(address);
                    if (name == null || name.equals("")) {
                        arrayList.add("address: " + address + "\nRSSI: " + RSSI);
                    } else {
                        arrayList.add("name: " + name + "\naddress: " + address + "\nRSSI: " + RSSI);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    public void search (View view){
        textView.setText("Searching...");
        searchButton.setEnabled(false);
        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        list = findViewById(R.id.listView);
        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);

        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(broadcastReceiver,intentFilter);
//
//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//
//        if (pairedDevices.size() > 0) {
//            // There are paired devices. Get the name and address of each paired device.
//            for (BluetoothDevice device : pairedDevices) {
//                arrayList.add(device.getName());
//                adapter.notifyDataSetChanged();
//            }
//        }
        list.setAdapter(adapter);
    }
}
