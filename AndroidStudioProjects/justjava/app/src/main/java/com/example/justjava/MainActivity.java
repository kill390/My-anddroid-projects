/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.justjava;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity =2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */

    public void increment(View view) {
        if (quantity==100){

            Toast toast = Toast.makeText(getApplicationContext(), "quantity cant be more than 100", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        quantity = quantity+1;
        displayQuantity(quantity);


    }
    public void decrement(View view) {
        if (quantity==1){
            Toast toast = Toast.makeText(getApplicationContext(), "quantity cant be less than 1", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        quantity = quantity-1;
        displayQuantity(quantity);
    }
    public void submitOrder(View view) {
        int price = calculatePrice();
       displayMessage(createOrderSummary());
       composeEmail("osamatamer390@gmail.com","cofirm order");
    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private int calculatePrice() {
        int price =5;
        CheckBox os =(CheckBox)findViewById(R.id.check);
        boolean a =os.isChecked();
        CheckBox addChoco =(CheckBox)findViewById(R.id.chocolate);
        boolean addChocoChecked =addChoco.isChecked();
        if (a == true) {
          price=price+1;

        }
         if (addChocoChecked==true){
            price=price+2;
        }

        return quantity*price ;
    }
    public void composeEmail(String addresses, String subject) {
        CheckBox os =(CheckBox)findViewById(R.id.check);
        boolean a =os.isChecked();
       CheckBox addChoco =(CheckBox)findViewById(R.id.chocolate);
        boolean addChocoChecked =addChoco.isChecked();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+addresses)); // only email apps should handle this

        intent.putExtra(Intent.EXTRA_TEXT,createOrderSummary() );
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }



    private String createOrderSummary(){

        CheckBox os =(CheckBox)findViewById(R.id.check);
        boolean a =os.isChecked();


        CheckBox addChoco =(CheckBox)findViewById(R.id.chocolate);
        boolean addChocoChecked =addChoco.isChecked();
        String orderSummary ="Name:"+name();
        orderSummary+="\nAdd Whipped cream?"+a;
        orderSummary+="\nAdd chocolate ? "+addChocoChecked;
        orderSummary+="\nQuantity: " +quantity;
        orderSummary+="\nTotal price: $"+calculatePrice();
        orderSummary+="\nThank you";
        return  orderSummary;
    }

      public String name(){
          EditText text = (EditText)findViewById(R.id.name_enter);
          String value = text.getText().toString();
          return value;
      }



    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text_view);
        priceTextView.setText(message);
    }
}