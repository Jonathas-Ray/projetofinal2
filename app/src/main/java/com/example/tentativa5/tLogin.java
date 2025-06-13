package com.example.tentativa5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class tLogin extends AppCompatActivity {

    private ProgressBar progessbar;
    private EditText edt_senhaLoguin;
    private EditText edt_emailLogin;
    private AppCompatButton eButton;
    private TextView cadastresse;
    private FirebaseAuth mAuth;

    private void animacaoBounce(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bouce);
        view.startAnimation(animation);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tloguin);

        progessbar = findViewById(R.id.progessbar);
        edt_emailLogin = findViewById(R.id.email);
        edt_senhaLoguin = findViewById(R.id.senha);
        eButton = findViewById(R.id.Ebutton);
        cadastresse = findViewById(R.id.cadastresseclic);
        mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Leva com um clique na escrita para a tela de cadastro
        cadastresse.setOnClickListener(view -> irparacadastro());

        // Exibir ou ocultar a senha com o "olhinho"
        edt_senhaLoguin.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (edt_senhaLoguin.getCompoundDrawables()[DRAWABLE_RIGHT] != null &&
                        event.getRawX() >= (edt_senhaLoguin.getRight()
                                - edt_senhaLoguin.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                    int selection = edt_senhaLoguin.getSelectionEnd();

                    if (edt_senhaLoguin.getInputType() ==
                            (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        edt_senhaLoguin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        edt_senhaLoguin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }

                    edt_senhaLoguin.setSelection(selection); // manter posição do cursor
                    return true;
                }
            }
            return false;
        });


        eButton.setOnClickListener(v -> {
            animacaoBounce(eButton);

            eButton.postDelayed(() -> {
                String loginemail = edt_emailLogin.getText().toString().trim();
                String loguinsenha = edt_senhaLoguin.getText().toString().trim();

                if (!TextUtils.isEmpty(loginemail) && !TextUtils.isEmpty(loguinsenha)) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(loginemail).matches()) {
                        if (loguinsenha.length() >= 6) {
                            progessbar.setVisibility(View.VISIBLE);

                            mAuth.signInWithEmailAndPassword(loginemail, loguinsenha)
                                    .addOnCompleteListener(task -> {
                                        progessbar.setVisibility(View.INVISIBLE);

                                        if (task.isSuccessful()) {
                                            abrirTelaPrincipal();
                                        } else {
                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthInvalidUserException e) {
                                                Toast.makeText(tLogin.this, "Este e-mail não está cadastrado.", Toast.LENGTH_SHORT).show();
                                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                                Toast.makeText(tLogin.this, "Senha incorreta ou e-mail inválido, tente novamente.", Toast.LENGTH_SHORT).show();
                                            } catch (Exception e) {
                                                Toast.makeText(tLogin.this, "Erro no login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(tLogin.this, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(tLogin.this, "E-mail inválido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(tLogin.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }, 100);
        });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(tLogin.this, perfiluser.class);
        startActivity(intent);
        finish();
    }

    private void irparacadastro() {
        Intent intent = new Intent(tLogin.this, tCadastro.class);
        startActivity(intent);
        finish();
    }
}