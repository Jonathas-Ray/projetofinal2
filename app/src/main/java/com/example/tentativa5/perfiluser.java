package com.example.tentativa5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class perfiluser extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView nv_side;
    ImageView btn_usuario;
    ImageView btn_historico;
    ImageView btn_adicionar;
    TextView tv_snv;
    ActionBarDrawerToggle toggle;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.perfiluser);

        // Toolbar e ajuste do padding para status bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            Insets statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            // Ajusta só padding top para evitar sobrepor a status bar
            v.setPadding(v.getPaddingLeft(), statusBarInsets.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        // Componentes principais
        drawerLayout = findViewById(R.id.main);
        nv_side = findViewById(R.id.nv_side);
        btn_usuario = findViewById(R.id.btn_usuario);
        btn_historico = findViewById(R.id.btn_historico);
        btn_adicionar = findViewById(R.id.btn_adicionar);
        tv_snv = findViewById(R.id.tv_snv);
        mAuth = FirebaseAuth.getInstance();


        // Configura ActionBarDrawerToggle (hambúrguer)
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navegation_drawer_open, R.string.navegation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Personaliza ícone do toggle (hambúrguer)
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black, getTheme()));
        toggle.getDrawerArrowDrawable().setBarLength(80f);
        toggle.getDrawerArrowDrawable().setBarThickness(10f);
        toggle.getDrawerArrowDrawable().setGapSize(12f);

        // Ações do menu lateral
        nv_side.setNavigationItemSelectedListener(item -> {
            tv_snv.setText(item.getTitle());
            if (item.getItemId() == R.id.snv_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(perfiluser.this, tLogin.class);
                startActivity(intent);
                finish();
//                finishAffinity();
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // Botão usuário: Exemplo de clique
        btn_usuario.setOnClickListener(v ->
                Toast.makeText(this, "Usuário clicado!", Toast.LENGTH_SHORT).show()
        );
        btn_historico.setOnClickListener(v ->
                Toast.makeText(this, "Historico clicado!", Toast.LENGTH_SHORT).show()
        );

            btn_adicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupadicionar(view);
//                  Toast.makeText(this, "Acicionar clicado!", Toast.LENGTH_SHORT).show()

                }
            });


        // Controle botão voltar para fechar drawer antes de sair
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(androidx.core.view.GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                } else {
                    finishAffinity();
                }
            }
        });
//        // sair ao clicar
//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signOut();
//
//                     Intent intent = new Intent(perfiluser.this, tLogin.class);
//                startActivity(intent);
//                finish();
//       }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void popupadicionar(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setElevation(5);

        // Mede a altura do conteúdo
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = popupView.getMeasuredHeight();

        // Pega a posição do botão na tela
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int anchorX = location[0];
        int anchorY = location[1];

        // Mostra o popup acima do botão
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                anchorX, anchorY - popupHeight - 37);


        // Clique nos itens
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
        //ao clicar aqui, abrir a funçao de digitar os valores a mão grande
        popupView.findViewById(R.id.texto).setOnClickListener(v -> {
            Intent intent = new Intent(perfiluser.this, adicionarvalores.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Clicou no 4", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
    }
}
