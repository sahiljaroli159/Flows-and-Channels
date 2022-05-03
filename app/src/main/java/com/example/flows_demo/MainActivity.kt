package com.example.flows_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.flows_demo.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel=ViewModelProvider(this).get(MainViewModel::class.java)

        val flow = flow<String> {
            for(i in 1..10){
                emit("Hello World")
                delay(1000L)
            }
        }
        lifecycleScope.launch {
            flow.buffer().collect{
                println(it)
                delay(2000L)
            }
        }





        binding.btnSnackbar.setOnClickListener {
            viewModel.triggerEvent()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect{event->
                when(event){
                    is MainViewModel.MyEvent.ErrorEvent -> {
                        Snackbar.make(binding.root,event.message,Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
}
