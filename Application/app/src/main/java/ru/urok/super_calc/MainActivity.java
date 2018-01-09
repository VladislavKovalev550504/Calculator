package ru.urok.super_calc;

import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    Button btOne, btTwo, btThree, btFour, btFive;
    Button btSix, btSeven, btEight, btNine, btZero;
    Button btPlus, btMinus, btMulti, btDiv, btEqual, btClear;
    Button btPoint, btSquare, btRadical, btBack;
    TextView tvLCD;
    SharedPreferences sp;
    Boolean add_button;
    String style_calc;
    String lang_calc;
    LinearLayout llExtra;
    RelativeLayout rlCommon;
    int[] bt_ids;
    Button[] bt_array;
    int len;
    int buttonW, buttonH;
    Typeface CF;
    private Locale locale;
    Boolean flagPoint;
    String operand1, operand2;
    int flagAction;
    double result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        tvLCD = (TextView) findViewById(R.id.tvLCD);
        llExtra = (LinearLayout) findViewById(R.id.llExtra);
        rlCommon = (RelativeLayout) findViewById(R.id.rlCommon);
        bt_ids = new int[]{R.id.btOne, R.id.btTwo, R.id.btThree, R.id.btFour,
                R.id.btFive, R.id.btSix, R.id.btSeven, R.id.btEight,
                R.id.btNine, R.id.btZero, R.id.btPlus, R.id.btMinus,
                R.id.btMulti, R.id.btDiv, R.id.btEqual, R.id.btClear,
                R.id.btPoint, R.id.btSquare, R.id.btRadical, R.id.btBack};
        bt_array = new Button[]{btOne, btTwo, btThree, btFour, btFive, btSix,
                btSeven, btEight, btNine,btZero, btPlus, btMinus, btMulti,
                btDiv, btEqual, btClear, btPoint, btSquare, btRadical, btBack};
        len = bt_array.length;
        for(int i = 0; i < len; i++){
            bt_array[i] = (Button) findViewById(bt_ids[i]);
            bt_array[i].setOnClickListener(this);
        }
        clearVariables();
        showNumber(operand1);
    }

    protected void onResume() {
        add_button = sp.getBoolean("add_button", false);
        style_calc = sp.getString("style_calc", "1");
        lang_calc = sp.getString("lang_calc", "default");
        locale = new Locale(lang_calc);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);
        int flag_visiblity = View.INVISIBLE;
        if(add_button)flag_visiblity = View.VISIBLE;
        llExtra.setVisibility(flag_visiblity);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        buttonW = (metricsB.widthPixels - 32)/4;
        buttonH = (metricsB.heightPixels - 32 - 120)/6;
        LinearLayout.LayoutParams param_tv = (LinearLayout.LayoutParams) tvLCD.getLayoutParams();
        param_tv.height = (int)(buttonH/1.3);
        tvLCD.setLayoutParams(param_tv);
        tvLCD.setTextSize((float) (buttonH/2.2));
        for(int i = 0; i < len; i++){
            LinearLayout.LayoutParams param_bt = (LinearLayout.LayoutParams) bt_array[i].getLayoutParams();
            param_bt.height = buttonH;
            param_bt.width = buttonW;
            bt_array[i].setLayoutParams(param_bt);
            bt_array[i].setTextSize((float) (buttonH/4));
        }
        if(style_calc.equals("1")){
            rlCommon.setBackgroundResource(R.drawable.gray_back);
            CF = Typeface.createFromAsset(getAssets(), "fonts/FSCristal.ttf");
            tvLCD.setTypeface(CF);
            for(int i = 0; i < len; i++){
                bt_array[i].setBackgroundResource(R.drawable.blue_button);
            }
        }else{
            rlCommon.setBackgroundResource(R.drawable.space_back);
            CF = Typeface.createFromAsset(getAssets(), "fonts/a_LCDNovaObl.ttf");
            tvLCD.setTypeface(CF);
            for(int i = 0; i < len; i++){
                bt_array[i].setBackgroundResource(R.drawable.pink_button);
            }
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.options_menu:
                startActivity(new Intent(this, PrefActivity.class));
                break;
            case R.id.about_menu:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btOne:
                ClickNumber("1");
                break;
            case R.id.btTwo:
                ClickNumber("2");
                break;
            case R.id.btThree:
                ClickNumber("3");
                break;
            case R.id.btFour:
                ClickNumber("4");
                break;
            case R.id.btFive:
                ClickNumber("5");
                break;
            case R.id.btSix:
                ClickNumber("6");
                break;
            case R.id.btSeven:
                ClickNumber("7");
                break;
            case R.id.btEight:
                ClickNumber("8");
                break;
            case R.id.btNine:
                ClickNumber("9");
                break;
            case R.id.btZero:
                if(flagAction == 0){
                    if(operand1.length() != 0)ClickNumber("0");
                }else{
                    if(operand2.length() != 0)ClickNumber("0");
                }
                break;
            case R.id.btPlus:
                if(flagAction == 0){
                    flagAction = 1;
                    flagPoint = false;
                }
                break;
            case R.id.btMinus:
                if(flagAction == 0){
                    flagAction = 2;
                    flagPoint = false;
                }
                break;
            case R.id.btMulti:
                if(flagAction == 0){
                    flagAction = 3;
                    flagPoint = false;
                }
                break;
            case R.id.btDiv:
                if(flagAction == 0){
                    flagAction = 4;
                    flagPoint = false;
                }
                break;
            case R.id.btPoint:
                if(!flagPoint){
                    if(flagAction == 0){
                        if(operand1.length() != 0){
                            ClickNumber(".");
                        }else{ClickNumber("0.");
                        }
                    }else{
                        if(operand2.length() != 0){
                            ClickNumber(".");
                        }else{ClickNumber("0.");}
                        flagPoint = true;
                    }
                }
                break;
            case R.id.btSquare:
                if(flagAction == 0){
                    if(operand1.length() == 0)operand1 = "0";
                    result = Math.pow(Double.parseDouble(operand1), 2);
                    showNumber(procNumber(result));
                    clearVariables();
                }else{
                    Toast.makeText(this, R.string.other_operation, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btRadical:
                if(flagAction == 0){
                    if(operand1.length() == 0)operand1 = "0";
                    result = Math.sqrt(Double.parseDouble(operand1));
                    showNumber(procNumber(result));
                    clearVariables();
                }else{
                    Toast.makeText(this, R.string.other_operation, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btBack:
                if(flagAction == 0){
                    if(operand1.length() != 0){
                        if(operand1.substring(operand1.length()-1, operand1.length()).equals("."))flagPoint = false;
                        operand1 = operand1.substring(0, operand1.length()-1);
                    }
                    showNumber(operand1);
                }else{
                    if(operand2.length() != 0){
                        if(operand2.substring(operand2.length()-1, operand2.length()).equals("."))flagPoint = false;
                        operand2 = operand2.substring(0, operand2.length()-1);
                    }
                    showNumber(operand2);
                }
                break;
            case R.id.btEqual:
                if(operand1.length() == 0)operand1 = "0";
                if(operand2.length() == 0)operand2 = "0";
                switch(flagAction){
                    case 1:
                        result = Double.parseDouble(operand1) + Double.parseDouble(operand2);
                        showNumber(procNumber(result));
                        clearVariables();
                        break;
                    case 2:
                        result = Double.parseDouble(operand1) - Double.parseDouble(operand2);
                        showNumber(procNumber(result));
                        clearVariables();
                        break;
                    case 3:
                        result = Double.parseDouble(operand1) * Double.parseDouble(operand2);
                        showNumber(procNumber(result));
                        clearVariables();
                        break;
                    case 4:
                        if(Double.parseDouble(operand2) != 0)result = Double.parseDouble(operand1) / Double.parseDouble(operand2);
                        showNumber(procNumber(result));
                        clearVariables();
                        break;
                    default:
                        Toast.makeText(this, R.string.no_operation, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btClear:
                clearVariables();
                showNumber(operand1);
                break;
        }
    }

    private void ClickNumber(String num){
        if(flagAction == 0){
            if(checkOver(operand1)){
                Toast.makeText(this, R.string.limit, Toast.LENGTH_LONG).show();
            }else{
                operand1 = operand1 + num;
                showNumber(operand1);
            }
        }else{
            if(checkOver(operand2)){
                Toast.makeText(this, R.string.limit, Toast.LENGTH_LONG).show();
            }else{
                operand2 = operand2 + num;
                showNumber(operand2);
            }
        }
    }

    private void showNumber(String num){
        if(num.length() == 0)num = "0";
        tvLCD.setText(num);
        if(num.equals("error"))Toast.makeText(this, R.string.degree_overflow, Toast.LENGTH_LONG).show();
    }

    private void clearVariables(){
        operand1 = "";
        operand2 = "";
        result = 0;
        flagAction = 0;
        flagPoint = false;
    }

    private String procNumber(double res){
        String num;
        long part_int = (long) (res - res%1);
        long part_frac = Math.round(res%1 * 1000000000);
        if(part_frac == 0){
            num = Long.toString(part_int);
            if(num.length() > 10)num ="error";
        }else{
            if(Long.toString(part_int).length() > 9){
                num ="error";
            }else{
                num = Long.toString(part_int) + "." + Long.toString(Math.round(part_frac / Math.pow(10, Long.toString(part_int).length()-1)));
            }
        }
        int i = 1;
        while(num.substring(num.length()-1, num.length()).equals("0") & num.contains(".")){
            num = num.substring(0, num.length()-1);
        }
        return num;
    }

    private boolean checkOver(String var){
        int corr = 0;
        if(var.contains("."))corr = 1;
        if(var.length()-corr == 10){
            return true;
        }else{return false;}
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("LCD_value", tvLCD.getText().toString());
        outState.putBoolean("flagPoint", flagPoint);
        outState.putString("operand1", operand1);
        outState.putString("operand2", operand2);
        outState.putInt("flagAction", flagAction);
        outState.putDouble("result", result);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvLCD.setText(savedInstanceState.getString("LCD_value"));
        flagPoint = savedInstanceState.getBoolean("flagPoint");
        operand1 = savedInstanceState.getString("operand1");
        operand2 = savedInstanceState.getString("operand2");
        flagAction = savedInstanceState.getInt("flagAction");
        result = savedInstanceState.getDouble("result");
    }
}