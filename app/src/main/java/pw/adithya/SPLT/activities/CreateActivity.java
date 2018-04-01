package pw.adithya.SPLT.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup;

import java.util.ArrayList;
import java.util.List;

import co.dift.ui.SwipeToAction;
import pw.adithya.SPLT.adapters.ExtrasAdapter;
import pw.adithya.SPLT.objects.Bill;
import pw.adithya.SPLT.objects.Extra;
import pw.adithya.SPLT.objects.Participant;
import pw.adithya.SPLT.adapters.BillsAdapter;
import pw.adithya.SPLT.adapters.ContributionsAdapter;
import pw.adithya.SPLT.adapters.ParticipantsAdapter;
import pw.adithya.splitandpay.R;

public class CreateActivity extends AppCompatActivity {
    ArrayList<Participant> participantsArrayList;
    ArrayList<Bill> billsArrayList;
    ArrayList<Participant> contributionsArrayList;
    ArrayList<Extra> extrasArrayList;
    ParticipantsAdapter participantsAdapter;
    ArrayList<String> stringsList;
    BillsAdapter billsAdapter;
    ExtrasAdapter extrasAdapter;
    ContributionsAdapter contributionsAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Button createButton = findViewById(R.id.button_create);

        ImageView addParticipantsButton = findViewById(R.id.button_participants_add);
        ImageView addBillsButton = findViewById(R.id.button_bills_add);
        ImageView addContributionsButton = findViewById(R.id.button_contributions_add);
        ImageView addExtrasButton = findViewById(R.id.button_extras_add);

