package com.example.mediahider

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediahider.databinding.ActivityEnterPinBinding
import com.example.mediahider.utils.AppThemePrefrences.GetSharedPreference
import com.example.mediahider.utils.PASSWORD_KEY

class EnterPin : AppCompatActivity() {

    private lateinit var binding:ActivityEnterPinBinding
    private lateinit var pinFields: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEnterPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pinFields = listOf(binding.pin1, binding.pin2, binding.pin3, binding.pin4)

        setupPinFieldFocus(pinFields)

        binding.continueBtn.setOnClickListener {
            val pin = pinFields.joinToString("") { it.text.toString() }
            val  password = GetSharedPreference(this, PASSWORD_KEY)

            if(pin.equals(password)){
                val intent = Intent(
                    this,
                    HomeScreen::class.java
                )
                startActivity(intent)

                finish()
            }
            else{
                Toast.makeText(this, "Wrong PIN", Toast.LENGTH_SHORT).show()

            }

        }


    }

    private fun setupPinFieldFocus(fields: List<EditText>) {
        for (i in fields.indices) {
            fields[i].apply {
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                filters = arrayOf(InputFilter.LengthFilter(1))

                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (!s.isNullOrEmpty()) {
                            if (i < fields.size - 1) fields[i + 1].requestFocus()
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })

                setOnKeyListener { _, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && text.isEmpty() && i > 0) {
                        fields[i - 1].apply {
                            requestFocus()
                            setSelection(length())
                        }
                        true
                    } else {
                        false
                    }
                }
            }
        }
    }


}