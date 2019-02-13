package org.feup.cmov.aef.cmov1_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.feup.cmov.aef.cmov1_app.requests.PurchaseTicketRequest;
import org.feup.cmov.aef.cmov1_app.tasks.PurchaseTicketTask;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //PurchaseTicketTask purchaseTicketTask = new PurchaseTicketTask();
        //purchaseTicketTask.execute(new PurchaseTicketRequest(3, 10, 8, "test"));
    }
}
