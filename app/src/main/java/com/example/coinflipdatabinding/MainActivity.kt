package com.example.coinflipdatabinding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.example.coinflipdatabinding.databinding.ActivityMainBinding
import kotlin.math.round

class MainActivity : AppCompatActivity() {
//    Initialize dataBinding object
    private lateinit var binding: ActivityMainBinding

    private var heads = 0
    private var tails = 0
    private var total = 0

    var coinStatus = "We are binding our data to the layout!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        The DataBinding way
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

//        set click Listeners
        binding.mainActivitySwSimulate.setOnCheckedChangeListener{ buttonView, isChecked -> enableSim(isChecked) }
        binding.mainActivityBtFlip.setOnClickListener { flip() }
        binding.mainActivityBtReset.setOnClickListener { reset() }
        binding.mainActivityBtSimulate.setOnClickListener { sim() }

//      test dataBinding into the layout
        binding.myActivity = this
    }

    private fun enableSim(onState: Boolean){
        if (onState){
            binding.mainActivityEtSimNumber.visibility = View.VISIBLE
            binding.mainActivityBtSimulate.visibility = View.VISIBLE
        } else {
            binding.mainActivityEtSimNumber.visibility = View.INVISIBLE
            binding.mainActivityBtSimulate.visibility = View.INVISIBLE
        }
    }

    //        Simulate a single coin flip
    private fun flip(){
        val randomNumber = (0..1).random()
//            update based on value
        if (randomNumber == 0) {
            update("heads")
        }
        else {
            update("tails")
        }
    }

    private fun update(coinValue: String){
        if (coinValue == "heads"){
            heads++
            binding.mainActivityIvIcon.setImageResource(R.drawable.ic_heads_icon)
            coinStatus = "You flipped a head!"
        }
        else{
            tails++
            binding.mainActivityIvIcon.setImageResource(R.drawable.ic_tails_icon)
            coinStatus = "You flipped a tail!"
        }

//        invalidate all binding expression
        binding.apply { invalidateAll() }

        total++

        binding.mainActivityTvTotalFlips.text = "Total Flips: $total"
        binding.activityMainTvTotalHeads.text = "Total Heads: $heads"
        binding.mainActivityTvTotalTails.text = "Total Tails: $tails"

        updateStatistics()
    }

    private fun updateStatistics(){
        var headsPercentResult = 0.0
        var tailsPercentResult = 0.0

        if(total != 0) {
            headsPercentResult = round((heads.toDouble() / total) * 10000) /100
            tailsPercentResult = round((tails.toDouble() / total) * 10000) /100
        }

        binding.mainActivityTvHeadsPercent.text = "Heads $headsPercentResult %"
        binding.mainActivityTvTailsPercent.text = "Tails $tailsPercentResult %"

        binding.mainActivityPbHeadsPercent.progress = headsPercentResult.toInt()
        binding.mainActivityPbTailsPercent.progress = tailsPercentResult.toInt()

    }

    private fun reset(){
        total = 0
        heads = 0
        tails = 0

        binding.mainActivityIvIcon.setImageResource(R.drawable.ic_flip_icon)
        binding.mainActivityTvTotalFlips.text = "Total Flips: $total"
        binding.activityMainTvTotalHeads.text = "Total Heads: $heads"
        binding.mainActivityTvTotalTails.text = "Total Tails: $tails"

        coinStatus = "Press to flip!"
        binding.apply { invalidateAll() }

        updateStatistics()

    }

    private fun sim(){
        var numToSim = 1
        if (binding.mainActivityEtSimNumber.text.toString() != ""){
            numToSim = binding.mainActivityEtSimNumber.text.toString().toInt()
        }
        binding.mainActivityEtSimNumber.setText("")

        for(i in 1..numToSim){
            flip()
        }

        hideKeyboard()
    }

    private fun hideKeyboard(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.mainActivityIvIcon.windowToken, 0)
    }
}