package ctrl_alt_delight.helpinghands;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static android.content.ContentValues.TAG;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends Activity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mPasswordView;
    private EditText mEmailView;
    RadioGroup radioGroup;
    RadioButton studentRadio;
    RadioButton advisorRadio;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        radioGroup = (RadioGroup) findViewById(R.id.register_radio_group);
        studentRadio = (RadioButton) findViewById(R.id.register_radio_student);
        advisorRadio = (RadioButton) findViewById(R.id.register_radio_advisor);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button registerButton = (Button) findViewById(R.id.email_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.email_register_button || id == EditorInfo.IME_NULL) {
                    createAccount();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = (Button) findViewById(R.id.email_return_sign_in);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void createAccount() {
        final String email = mEmailView.getText().toString().trim();
        final String password = mPasswordView.getText().toString().trim();
        final DatabaseReference myRef = database.getReference(email.split("@")[0]);

        if (!isEmailValid(email)) {
            Toast.makeText(RegisterActivity.this, "Please use a Georgia Tech email ID.",
                    Toast.LENGTH_SHORT).show();
            register();
        } else if (!isPasswordValid(password)) {
            Toast.makeText(RegisterActivity.this, "Please use a password of at least 6 characters.",
                    Toast.LENGTH_SHORT).show();
            register();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Could not register. Please try again",
                                        Toast.LENGTH_SHORT).show();
                                register();
                            } else {
//                                Toast.makeText(RegisterActivity.this, "Registered successfully",
//                                        Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegisterActivity.this, "Registered successfully",
                                        Toast.LENGTH_SHORT).show();
//                                if (radioGroup.getCheckedRadioButtonId() == 1) {
//                                    myRef.setValue("Adviser");
//                                } else {
//                                    myRef.setValue("Student");
//                                }
                                signIn();
                            }
                        }
                    });
        }

    }

    private void signIn() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void register() {
        Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean isEmailValid(String email) {
        //TODO
        return email.contains("@gatech.edu");
    }

    private boolean isPasswordValid(String password) {
        //TODO
        return password.length() > 6;
    }
}