        stringsList = new ArrayList<>();
        participantsArrayList = new ArrayList<>();
        billsArrayList = new ArrayList<>();
        contributionsArrayList = new ArrayList<>();
        extrasArrayList = new ArrayList<>();
        context = this;

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Bill");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_participants);
        RecyclerView billsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_bills);
        final RecyclerView contributionsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_contributions);
        RecyclerView extrasRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_extras);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this);

        CardView headersCardView = findViewById(R.id.cardview_headers);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        billsRecyclerView.setLayoutManager(layoutManager2);
        billsRecyclerView.setHasFixedSize(true);
        contributionsRecyclerView.setLayoutManager(layoutManager3);
        contributionsRecyclerView.setHasFixedSize(true);
        extrasRecyclerView.setLayoutManager(layoutManager4);
        extrasRecyclerView.setHasFixedSize(true);

        participantsAdapter = new ParticipantsAdapter(participantsArrayList, this);
        recyclerView.setAdapter(participantsAdapter);

        billsAdapter = new BillsAdapter(billsArrayList, this);
        billsRecyclerView.setAdapter(billsAdapter);

        contributionsAdapter = new ContributionsAdapter(contributionsArrayList, this);
        contributionsRecyclerView.setAdapter(contributionsAdapter);

        extrasAdapter = new ExtrasAdapter(extrasArrayList, this);
        extrasRecyclerView.setAdapter(extrasAdapter);

        SwipeToAction swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<String>() {
            @Override
            public boolean swipeLeft(final String itemData) {
                final int pos = removeParticipant(itemData);
                displaySnackbar(itemData + " deleted", "Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addParticipant(pos, itemData);
                    }
                });
                return true;
            }

            @Override
            public boolean swipeRight(String itemData) {
                return false;
            }

            @Override
            public void onClick(String itemData) {

            }

            @Override
            public void onLongClick(String itemData) {

            }
        });

        addParticipantsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new MaterialDialog.Builder(context)
                        .title("Add a Participant")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Hint", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                participantsArrayList.add(new Participant(input.toString()));
                                participantsAdapter.notifyDataSetChanged();
                            }
                        })
                        .negativeText("Cancel")
                        .positiveText("Add")
                        .show();
            }
        });

        addBillsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setStringList();

                if (stringsList.size() != 0) {
                    MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                            .title("Add a Bill")
                            .customView(R.layout.custom_bill_view, true)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    Bill bill = new Bill();
                                    bill.desc = ((EditText) dialog.getCustomView().findViewById(R.id.edittext_desc)).getText().toString();
                                    bill.qty = Integer.parseInt(((EditText) dialog.getCustomView().findViewById(R.id.edittext_qty)).getText().toString());
                                    bill.price = Double.valueOf(((EditText) dialog.getCustomView().findViewById(R.id.edittext_price)).getText().toString());
                                    bill.total = bill.qty * bill.price;
                                    billsArrayList.add(bill);
                                    billsAdapter.notifyDataSetChanged();

                                    CompoundButtonGroup compoundButtonGroup = dialog.getCustomView().findViewById(R.id.cmpbtngrp);
                                    List<Integer> intList = compoundButtonGroup.getCheckedPositions();

                                    for (int i : intList) {
                                        participantsArrayList.get(i).price += bill.total / intList.size();
                                    }
                                }
                            })
                            .positiveText("Add")
                            .show();

                    CompoundButtonGroup compoundButtonGroup = materialDialog.getCustomView().findViewById(R.id.cmpbtngrp);

                    compoundButtonGroup.setEntries(stringsList);
                    compoundButtonGroup.reDraw();
                }
                else
                    Toast.makeText(context, "You need to add some participants first", Toast.LENGTH_SHORT).show();
            }
        });

        addContributionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setStringList();

                if(stringsList.size() != 0) {
                    MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                            .title("Add a Bill")
                            .customView(R.layout.custom_contribution_view, true)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    Spinner spinner = dialog.getCustomView().findViewById(R.id.contributionSpinner);

                                    Log.e("Name", spinner.getSelectedItem().toString());

                                    for (Participant p : participantsArrayList)
                                    {
                                        if (p.name.equals(spinner.getSelectedItem().toString())) {
                                            p.contrib = Double.parseDouble(((EditText) dialog.getCustomView().findViewById(R.id.edittext_price)).getText().toString());
                                            contributionsArrayList.add(p);
                                        }
                                    }

                                    contributionsAdapter.notifyDataSetChanged();
                                }
                            })
                            .positiveText("Add")
                            .show();

                    Spinner spinner = materialDialog.getCustomView().findViewById(R.id.contributionSpinner);
                    ArrayAdapter dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, stringsList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                }
                else
                    Toast.makeText(context, "You need to add some participants first", Toast.LENGTH_SHORT).show();
            }
        });

        addExtrasButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                        .title("Add Extras")
                        .customView(R.layout.custom_extras_view, true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                Spinner spinner = dialog.getCustomView().findViewById(R.id.descSpinner);
                                Extra extra = new Extra();
                                EditText percentageEditText = dialog.getCustomView().findViewById(R.id.edittext_percentage);
                                extra.desc = spinner.getSelectedItem().toString();
                                extra.percentage = Double.parseDouble(percentageEditText.getText().toString());
                                extrasArrayList.add(extra);
                                extrasAdapter.notifyDataSetChanged();
                            }
                        })
                        .positiveText("Add")
                        .show();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double extras = 1 + ((extrasArrayList.get(0).percentage + extrasArrayList.get(1).percentage) / 100);

                for (Participant p: participantsArrayList) {
                    Log.e(p.name, "" + (p.price * extras - p.contrib));
                }
            }
        });
    }

    private void displaySnackbar(String text, String actionName, View.OnClickListener action) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                .setAction(actionName, action);

        View v = snack.getView();
        //v.setBackgroundColor(getResources().getColor(R.color.secondary));
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.BLACK);

        snack.show();
    }

    private void setStringList()
    {
        stringsList = new ArrayList<>();

        for (Participant participant : participantsArrayList)
        {
            stringsList.add(participant.name);
        }
    }

    private int removeParticipant(String name) {
        int pos = participantsArrayList.indexOf(new Participant(name));
        participantsArrayList.remove(new Participant(name));
        participantsAdapter.notifyItemRemoved(pos);
        return pos;
    }

    private void addParticipant(int pos, String name) {
        participantsArrayList.add(pos, new Participant(name));
        participantsAdapter.notifyItemInserted(pos);
    }
}
