package com.example.rdhomeworkl23testapp

import androidx.activity.viewModels
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

class MainActivity: AppCompatActivity() {

    private val viewModel: MyViewModel by viewModels {
        MyViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val resultText: TextView = findViewById(R.id.main_tv)
        val resultBtn: Button = findViewById(R.id.result_btn)

        resultBtn.setOnClickListener {
            viewModel.getData()
        }
        viewModel.uiState.observe(this){uiState->
            when(uiState) {
                is MyViewModel.UIState.Empty -> Unit
                is MyViewModel.UIState.Result -> {
                    resultText.text = uiState.title
                }
                is MyViewModel.UIState.Processing -> resultText.text = "Processing"
                is MyViewModel.UIState.Error ->{
                    resultText.text = ""
                    Toast.makeText(this,uiState.description, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}