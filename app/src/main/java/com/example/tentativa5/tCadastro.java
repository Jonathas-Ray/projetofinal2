package com.example.tentativa5;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class tCadastro extends AppCompatActivity {

    private EditText edt_emailregistrar;
    private EditText edt_senharegistrar;
    private EditText edt_confSenhaEditText;
    private AppCompatButton edt_buttonCadastrar;
    private ImageButton edt_buttonGoogle;
    private ProgressBar progressBarLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tcadastro);

        // Inicializações
        edt_emailregistrar = findViewById(R.id.email);
        edt_senharegistrar = findViewById(R.id.senha);
        edt_confSenhaEditText = findViewById(R.id.confsenha);
        edt_buttonCadastrar = findViewById(R.id.buttonid);
        // edt_buttonGoogle = findViewById(R.id.btnLgoogle); // Descomentável se necessário
        progressBarLogin = findViewById(R.id.progessbar);
        mAuth = FirebaseAuth.getInstance();

        // Ajuste para barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Funções visuais
        habilitarToggleSenha(edt_senharegistrar);
        habilitarToggleSenha(edt_confSenhaEditText);

        // Clique no botão de cadastro
        edt_buttonCadastrar.setOnClickListener(v -> {
            String cadastroemail = edt_emailregistrar.getText().toString().trim();
            String cadastrosenha = edt_senharegistrar.getText().toString().trim();
            String confirmaSenha = edt_confSenhaEditText.getText().toString().trim();

            animacaoBounce(edt_buttonCadastrar);

            edt_buttonCadastrar.postDelayed(() -> {
                if (!TextUtils.isEmpty(cadastroemail) &&
                        !TextUtils.isEmpty(cadastrosenha) &&
                        !TextUtils.isEmpty(confirmaSenha)) {

                    if (cadastrosenha.equals(confirmaSenha)) {
                        progressBarLogin.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(cadastroemail, cadastrosenha)
                                .addOnCompleteListener(task -> {
                                    progressBarLogin.setVisibility(View.INVISIBLE);

                                    if (task.isSuccessful()) {
                                        abrirTelaPrincipal();
                                        Toast.makeText(tCadastro.this, "Cadastro feito com sucesso!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Tratamento de erros específicos do Firebase
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException e) {
                                            Toast.makeText(tCadastro.this, "Use pelo menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            Toast.makeText(tCadastro.this, "E-mail inválido.", Toast.LENGTH_SHORT).show();
                                        } catch (FirebaseAuthUserCollisionException e) {
                                            Toast.makeText(tCadastro.this, "Este e-mail já está cadastrado.", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Toast.makeText(tCadastro.this, "Erro ao cadastrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(tCadastro.this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(tCadastro.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }, 100);
        });
    }

    /**
     * Animação com bounce ao clicar no botão.
     */
    private void animacaoBounce(AppCompatButton botao) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(botao, "scaleX", 1.0f, 1.2f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(botao, "scaleY", 1.0f, 1.2f, 1.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(100);
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.start();
    }

    /**
     * Mostra ou oculta a senha ao tocar no ícone "olhinho".
     */
    @SuppressLint("ClickableViewAccessibility")
    private void habilitarToggleSenha(EditText campoSenha) {
        campoSenha.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (campoSenha.getRight() - campoSenha.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (campoSenha.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        campoSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        campoSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                    campoSenha.setSelection(campoSenha.getText().length());
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * Redireciona para a tela de login após o cadastro.
     */
    private void abrirTelaPrincipal() {
        Intent intent = new Intent(tCadastro.this, tLogin.class);
        startActivity(intent);
        finish();
    }
}
