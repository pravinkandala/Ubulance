package com.pk.ubulance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GetProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_product);


        Api api  = new Api(this,"38.998725","-76.866995",null,null,null);
        api.getProductId();
        if(api.getFirst_productID()!=null){
            Log.d("gotproductback",api.getFirst_productID());
        }else{
            Toast.makeText(this,"There are no uber cars around you\n please call 911",Toast.LENGTH_LONG).show();

        }

    }
}
