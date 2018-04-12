package pw.adithya.SPLT.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andreabaccega.widget.FormEditText;
import com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.dift.ui.SwipeToAction;
import pw.adithya.SPLT.adapters.ExtrasAdapter;
import pw.adithya.SPLT.objects.Bill;
import pw.adithya.SPLT.objects.Extra;
import pw.adithya.SPLT.objects.Participant;
import pw.adithya.SPLT.adapters.BillsAdapter;
import pw.adithya.SPLT.adapters.ContributionsAdapter;
import pw.adithya.SPLT.adapters.ParticipantsAdapter;
import pw.adithya.SPLT.R;

public class CreateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
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
    Calendar calendar;
    TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        calendar = Calendar.getInstance();

        final EditText titleEditText = findViewById(R.id.edittext_title);

        final TextView participantsNum = findViewById(R.id.textview_participants_num);
        final TextView totalNum = findViewById(R.id.textview_total_num);
        final TextView extrasNum = findViewById(R.id.textview_extras_num);

        ImageView createButton = findViewById(R.id.imageview_create);
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
        dateTextView = findViewById(R.id.textview_date);

        Date d = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(d);

        dateTextView.setText(formattedDate);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_participants);
        final RecyclerView billsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_bills);
        final RecyclerView contributionsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_contributions);
        RecyclerView extrasRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_extras);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this);

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

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateActivity.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

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
                                Log.e("Arraylist", participantsArrayList.size() + "");
                                participantsAdapter.notifyDataSetChanged();
                                participantsNum.setText("" + participantsArrayList.size());
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

                                    FormEditText descFET, qtyFET, priceFET;
                                    descFET = dialog.getCustomView().findViewById(R.id.edittext_desc);
                                    qtyFET = dialog.getCustomView().findViewById(R.id.edittext_qty);
                                    priceFET = dialog.getCustomView().findViewById(R.id.edittext_price);

                                    boolean allValid = true;
                                    FormEditText[] allFields = {descFET, qtyFET, priceFET};

                                    for (FormEditText field: allFields) {
                                        allValid = field.testValidity() && allValid;
                                    }

                                    if (allValid) {
                                        Bill bill = new Bill();
                                        bill.desc = descFET.getText().toString();
                                        bill.qty = Double.valueOf(qtyFET.getText().toString());
                                        bill.price = Double.valueOf(priceFET.getText().toString());
                                        bill.total = bill.qty * bill.price;
                                        billsArrayList.add(bill);
                                        billsAdapter.notifyDataSetChanged();

                                        totalNum.setText("$" + String.format("%.2f", calculateTotal()));
                                        CompoundButtonGroup compoundButtonGroup = dialog.getCustomView().findViewById(R.id.cmpbtngrp);
                                        List<Integer> intList = compoundButtonGroup.getCheckedPositions();

                                        for (int i : intList) {
                                            participantsArrayList.get(i).price += bill.total / intList.size();
                                        }

                                        dialog.dismiss();
                                    }
                                }
                            })
                            .positiveText("Add")
                            .autoDismiss(false)
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
                                extrasNum.setText(String.format("%.1f", (calculateExtras() - 1) * 100) + "%");
                            }
                        })
                        .positiveText("Add")
                        .show();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SummaryActivity.participantsArrayList = participantsArrayList;
                SummaryActivity.billName = titleEditText.getText().toString();
                SummaryActivity.participants = String.valueOf(participantsArrayList.size());
                SummaryActivity.extras = calculateExtras();
                SummaryActivity.total = String.format("%.2f", calculateTotal());
                SummaryActivity.bills = String.valueOf(billsArrayList.size());

                startActivity(new Intent(CreateActivity.this, SummaryActivity.class));
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
        setStringList();
        int pos = stringsList.indexOf(name);
        participantsArrayList.remove(pos);
        participantsAdapter.notifyItemRemoved(pos);
        return pos;
    }

    private void addParticipant(int pos, String name) {
        participantsArrayList.add(pos, new Participant(name));
        participantsAdapter.notifyItemInserted(pos);
    }

    private double calculateTotal()
    {
        double total = 0;

        for (Bill b : billsArrayList)
        {
            total += b.total;
        }

        total = total * calculateExtras();

        return total;
    }

    private double calculateExtras()
    {
        double extras = 1;

        if (extrasArrayList.size() != 0) {
            for (Extra e : extrasArrayList) {
                extras += e.percentage;
            }

            extras = extras / 100 + 1;
        }

        return extras;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("d M yyyy");
        Date d = null;
        try {
            d = df2.parse(dayOfMonth + " " + monthOfYear + 1 + " " + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateTextView.setText(df.format(d));

        Log.e("Date", dayOfMonth + "");
        Log.e("Month", monthOfYear + "");
        Log.e("Year", year + "");
    }
}
