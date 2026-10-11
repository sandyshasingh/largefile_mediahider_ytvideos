package com.example.mediahider

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediahider.databinding.ActivitySetPinBinding
import com.example.mediahider.utils.AppThemePrefrences.SetSharedPreference
import com.example.mediahider.utils.PASSWORD_KEY
import com.google.android.material.button.MaterialButton

class SetPinActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetPinBinding
    private lateinit var pinFields: List<EditText>
    private lateinit var confirmFields: List<EditText>
    private lateinit var successMessage: TextView
    private lateinit var btnSetPin: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySetPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pinFields = listOf(binding.pin1, binding.pin2, binding.pin3, binding.pin4)
        confirmFields = listOf(binding.confirmPin1, binding.confirmPin2, binding.confirmPin3, binding.confirmPin4)
        successMessage = findViewById(R.id.successMessage)
        btnSetPin = findViewById(R.id.btnSetPin)

        successMessage.visibility = View.GONE
        btnSetPin.isEnabled = false

        setupPinFieldFocus(pinFields)
        setupPinFieldFocus(confirmFields)

        confirmFields.forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    validatePins()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        btnSetPin.setOnClickListener {
            val pin = pinFields.joinToString("") { it.text.toString() }
            Toast.makeText(this, "PIN set: $pin", Toast.LENGTH_SHORT).show()
            SetSharedPreference(this, PASSWORD_KEY,pin)
            val intent = Intent(
                this,
                HomeScreen::class.java
            )
            startActivity(intent)

            finish()

            // Save securely or move to next screen
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

    private fun validatePins() {
        val pin = pinFields.joinToString("") { it.text.toString() }
        val confirmPin = confirmFields.joinToString("") { it.text.toString() }

        if (pin.length == 4 && confirmPin.length == 4) {
            if (pin == confirmPin) {
                successMessage.text = "Your PIN codes are the same ✓"
                successMessage.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
                successMessage.visibility = View.VISIBLE
                btnSetPin.isEnabled = true
            } else {
                successMessage.text = "PIN codes do not match ✕"
                successMessage.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                successMessage.visibility = View.VISIBLE
                btnSetPin.isEnabled = false
            }
        } else {
            successMessage.visibility = View.GONE
            btnSetPin.isEnabled = false
        }
    }
}