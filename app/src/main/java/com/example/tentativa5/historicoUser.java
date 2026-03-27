package com.example.tentativa5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class historicoUser extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView btn_usuario, btn_adicionar, btn_historico;
    private AppCompatButton btnMostrarDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historico_user);

        configurarInsets();
        inicializarViews();
        configurarBotoes();
    }

    private void configurarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void inicializarViews() {
        drawerLayout = findViewById(R.id.main);
        btn_usuario = findViewById(R.id.btn_usuario);
        btn_adicionar = findViewById(R.id.btn_adicionar);
        btn_historico = findViewById(R.id.btn_historico);
        btnMostrarDados = findViewById(R.id.btn_mostrar_dados);
    }

    private void configurarBotoes() {
        btn_usuario.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        btn_adicionar.setOnClickListener(this::popupAdicionar);

        btn_historico.setOnClickListener(v -> {
            // Já está na tela de histórico
        });

        btnMostrarDados.setOnClickListener(this::abrirPopupComProgress);
    }

    private void popupAdicionar(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

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
//            Intent intent = new Intent(historicoUser.this, camera.class);
//            intent.putExtra("botao_selecionado", "camera");
//            startActivity(intent);
            Toast.makeText(this, "Clicou no 1", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.galeria).setOnClickListener(v -> {
//            Intent intent = new Intent(historicoUser.this, galeria.class);
//            intent.putExtra("botao_selecionado", "galeria");
//            startActivity(intent);
            Toast.makeText(this, "Clicou no 2", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.microfone).setOnClickListener(v -> {
//            Intent intent = new Intent(historicoUser.this, microfone.class);
//            intent.putExtra("botao_selecionado", "microfone");
//            startActivity(intent);
            Toast.makeText(this, "Clicou no 3", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.texto).setOnClickListener(v -> {
            Intent intent = new Intent(historicoUser.this, adicionarvalores.class);
            intent.putExtra("botao_selecionado", "texto");
            startActivity(intent);
            popupWindow.dismiss();
        });
    }

    private void abrirPopupComProgress(View anchorView) {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_historico, null);

        TextView txtDados = popupView.findViewById(R.id.txt_dados);
        ProgressBar progressBar = popupView.findViewById(R.id.progressBar);
        AppCompatButton btnFechar = popupView.findViewById(R.id.btn_fechar);

        progressBar.setVisibility(View.VISIBLE);
        txtDados.setVisibility(View.GONE);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setElevation(10);
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

        btnFechar.setOnClickListener(v -> popupWindow.dismiss());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("dados_enviados");

        ref.limitToLast(9).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                txtDados.setVisibility(View.VISIBLE);

                if (snapshot.exists()) {
                    StringBuilder dadosFormatados = new StringBuilder();

                    for (DataSnapshot child : snapshot.getChildren()) {
                        for (DataSnapshot campo : child.getChildren()) {
                            String chave = campo.getKey();
                            String valor = String.valueOf(campo.getValue());
                            dadosFormatados.append(chave).append(": ").append(valor).append("\n");
                        }
                    }

                    txtDados.setText(dadosFormatados.toString());
                } else {
                    txtDados.setText("Nenhum dado encontrado.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                txtDados.setVisibility(View.VISIBLE);
                txtDados.setText("Erro ao carregar dados.");
            }
        });
    }
}
