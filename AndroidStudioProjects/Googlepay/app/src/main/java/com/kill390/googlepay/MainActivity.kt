package com.kill390.googlepay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.util.CollectionUtils
import com.google.android.gms.wallet.*
import com.google.android.gms.wallet.Wallet.WalletOptions
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var button: RelativeLayout? = null
    var paymentsClient: PaymentsClient? = null

    private val baseCardPaymentMethod = JSONObject().apply {
        put("type", "CARD")
        put("parameters", JSONObject().apply {
            put("allowedCardNetworks", JSONArray(listOf("VISA", "MASTERCARD")))
            put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
        })
    }

    private val googlePayBaseConfiguration = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
        put("allowedPaymentMethods",  JSONArray().put(baseCardPaymentMethod))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        paymentsClient = createPaymentsClient(this)
        button = findViewById(R.id.googlePayButton)




            val readyToPayRequest =
                    IsReadyToPayRequest.fromJson(googlePayBaseConfiguration.toString())

            val readyToPayTask = paymentsClient!!.isReadyToPay(readyToPayRequest)
            readyToPayTask.addOnCompleteListener { task ->
                try {
                    task.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
                } catch (exception: ApiException) {
                    // Error determining readiness to use Google Pay.
                    // Inspect the logs for more details.
                }
            }


    }

    fun createPaymentsClient(activity: Activity?): PaymentsClient {
        val walletOptions = WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
        return Wallet.getPaymentsClient(activity!!, walletOptions)
    }
    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            button?.visibility = View.VISIBLE
            button?.setOnClickListener { requestPayment() }
        } else {
            // Unable to pay using Google Pay. Update your UI accordingly.
        }
    }

    private fun requestPayment() {
        // TODO: Perform transaction
        val paymentDataRequest =
                PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        AutoResolveHelper.resolveTask(
                paymentsClient!!.loadPaymentData(paymentDataRequest),
                this, 0)

    }
    private val tokenizationSpecification = JSONObject().apply {
        put("type", "PAYMENT_GATEWAY")
        put("parameters", JSONObject(mapOf(
                "gateway" to "example",
                "gatewayMerchantId" to "BCR2DN6T7PSZVIAK")))
    }
    private val cardPaymentMethod = JSONObject().apply {
        put("type", "CARD")
        put("tokenizationSpecification", tokenizationSpecification)
        put("parameters", JSONObject().apply {
            put("allowedCardNetworks", JSONArray(listOf("VISA", "MASTERCARD")))
            put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
            put("billingAddressRequired", true)
            put("billingAddressParameters", JSONObject(mapOf("format" to "FULL")))
        })
    }
    private val transactionInfo = JSONObject().apply {
        put("totalPrice", "1.99")
        put("totalPriceStatus", "FINAL")
        put("currencyCode", "USD")
    }
    private val merchantInfo = JSONObject().apply {
        put("merchantName", "Example Merchant")
        put("merchantId", "BCR2DN6T7PSZVIAK")
    }
    private val paymentDataRequestJson = JSONObject(googlePayBaseConfiguration.toString()).apply {
        put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod))
        put("transactionInfo", transactionInfo)
        put("merchantInfo", merchantInfo)
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        PaymentData.getFromIntent(data!!)?.let(::handlePaymentSuccess)
                        Log.i("RESULT_OK", "RESULT_OK")
                    }
                    Activity.RESULT_CANCELED -> {
                        // The user cancelled without selecting a payment method.
                        Log.i("RESULT_CANCELED","RESULT_CANCELED")

                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                         Log.i("errer","error")
                        }
                    }
                }
            }
        }
    }
    private fun handlePaymentSuccess(paymentData: PaymentData) {

        Log.i("handlePaymentSuccess" , "here")
        // Sample TODO: Use this token to perform a payment through your payment gateway
    }
}