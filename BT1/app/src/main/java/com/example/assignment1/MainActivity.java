package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar1 ;

    Button linear , relative , table ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar1 = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);

        linear = findViewById(R.id.layout_linear);
        relative = findViewById(R.id.layout_relative);
        table = findViewById(R.id.layout_table);

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Linear.class);
                MainActivity.this.startActivity(intent);
            }
        });

        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Relative.class);
                MainActivity.this.startActivity(intent);
            }
        });

        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Table.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar1) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucustom,menu);

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.more:
                dialog();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return false ;
    }

    public void dialog (){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog);


        Button cancelBt = dialog.findViewById(R.id.cancel);
        Button okBT = dialog.findViewById(R.id.ok);

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        okBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this,"Cảm ơn đã sử dụng ứng dụng!", Toast.LENGTH_SHORT);
                toast.show();
                dialog.cancel();
            }
        });

        dialog.show();
    }
}