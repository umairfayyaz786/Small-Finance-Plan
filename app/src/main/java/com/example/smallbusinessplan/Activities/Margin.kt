package com.example.smallbusinessplan.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.example.ads.Classes.Strategies.bannerAds
import com.example.smallbusinessplan.Extensions.Calculator
import com.example.smallbusinessplan.Extensions.gone
import com.example.smallbusinessplan.Extensions.visible
import com.example.smallbusinessplan.R
import com.example.smallbusinessplan.SharedPref
import com.example.smallbusinessplan.Utils.AppConstants
import com.example.smallbusinessplan.Utils.NetworkUtils
import com.google.android.material.bottomsheet.BottomSheetDialog

class Margin : AppCompatActivity() {
    private lateinit var bannerAd:FrameLayout
    private var MainMenu: Menu? = null
    private lateinit var sharedPref: SharedPref

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_margin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title= Html.fromHtml("<small>MARGIN</small>")
        sharedPref = SharedPref(this)

        bannerAd=findViewById(R.id.MarginBannerAd)
        if (NetworkUtils.isNetworkAvailable(this)) {
            bannerAd.visible()
            bannerAds(this, bannerAd, "SMALL_BANNER")
        } else {
            bannerAd.gone()
        }

        val sales_Gross_Price: EditText = findViewById(R.id.sales_Gross_Price)
        val tax: EditText = findViewById(R.id.tax)
        tax.setText(sharedPref.getIntegerValue(AppConstants.TEXT_KEY).toString())
        val cost_price: EditText = findViewById(R.id.cost_price)
        val Result: Button = findViewById(R.id.calculate)
        val SalesNetPrice_result: TextView = findViewById(R.id.SalesNetPrice_result)
        val Gross_profit_result: TextView = findViewById(R.id.Gross_profit_result)
        val Markup_percent_result: TextView = findViewById(R.id.Markup_percent_result)
        val Margin_percent_result: TextView = findViewById(R.id.Margin_percent_result)

        Result.setOnClickListener {
            if (sales_Gross_Price.text.toString().isEmpty() || sales_Gross_Price.text.toString().isBlank()){
                Toast.makeText(this, "Sales Gross Price must be required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (tax.text.toString().isEmpty() || tax.text.toString().isBlank()){
                Toast.makeText(this, "GST/VST % must be required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (cost_price.text.toString().isEmpty() || cost_price.text.toString().isBlank()){
                Toast.makeText(this, "Cost Price must be required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val sales_Gross_Pricee = sales_Gross_Price.text.toString().toDouble()
            val taxx = tax.text.toString().toDouble()
            val cost_pricee = cost_price.text.toString().toDouble()
            sharedPref.setIntegerValue(AppConstants.TEXT_KEY, cost_pricee)

            val salesnetprice = (sales_Gross_Pricee - (sales_Gross_Pricee - (sales_Gross_Pricee / ((1 + (taxx) / 100)))))
            SalesNetPrice_result.text = salesnetprice.toString()
            val grossprofit = (salesnetprice - cost_pricee)
            Gross_profit_result.text = grossprofit.toString()
            val markup = (((salesnetprice - cost_pricee) / cost_pricee) * 100)
            Markup_percent_result.text = markup.toString()
            val margin = (((salesnetprice - cost_pricee) / salesnetprice) * 100)
            Margin_percent_result.text = margin.toString()
        }
        val formulaBtn:Button = findViewById(R.id.formula)
        formulaBtn.setOnClickListener {
            val i = Intent(this , FormulasActivity::class.java)
            startActivity(i)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            val i = Intent(this , Calculator::class.java)
            startActivity(i)
        }
        if (item.itemId == R.id.ReviewsAction){
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.kachariya.smallbusinessplan")
                )
            )
        }
        if (item.itemId == R.id.QuestionAction) {
            showBottomSheetDialog()
        }
        return super.onOptionsItemSelected(item)
    }
    fun showBottomSheetDialog(){

        val bottomSheetDialog = BottomSheetDialog(this)
        val  viewLayout= LayoutInflater.from(this).inflate(R.layout.bottom_sheet,null)
        bottomSheetDialog.setContentView(viewLayout)
        bottomSheetDialog.show()
        val close:TextView = viewLayout.findViewById(R.id.close_bottom_sheet)
        close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MainMenu = menu
        menuInflater.inflate(R.menu.calculator_activities_items, menu)
        return super.onCreateOptionsMenu(menu)

    }
    override fun onBackPressed() {
        val intent = Intent(this , Calculator::class.java)
        startActivity(intent)
    }
}