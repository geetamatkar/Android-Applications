package com.example.tipsplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

     private EditText totalBill, peopleCount;
     private RadioGroup radioGroup;
     RadioButton r12, r15, r18, r20;
     private TextView tipPercent,tipTotal,perPersonAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalBill = findViewById(R.id.editTextNumberSigned);
        peopleCount = findViewById(R.id.totalPeopleView);
        radioGroup = findViewById(R.id.radioGroup);

        r12 = findViewById(R.id.radio1);
        r15 = findViewById(R.id.radio2);
        r18 = findViewById(R.id.radio3);
        r20 = findViewById(R.id.radio4);

        tipPercent = findViewById(R.id.tipAmountView);
        tipTotal = findViewById(R.id.tipTotalView);
        perPersonAmount = findViewById(R.id.perPersonView);

    }

    public void OnClickClear(View v)
    {
        totalBill.setText("");       //clear totalBill text
        r12.setChecked(false);       //clear radio button r12
        r15.setChecked(false);       //clear radio button r15
        r18.setChecked(false);       //clear radio button r18
        r20.setChecked(false);       //clear radio button r20
        tipPercent.setText("");      //clear tipPercent text
        tipTotal.setText("");        //clear tipTotal text
        peopleCount.setText("");     //clear peopleCount text
        perPersonAmount.setText(""); //clear perPersonAmount text
    }

    public void OnTipSelect(View v){

        if(TextUtils.isEmpty(totalBill.getText().toString()))   // check if totalBill entered or not
        {
            r12.setChecked(false);
            r15.setChecked(false);
            r18.setChecked(false);
            r20.setChecked(false);
            return;
        }
        String strTotalBill;
        double doubleBill, doubleTotal, doubleTipPercent = 0.0;
        strTotalBill  = totalBill.getText().toString();
        doubleBill  = Double.parseDouble(strTotalBill);

        int viewId = v.getId();
        tipPercent.setText("$" + String.format("%.2f", doubleBill));
        if (viewId == R.id.radio1)
        {
            totalBill.setText("$" + String.format("%.2f", doubleBill));
            doubleTipPercent = (doubleBill * 0.12) ;
        }
        else if (viewId == R.id.radio2)
        {
            totalBill.setText("$" + String.format("%.2f", doubleBill));
            doubleTipPercent = (doubleBill * 0.15);

        }
        else if (viewId == R.id.radio3)
        {
            totalBill.setText("$" + String.format("%.2f", doubleBill));
            doubleTipPercent = (doubleBill * 0.18);
        }
        else if (viewId == R.id.radio4)
        {
            totalBill.setText("$"+String.format("%.2f",doubleBill));
            doubleTipPercent = (doubleBill * 0.20) ;
        }

        doubleTotal = doubleBill + doubleTipPercent;
        tipTotal.setText("$" + String.format("%.2f", doubleTotal));
    }

    public void OnClickGo(View v)
    {

        String strTotal;
        if(TextUtils.isEmpty(totalBill.getText().toString()) || TextUtils.isEmpty(peopleCount.getText().toString()))  // check if edittexts have value
        {
            return;
        }

        if(radioGroup.getCheckedRadioButtonId() == -1)    //check radio buttons clicked or not
        {
            Toast.makeText(getApplicationContext(),"Please select tip percent",Toast.LENGTH_SHORT).show();
            return;
        }

        double totalD, perPersonCalc;
        String strPeopleCount;
        strTotal = tipTotal.getText().toString().substring(1);
        totalD = Double.parseDouble(strTotal);

        strPeopleCount = peopleCount.getText().toString();
        perPersonCalc =  totalD / Double.parseDouble(strPeopleCount);   //Counting share per person

        perPersonAmount.setText("$" + String.format("%.2f", perPersonCalc));
    }

}