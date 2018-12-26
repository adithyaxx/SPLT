package pw.adithya.SPLT.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.markushi.ui.CircleButton;
import co.dift.ui.SwipeToAction;
import es.dmoral.toasty.Toasty;
import pw.adithya.SPLT.adapters.ExtrasAdapter;
import pw.adithya.SPLT.objects.Bill;
import pw.adithya.SPLT.objects.Combined;
import pw.adithya.SPLT.objects.Extra;
import pw.adithya.SPLT.objects.Participant;
import pw.adithya.SPLT.adapters.BillsAdapter;
import pw.adithya.SPLT.adapters.ContributionsAdapter;
import pw.adithya.SPLT.adapters.ParticipantsAdapter;
import pw.adithya.SPLT.R;

public class CreateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private ArrayList<Participant> participantsArrayList;
    ArrayList<Bill> billsArrayList;
    ArrayList<Participant> contributionsArrayList;
    ArrayList<Extra> extrasArrayList;
    ParticipantsAdapter participantsAdapter;
    ArrayList<String> stringsList;
    ArrayList<Combined> combinedArrayList;
    BillsAdapter billsAdapter;
    ExtrasAdapter extrasAdapter;
    ContributionsAdapter contributionsAdapter;
    Context context;
    Calendar calendar;
    TextView dateTextView;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    Gson gson;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Bill");*/

        //int sadi = Integer.parseInt("asdasd");

        editor = getSharedPreferences("app", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("app", MODE_PRIVATE);
        gson = new Gson();
        calendar = Calendar.getInstance();

        final EditText titleEditText = findViewById(R.id.edittext_title);

        final TextView participantsNum = findViewById(R.id.textview_participants_num);
        final TextView totalNum = findViewById(R.id.textview_total_num);
        final TextView extrasNum = findViewById(R.id.textview_extras_num);

        CircleButton createButton = findViewById(R.id.imageview_create);
        ImageView addParticipantsButton = findViewById(R.id.button_participants_add);
        ImageView addBillsButton = findViewById(R.id.button_bills_add);
        ImageView addContributionsButton = findViewById(R.id.button_contributions_add);
        ImageView addExtrasButton = findViewById(R.id.button_extras_add);

        stringsList = new ArrayList<>();
        participantsArrayList = new ArrayList<>();
        billsArrayList = new ArrayList<>();
        contributionsArrayList = new ArrayList<>();
        extrasArrayList = new ArrayList<>();

        final Type combinedArrayListType = new TypeToken<ArrayList<Combined>>(){}.getType();

        if (prefs.getString("combinedArrayList", "").equals(""))
            combinedArrayList = new ArrayList<>();
        else
            combinedArrayList = gson.fromJson(prefs.getString("combinedArrayList", ""), combinedArrayListType);

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

        intent = getIntent();

        if (intent.hasExtra("from"))
        {
            int pos = intent.getIntExtra("pos", 0);
            participantsArrayList.addAll(combinedArrayList.get(pos).participantsArrayList);
            billsArrayList.addAll(combinedArrayList.get(pos).billsArrayList);
            contributionsArrayList.addAll(combinedArrayList.get(pos).contributionsArrayList);
            extrasArrayList.addAll(combinedArrayList.get(pos).extrasArrayList);

            titleEditText.setText(combinedArrayList.get(pos).title);
            dateTextView.setText(combinedArrayList.get(pos).date);
            extrasNum.setText(combinedArrayList.get(pos).extras + "%");
            totalNum.setText("$" + combinedArrayList.get(pos).total);
            participantsNum.setText(participantsArrayList.size() + "");

            participantsAdapter.notifyDataSetChanged();
            billsAdapter.notifyDataSetChanged();
            contributionsAdapter.notifyDataSetChanged();
            extrasAdapter.notifyDataSetChanged();

            Log.e("Bills Size", billsArrayList.size() + "");
        }

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
                                            Log.e("Price", participantsArrayList.get(i).price + "");
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
                if (titleEditText.getText().toString().equals(""))
                    Toasty.error(context, "Please enter a valid title", Toast.LENGTH_SHORT, true).show();
                else if (participantsArrayList.size() == 0)
                    Toasty.error(context, "Please enter at least one participant", Toast.LENGTH_SHORT, true).show();
                else if (billsArrayList.size() == 0)
                    Toasty.error(context, "Please enter at least one bill", Toast.LENGTH_SHORT, true).show();
                else {
                    SummaryActivity.participantsArrayList = participantsArrayList;
                    SummaryActivity.billName = titleEditText.getText().toString();
                    SummaryActivity.participants = String.valueOf(participantsArrayList.size());

                    double extras = 0.0;

                    if (extrasArrayList.size() != 0) {
                        for (Extra e : extrasArrayList) {
                            extras += e.percentage;
                        }
                    }

                    SummaryActivity.extras = extras;

                    SummaryActivity.total = String.format("%.2f", calculateTotal());
                    SummaryActivity.bills = String.valueOf(billsArrayList.size());

                    Combined combined = new Combined();
                    combined.billsArrayList = billsArrayList;
                    combined.extrasArrayList = extrasArrayList;
                    combined.participantsArrayList = participantsArrayList;
                    combined.contributionsArrayList = contributionsArrayList;
                    combined.title = titleEditText.getText().toString();
                    combined.date = dateTextView.getText().toString();
                    combined.extras = extras;
                    combined.total = String.format("%.2f", calculateTotal());

                    if (intent.hasExtra("from"))
                        combinedArrayList.set(intent.getIntExtra("pos", -1), combined);
                    else
                        combinedArrayList.add(combined);

                    editor.putString("combinedArrayList", gson.toJson(combinedArrayList));
                    editor.apply();
                    editor.commit();

                    startActivity(new Intent(CreateActivity.this, SummaryActivity.class));
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
        double total = 0, extras = 0;

        for (Bill b : billsArrayList)
        {
            total += b.total;
        }

        extras = calculateExtras();

        if (extras > 0)
            total *= extras;

        Log.e("Total", total + "");

        return total;
    }

    private double calculateExtras()
    {
        double extras = 0;

        if (extrasArrayList.size() != 0) {
            for (Extra e : extrasArrayList) {
                extras += e.percentage;
            }

            extras = extras / 100 + 1;
        }

        Log.e("Extras", extras + "");

        return extras;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("d M yyyy");
        Date d = null;

        try {
            monthOfYear++;
            d = df2.parse(dayOfMonth + " " + monthOfYear + " " + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateTextView.setText(df.format(d));
    }
}
