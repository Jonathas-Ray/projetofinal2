package com.example.tentativa5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class perfiluser extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView nv_side;
    ImageView btn_usuario, btn_historico, btn_adicionar;
    TextView tv_snv;
    FirebaseAuth mAuth;

    private long tempoUltimoBackPress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.perfiluser);

        inicializarViews();
        configurarToolbar();
        configurarDrawer();
        configurarBotoes();
        configurarBackButton();
    }

    private void inicializarViews() {
        drawerLayout = findViewById(R.id.main);
        nv_side = findViewById(R.id.nv_side);
        btn_usuario = findViewById(R.id.btn_usuario);
        btn_historico = findViewById(R.id.btn_historico);
        btn_adicionar = findViewById(R.id.btn_adicionar);
        tv_snv = findViewById(R.id.tv_snv);
        mAuth = FirebaseAuth.getInstance();
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            Insets statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), statusBarInsets.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
    }

    private void configurarDrawer() {
        nv_side.setNavigationItemSelectedListener(item -> {
            tv_snv.setText(item.getTitle());
            if (item.getItemId() == R.id.snv_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(perfiluser.this, tLogin.class);
                startActivity(intent);
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void configurarBotoes() {
        btn_usuario.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        btn_historico.setOnClickListener(v ->
                Toast.makeText(this, "Histórico clicado!", Toast.LENGTH_SHORT).show()
        );

        btn_adicionar.setOnClickListener(this::popupadicionar);
    }

    private void configurarBackButton() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                } else {
                    long tempoAtual = System.currentTimeMillis();
                    if (tempoAtual - tempoUltimoBackPress < 2000) {
                        finishAffinity(); // Sai do app
                    } else {
                        Toast.makeText(perfiluser.this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();
                        tempoUltimoBackPress = tempoAtual;
                    }
                }
            }
        });
    }

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
            Intent intent = new Intent(perfiluser.this, adicionarvalores.class);
            intent.putExtra("botao_selecionado", "texto");
            startActivity(intent);
            popupWindow.dismiss();
        });
    }
}
