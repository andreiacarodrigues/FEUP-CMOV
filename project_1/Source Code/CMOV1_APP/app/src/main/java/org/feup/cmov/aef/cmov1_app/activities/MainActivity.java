package org.feup.cmov.aef.cmov1_app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.feup.cmov.aef.cmov1_app.Constants;
import org.feup.cmov.aef.cmov1_app.R;
import org.feup.cmov.aef.cmov1_app.Utils;
import org.feup.cmov.aef.cmov1_app.entitites.User;
import org.feup.cmov.aef.cmov1_app.tasks.RegisterUserTask;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements RegisterUserTask.ICallback
{
    EditText usernameEditText;
    EditText passwordEditText;
    EditText nameEditText;
    EditText nifEditText;
    TextInputLayout nifTextInputLayout;
    EditText creditCardNumberEditText;
    TextInputLayout creditCardNumberTextInputLayout;

    // Credit Card Type
    Spinner creditCardTypeSpinner;
    boolean creditCardTypeSpinnerTouched = false;

    // Credit Card Validity Month
    Spinner creditCardExpMonthSpinner;
    boolean creditCardExpMonthSpinnerTouched = false;

    // Credit Card Validity Year
    Spinner creditCardExpYearSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        usernameEditText = findViewById(R.id.register_username);
        passwordEditText = findViewById(R.id.register_password);
        passwordEditText.setTransformationMethod(new PasswordTransformationMethod()); // To fix weird behavior with password input type on XML
        nameEditText = findViewById(R.id.register_name);
        nifEditText = findViewById(R.id.register_nif);
        nifTextInputLayout = findViewById(R.id.register_nif_layout);
        creditCardNumberEditText = findViewById(R.id.register_card_number);
        creditCardNumberTextInputLayout = findViewById(R.id.register_card_number_layout);
        creditCardTypeSpinner = findViewById(R.id.register_card_type);
        creditCardExpMonthSpinner = findViewById(R.id.register_exp_month);
        creditCardExpYearSpinner = findViewById(R.id.register_exp_year);

        // Adapters
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.card_types, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        creditCardTypeSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.months, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        creditCardExpMonthSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.years, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        creditCardExpYearSpinner.setAdapter(adapter);

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(this::onRegisterButton);

        nifEditText.setOnFocusChangeListener(this::nifOnFocusChange);

        creditCardNumberEditText.setOnEditorActionListener(this::creditCardNumberOnEditorAction);
        creditCardNumberEditText.setOnFocusChangeListener(this::creditCardNumberOnFocusChange);

        creditCardTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(!creditCardTypeSpinnerTouched)
                {
                    creditCardTypeSpinnerTouched = true;
                }else
                {
                    creditCardExpMonthSpinner.requestFocus();
                    creditCardExpMonthSpinner.performClick();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        creditCardExpMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(!creditCardExpMonthSpinnerTouched)
                {
                    creditCardExpMonthSpinnerTouched = true;
                }else
                {
                    creditCardExpYearSpinner.requestFocus();
                    creditCardExpYearSpinner.performClick();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        creditCardExpYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        generateAndStoreKeys();
    }

    public void creditCardNumberOnFocusChange(View v, boolean hasFocus)
    {
        if(!hasFocus)
        {
            if(!Utils.luhnAlgorithm(creditCardNumberEditText.getText().toString()))
            {
                creditCardNumberTextInputLayout.setError("Invalid number.");
            }else
            {
                creditCardNumberTextInputLayout.setErrorEnabled(false);
            }
        }
    }

    public void nifOnFocusChange(View v, boolean hasFocus)
    {
        if(!hasFocus)
        {
            if(!Utils.nifValidation(nifEditText.getText().toString()))
            {
                nifTextInputLayout.setError("Invalid number.");
            }else
            {
                nifTextInputLayout.setErrorEnabled(false);
            }
        }
    }

    private boolean creditCardNumberOnEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if(actionId == EditorInfo.IME_ACTION_NEXT)
        {
            hideKeyboard();
            creditCardNumberEditText.clearFocus();
            creditCardTypeSpinner.requestFocus();
            creditCardTypeSpinner.performClick();
        }
        return true;
    }

    private void hideKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void onRegisterButton(View v)
    {
        Log.i("OnRegisterButton", "Grabbing values...");
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String nif = nifEditText.getText().toString();
        String creditCardNumber = creditCardNumberEditText.getText().toString();
        String creditCardType = creditCardTypeSpinner.getSelectedItem().toString();
        String creditCardExpirationMonth = creditCardExpMonthSpinner.getSelectedItem().toString();
        String creditCardExpirationYear = creditCardExpYearSpinner.getSelectedItem().toString();

        if(username.isEmpty() || password.isEmpty() || name.isEmpty() || nif.isEmpty() || creditCardNumber.isEmpty() ||
                creditCardType.isEmpty() || creditCardExpirationMonth.isEmpty() || creditCardExpirationYear.isEmpty())
        {

            Toast.makeText(v.getContext(),"Empty field! All the fields in the registration form are required.", Toast.LENGTH_LONG).show();
            return;
        }

        int year = Integer.parseInt(creditCardExpYearSpinner.getSelectedItem().toString());
        int month = Integer.parseInt(creditCardExpMonthSpinner.getSelectedItem().toString());
        GregorianCalendar creditCardDate = new GregorianCalendar(year, month, 1);
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(Calendar.MONTH, 1);

        String error = "";
        if(creditCardDate.before(currentDate))
        {
            error += "Please select a valid date.";
        }

        if(!Utils.luhnAlgorithm(creditCardNumber))
        {
            if(!error.isEmpty())
                error += "\n";
            error += "Please enter a valid credit card number.";
        }

        if(!Utils.nifValidation(nif))
        {
            if(!error.isEmpty())
                error += "\n";
            error += "Please enter a valid nif number.";
        }

        if(!error.isEmpty())
        {
            Toast.makeText(v.getContext(),error, Toast.LENGTH_LONG).show();
            return;
        }

        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

        Log.i("OnRegisterButton", "Creating user...");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setNIF(nif);
        user.setCreditCardNumber(creditCardNumber);
        user.setCreditCardType(creditCardType);
        user.setCreditCardExpiration(creditCardExpirationYear + "-" + creditCardExpirationMonth);
        RSAPublicKey publicKey = Utils.getPublicKey();
        user.setPublicKeyModulus(Utils.bytesToHex(publicKey.getModulus().toByteArray()));
        user.setPublicKeyExponent(Utils.bytesToHex(publicKey.getPublicExponent().toByteArray()));
        Log.i("OnRegisterButton", "User created! User:\n" + user);

        Log.i("OnRegisterButton", "Initializing thread...");
        new RegisterUserTask(this).execute(user);

        Log.i("OnRegisterButton", "Done!");
    }

    private void generateAndStoreKeys()
    {
        try
        {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(Constants.KEYNAME, null);

            if(entry == null)
            {
                Calendar start = new GregorianCalendar();
                Calendar end = new GregorianCalendar();
                end.add(Calendar.YEAR, 20);

                KeyPairGenerator kgen = KeyPairGenerator.getInstance(Constants.KEY_ALGO, Constants.ANDROID_KEYSTORE);
                AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(this)
                        .setKeySize(Constants.KEY_SIZE)
                        .setAlias(Constants.KEYNAME)
                        .setSubject(new X500Principal("CN=" + Constants.KEYNAME))
                        .setSerialNumber(BigInteger.valueOf(12121212))
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                kgen.initialize(spec);
                KeyPair kp = kgen.generateKeyPair();
            }
        }
        catch(Exception ex)
        {

        }
    }

    @Override
    public void onPostRegisterUserTask(RegisterUserTask.RegisterUserTaskResult result)
    {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        switch(result)
        {
            case Success:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(this,"Registered successfully!", Toast.LENGTH_LONG).show();
                break;
            case UserWithTheSameUsernameAlreadyExists:
                Toast.makeText(this,"Username already exists, please choose another one.", Toast.LENGTH_LONG).show();
                break;
            case UnknownError:
                Toast.makeText(this,"An unknown error has occured, please try again.", Toast.LENGTH_LONG).show();
                break;
        }
    }
}