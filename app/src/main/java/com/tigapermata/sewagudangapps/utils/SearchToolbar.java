package com.tigapermata.sewagudangapps.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.tigapermata.sewagudangapps.R;
import com.tigapermata.sewagudangapps.activity.ScanActivity;
import com.tigapermata.sewagudangapps.activity.inbound.FormIncomingActivity;
import com.tigapermata.sewagudangapps.activity.inbound.ListIncomingActivity;

public class SearchToolbar  implements View.OnClickListener{

    public final static int  VOICE_INTENT_REQUEST_CODE = 9999;

    View view;
    Context context;
    RelativeLayout searchLayout;
    EditText searchEditText;
    ImageButton arrowBackBtn;
    ImageButton micBtn;
    ImageButton clearBtn;
    private InputMethodManager imm;
    OnSearchToolbarQueryTextListner listner;



    public SearchToolbar(Context context,OnSearchToolbarQueryTextListner listner, View view)
    {
        this.view = view;
        this.context = context;
        searchLayout = view.findViewById(R.id.search_layout);
        searchEditText = view.findViewById(R.id.searchEditText);
        arrowBackBtn = view.findViewById(R.id.ic_arrowBack);
        micBtn = view.findViewById(R.id.ic_barcode);
        clearBtn = view.findViewById(R.id.ic_clear);
        this.listner = listner;


        arrowBackBtn.setOnClickListener(this);
        micBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
    }



    /* Jb Toolbar open ho to:
     *  - focus editText pr ho aur keyboard open ho aur editText null ho
     *  - jb user Type krna shuru ho to mic to hide kr k clear btn ho show krna hy,
     *  - clearButton Press ho to editText clear ho jaey
     *  - jb keyboard k submit button pr click ho to agr Edittext empty hy to keyboar hide na ho aur agr edittext empty
     *    nahi hy to user ko query show kr k editText ko null kr dyna hy
     * */
    public void openSearchToolbar() // Open the SearchToolbar
    {
        searchLayout.setVisibility(View.VISIBLE);

        /**  focus editText pr ho aur keyboard open ho aur editText null ho*/
        searchEditText.setText(null);
        searchEditText.requestFocus();
        openKeyboard();

        /**  jb user Type krna shuru ho to mic to hide kr k clear btn ho show krna hy,*/
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                listner.onQueryTextChange(editable.toString());
                if(editable.length()>0)
                {
                    micBtn.setVisibility(View.GONE);
                    clearBtn.setVisibility(View.VISIBLE);
                }
                else
                {
                    micBtn.setVisibility(View.VISIBLE);
                    clearBtn.setVisibility(View.GONE);
                }
            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(textView.getText().toString().length()<1)
                {
                    return true;
                }
                else
                {
                    listner.onQueryTextSubmit(textView.getText().toString());
                    closeSearchToolbar();
                    return false;
                }


            }
        });


    }


    /* Close the tooolbar:
     * jb toolbar close ho to searchLayout hide ho jaey aur keyboar b close ho jaey*/
    private void closeSearchToolbar() {
        searchLayout.setVisibility(View.GONE);
        listner.onBackClick();
        closeKeyboard();
    }



    private void openKeyboard()
    {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void closeKeyboard()
    {
        imm.hideSoftInputFromWindow(arrowBackBtn.getWindowToken(),0);
    }




    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.ic_arrowBack:
                closeSearchToolbar();
                break;

            case R.id.ic_clear:
                searchEditText.setText(null);
                break;

            case R.id.ic_barcode:
                listner.onBarcodeScan();
        }

    }



    public interface OnSearchToolbarQueryTextListner
    {
        void onQueryTextSubmit(String query);
        void onQueryTextChange(String editable);
        void onBarcodeScan();
        void onBackClick();
    }


}

