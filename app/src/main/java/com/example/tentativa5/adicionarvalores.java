package com.example.tentativa5;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class adicionarvalores extends AppCompatActivity {

    // Declaração de variáveis
    DrawerLayout drawerLayout;
    NavigationView nv_side;
    ImageView btn_historico, btn_adicionar, btn_usuario;
    AppCompatButton btn_enviar;
    EditText campo1, campo2, campo3, campo4, campo5;
    TextView tv_snv;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adicionarvalores);

        // Inicializações de views e Firebase
        inicializarViews();
        configurarToolbar();
        configurarInsets();
        configurarDrawer();
        configurarBotoes();
        configurarBackButton();
    }

    // Inicialização dos componentes visuais
    private void inicializarViews() {
        drawerLayout = findViewById(R.id.main);
        nv_side = findViewById(R.id.nv_side);
        btn_historico = findViewById(R.id.btn_historico);
        btn_adicionar = findViewById(R.id.btn_adicionar);
        btn_usuario = findViewById(R.id.btn_usuario);
        tv_snv = findViewById(R.id.tv_snv);
        mAuth = FirebaseAuth.getInstance();
        btn_enviar = findViewById(R.id.btn_enviar);
        campo1 = findViewById(R.id.campo1);
        campo2 = findViewById(R.id.campo2);
        campo3 = findViewById(R.id.campo3);
        campo4 = findViewById(R.id.campo4);
        campo5 = findViewById(R.id.campo5);
    }

    // Configuração da Toolbar
    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Aplicação de insets de sistema
    private void configurarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(drawerLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Configuração do menu lateral (Navigation Drawer)
    private void configurarDrawer() {
        nv_side.setNavigationItemSelectedListener(item -> {
            tv_snv.setText(item.getTitle());
            if (item.getItemId() == R.id.snv_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, tLogin.class);
                startActivity(intent);
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    // Configuração de todos os botões
    private void configurarBotoes() {
        btn_historico.setOnClickListener(v ->
                Toast.makeText(this, "Historico clicado!", Toast.LENGTH_SHORT).show()
        );

        btn_adicionar.setOnClickListener(this::popupadicionar);

        btn_usuario.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    // Configuração do comportamento do botão "voltar"
    private void configurarBackButton() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                } else {
                    Intent intent = new Intent(adicionarvalores.this, perfiluser.class);
                    startActivity(intent);
                }
            }
        });
    }

    // Pop-up do botão adicionar
    private void popupadicionar(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setElevation(5);

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = popupView.getMeasuredHeight();

        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int anchorX = location[0];
        int anchorY = location[1];

        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                anchorX, anchorY - popupHeight - 37);

        ImageView btnTexto = popupView.findViewById(R.id.texto);
        if (btnTexto != null) {
            btnTexto.setColorFilter(getResources().getColor(android.R.color.holo_blue_light));
        }

        popupView.findViewById(R.id.camera).setOnClickListener(v -> {
            Toast.makeText(this, "Clicou no 1", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.galeria).setOnClickListener(v -> {
            Toast.makeText(this, "Clicou no 2", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.microfone).setOnClickListener(v -> {
            Toast.makeText(this, "Clicou no 3", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.texto).setOnClickListener(v -> {
            Toast.makeText(this, "Você já está aqui", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
    }
}
