package com.example.assignment1;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Table extends AppCompatActivity {
    Toolbar toolbar1;

    Button button1,button2,button3,button4,button5;
    SeekBar seekBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_layout);

        toolbar1 = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);

        seekBar = findViewById(R.id.seebar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int chance  = 0 ;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                chance = progress ;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println(chance);
                if(chance % 2 == 0 ) {
                    button1.setBackgroundTintList(ContextCompat.getColorStateList(Table.this,R.color.orange));
                    button2.setBackgroundTintList(ContextCompat.getColorStateList(Table.this,R.color.teal_200));
                    button3.setBackgroundTintList(ContextCompat.getColorStateList(Table.this,R.color.teal_700));
                    button4.setBackgroundTintList(ContextCompat.getColorStateList(Table.this,R.color.white));
                    button5.setBackgroundTintList(ContextCompat.getColorStateList(Table.this,R.color.purple_500));
                }
                else {
                    button1.setBackgroundTintList(ContextCompat.getColorStateList(Table.this,R.color.red));
                    button2.setBackgroundTintList(ContextCompat.getColorStateList(Table.this,R.color.blue));
                    button3.setBackgroundTintList(ContextCompat.getColorStateList(Table.this,R.color.yellow));
                    button4.setBackgroundTintList(ContextCompat.getColorStateList(Table.this,R.color.orange));
                    button5.setBackgroundTintList(ContextCompat.getColorStateList(Table.this,R.color.teal_200));
                }
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
                Toast toast = Toast.makeText(Table.this,"Cảm ơn đã sử dụng ứng dụng!", Toast.LENGTH_SHORT);
                toast.show();
                dialog.cancel();
            }
        });

        dialog.show();
    }
}