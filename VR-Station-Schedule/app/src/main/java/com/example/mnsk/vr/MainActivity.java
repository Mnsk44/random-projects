package com.example.mnsk.vr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Teemu Männikkö
 * Simple Android-app to search train schedules according to station,
 * from Finnish national railways (VR)
 * Programmed for and tested on Android 6.0, API 23 (Honor 7)
 */

public class MainActivity extends AppCompatActivity {

    public final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public final String NEW_DATE_FORMAT = "HH:mm dd.MM.";
    public static final String VR_URL = "https://rata.digitraffic.fi/";
    public final String EMPTY = " ";
    public final String EMPTY0 = "";
    public final String MIN = " min";
    public final String ARR_TRAINS = "Saapuvat junat:";
    public final String DEP_TRAINS = "Lähtevät junat:";
    public final String ARRIVAL = "ARRIVAL";
    public final String DEPARTURE = "DEPARTURE";
    public final String TRAIN = "Juna:";
    public final String TIME_IN = "Saapumisaika:";
    public final String TIME_OUT = "Lähtöaika:";
    public final String DELAY = "Viive:";
    public final String FROM = "Suunnasta:";
    public final String TO = "Suuntaan:";
    public final String ERROR = "Virhe: ";
    public final String CONN_ERROR = "Yhteysvirhe, yritä uudelleen\n";
    public final String NAME_ERROR = "Virheellinen aseman nimi!";
    public final String STATION = "Asema: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Build retrofit instance to connect VR's REST API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VR_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final VRApi vrService = retrofit.create(VRApi.class);

        // Call search for current list of stations for searching.
        setStations(vrService);

        // Set "search"-button to trigger searchTrains from VR API
        final Button searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchTrains(vrService);
            }
        });
    }

    /**
     * Handle on-click search, pre-determined amount of trains (20/direction).
     * Gets station information from AutoCompleteView
     * @param service Service provider (VR) for train search.
     */

    protected void searchTrains(VRApi service) {
        // UI components
        final AutoCompleteTextView searchStation = findViewById(R.id.searchStation);
        final TableLayout arrive = findViewById(R.id.arrive);
        final TableLayout departure = findViewById(R.id.departure);
        String ss = EMPTY0;
        String stat = EMPTY0;

        // Get station name information from AutoCompleteView if set correctly
        if(!TextUtils.isEmpty(searchStation.getText().toString())) {
            ss = searchStation.getText().toString();
            if(ss.length() > 3) {
                stat = ss.substring(3, ss.length());
            }
        }

        TableRow line = new TableRow(MainActivity.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        line.setLayoutParams(lp);

        // Check once more that station name is proper
        if(ss.length() > 1) {

            String sscPRE;
            if(ss.length() == 2) {
                sscPRE = ss.substring(0, 2).trim();
            } else {
                sscPRE = ss.substring(0, 3).trim();
            }
            final String ssc = sscPRE;
            // final check for station name
            if(checkStation(ssc)) {

                // Call table preparations
                prepareTables(stat);
                prepareCategories();

                // Get train-list from VR
                Call<JsonArray> trains = service.getTrains(ssc);

                trains.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> trains, Response<JsonArray> response) {

                        if (response.isSuccessful()) {
                            try {
                                // Parse JSONArray from VR
                                String jsonString = response.body().toString();
                                Type listType = new TypeToken<List<Train>>() {}.getType();
                                List<Train> trainList = new Gson().fromJson(jsonString, listType);

                                // First iteration will set arriving trains list
                                trainList = sortTrainsBySchedule(trainList, ssc, ARRIVAL);

                                // Go through every train operating via predetermined station
                                for (int i = 0; i < trainList.size(); i++) {
                                    List<TimeTableRow> tt = trainList.get(i).getTimeTableRows();
                                    // Go through every stop train makes and pick up schedule for station x
                                    for (int j = 0; j < tt.size(); j++) {
                                        if (Objects.equals(tt.get(j).getStationShortCode(), ssc)) {
                                            boolean future = false;
                                            // This should limit to trains arriving/departing in the future
                                            try {
                                                future = isInFuture(tt.get(j).getScheduledTime());
                                            } catch (ParseException e) {
                                                Toast.makeText(MainActivity.this, CONN_ERROR + e, Toast.LENGTH_LONG).show();
                                            }

                                            // We gather information only for trains in the future with correct direction
                                            if (future) {
                                                if (Objects.equals(tt.get(j).getType(), ARRIVAL)) {
                                                    TableRow line = new TableRow(MainActivity.this);
                                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                                    line.setLayoutParams(lp);

                                                    TextView train = new TextView(MainActivity.this);
                                                    String trainNN = trainList.get(i).getTrainType() + EMPTY + Integer.toString(trainList.get(i).getTrainNumber());
                                                    train.setText(trainNN);
                                                    TextView time = new TextView(MainActivity.this);
                                                    time.setText(parseTime(tt.get(j).getScheduledTime()));
                                                    TextView delay = new TextView(MainActivity.this);
                                                    TextView dir = new TextView(MainActivity.this);
                                                    // For trains coming to station, get starting point station
                                                    dir.setText(tt.get(0).getStationShortCode());

                                                    line.addView(train);
                                                    line.addView(time);
                                                    // Difference in minutes can get null-values
                                                    if (tt.get(j).getDifferenceInMinutes() != null) {
                                                        String arr = Integer.toString(tt.get(j).getDifferenceInMinutes()) + MIN;
                                                        delay.setText(arr);
                                                        line.addView(delay);
                                                    } else {
                                                        delay.setText(EMPTY);
                                                        line.addView(delay);
                                                    }
                                                    line.addView(dir);

                                                    //Finally add item to the correct list
                                                    arrive.addView(line);
                                                }
                                            }
                                        }
                                    }
                                }

                                // Second iteration will set departing trains list
                                trainList = sortTrainsBySchedule(trainList, ssc, DEPARTURE);

                                // Go through every train operating via predetermined station
                                for (int i = 0; i < trainList.size(); i++) {
                                    List<TimeTableRow> tt = trainList.get(i).getTimeTableRows();
                                    // Go through every stop train makes and pick up schedule for station x
                                    for (int j = 0; j < tt.size(); j++) {
                                        if (Objects.equals(tt.get(j).getStationShortCode(), ssc)) {
                                            boolean future = false;
                                            // This should limit to trains arriving/departing in the future
                                            try {
                                                future = isInFuture(tt.get(j).getScheduledTime());
                                            } catch (ParseException e) {
                                                Toast.makeText(MainActivity.this, CONN_ERROR + e, Toast.LENGTH_LONG).show();
                                            }
                                            // We gather information only for trains in the future with correct direction
                                            if (future) {
                                                if (Objects.equals(tt.get(j).getType(), DEPARTURE)) {

                                                    TableRow line = new TableRow(MainActivity.this);
                                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                                    line.setLayoutParams(lp);

                                                    TextView train = new TextView(MainActivity.this);
                                                    String trainNN = trainList.get(i).getTrainType() + EMPTY + Integer.toString(trainList.get(i).getTrainNumber());
                                                    train.setText(trainNN);
                                                    TextView time = new TextView(MainActivity.this);
                                                    time.setText(parseTime(tt.get(j).getScheduledTime()));
                                                    TextView delay = new TextView(MainActivity.this);
                                                    TextView dir = new TextView(MainActivity.this);

                                                    // For trains leaving station, get their final destination
                                                    dir.setText(tt.get(tt.size() - 1).getStationShortCode());

                                                    line.addView(train);
                                                    line.addView(time);
                                                    // Difference in minutes can get null-values
                                                    if (tt.get(j).getDifferenceInMinutes() != null) {
                                                        String arr = Integer.toString(tt.get(j).getDifferenceInMinutes()) + MIN;
                                                        delay.setText(arr);
                                                        line.addView(delay);
                                                    } else {
                                                        delay.setText(EMPTY);
                                                        line.addView(delay);
                                                    }
                                                    line.addView(dir);

                                                    // Finally add item to the list
                                                    departure.addView(line);
                                                }
                                            }
                                        }
                                    }
                                }

                            } catch (NullPointerException e) {
                                // UI Toast for user if something goes wrong
                                Toast.makeText(MainActivity.this, CONN_ERROR + e, Toast.LENGTH_LONG).show();

                                // ADD ERROR LOGGING ETC
                            }


                        } else {
                            // Response problem handling
                            int statusCode = response.code();
                            TableRow error = new TableRow(MainActivity.this);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            error.setLayoutParams(lp);
                            TextView err = new TextView(MainActivity.this);
                            String er = ERROR + Integer.toString(statusCode);
                            err.setText(er);
                            arrive.addView(err);

                            // ADD PROPER HANDLING/LOGS ETC
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        // ADD PROPER ERROR HANDLING
                        Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Names other than official stations produce a toast
                Toast.makeText(MainActivity.this, NAME_ERROR, Toast.LENGTH_SHORT).show();
            }
        } else {
            // Names other than official stations produce a toast
            Toast.makeText(MainActivity.this, NAME_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Find train stations and fill them to AutoCompleteTextView on main page.
     * @param service Service provider (VR) for RESTful API
     */
    private void setStations(VRApi service) {
        final AutoCompleteTextView searchStation = findViewById(R.id.searchStation);

        // Get JSON-format from VR
        Call<JsonArray> stations = service.getStations();

        stations.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> stations, Response<JsonArray> response) {

                if(response.isSuccessful()) {
                    try {
                        // Parse JSONArray from VR
                        String jsonString = response.body().toString();
                        Type listType = new TypeToken<List<Station>>() {}.getType();
                        List<Station> stationsList = new Gson().fromJson(jsonString, listType);

                        // String-list for search functionality
                        List<String> stationsNamesList = new ArrayList<String>();
                        for (int i = 0; i < stationsList.size(); i++) {
                            if (stationsList.get(i).getPassengerTraffic()) {
                                stationsNamesList.add(stationsList.get(i).getStationShortCode() + EMPTY + stationsList.get(i).getStationName());
                            }
                        }

                        // Set station names  for autocompleteview search functionality.
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1, stationsNamesList);
                        searchStation.setAdapter(dataAdapter);
                    } catch (NullPointerException e) {
                        Toast.makeText(MainActivity.this, CONN_ERROR + e, Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Connection error handling
                    int statusCode  = response.code();
                    Toast.makeText(MainActivity.this, CONN_ERROR + Integer.toString(statusCode), Toast.LENGTH_LONG).show();

                    // ADD LOGGING ETC FUNCTIONALITY
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                // ADD PROPER ERROR HANDLING
                Toast.makeText(MainActivity.this, CONN_ERROR, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Check if searchable station name is proper.
     * @param station Station name to search for
     * @return
     */
    private boolean checkStation(String station) {
        AutoCompleteTextView searchBar = findViewById(R.id.searchStation);
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) searchBar.getAdapter();
        int n = adapter.getCount();
        List<String> stations = new ArrayList<>(n);
        // Get station shortcodes from AutoCompleteTExtView
        for(int i = 0; i < n; i++) {
            stations.add(adapter.getItem(i).substring(0,3));
        }
        // Check if station shortcode is proper
        return stations.contains(station);
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Prepares TableViews with headlines and station name
     * @param station Name of the station
     */
    private void prepareTables(String station) {
        TableLayout arrive = findViewById(R.id.arr_info);
        TableLayout departure = findViewById(R.id.dep_info);

        // For another search, clear previous
        if( arrive.getChildCount() > 0) {
            arrive.removeAllViews();
        }
        if( departure.getChildCount() > 0) {
            departure.removeAllViews();
        }

        // Station name, direction added to tables
        TableRow arrivals = new TableRow(this);
        TableRow departures = new TableRow(this);
        TableRow arrInfo = new TableRow(this);
        TableRow depInfo = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        arrivals.setLayoutParams(lp);
        departures.setLayoutParams(lp);
        arrInfo.setLayoutParams(lp);
        depInfo.setLayoutParams(lp);

        TextView arr = new TextView(this);
        TextView dep = new TextView(this);
        TextView at = new TextView(this);
        TextView dt = new TextView(this);

        arr.setText(ARR_TRAINS);
        dep.setText(DEP_TRAINS);
        String st = STATION + station;
        at.setText(st);
        dt.setText(st);

        arrivals.addView(arr);
        departures.addView(dep);
        arrInfo.addView(at);
        depInfo.addView(dt);

        arrive.addView(arrivals);
        departure.addView(departures);
        arrive.addView(arrInfo);
        departure.addView(depInfo);

    }

    /**
     * Prepares the categories to be tabled in TableViews
     */
    private void prepareCategories() {
        TableLayout arrive = findViewById(R.id.arrive);
        TableLayout departure = findViewById(R.id.departure);

        // For another search, clear previous
        if( arrive.getChildCount() > 0) {
            arrive.removeAllViews();
        }
        if( departure.getChildCount() > 0) {
            departure.removeAllViews();
        }

        // Add column names, divide table equally for all etc properties
        arrive.setStretchAllColumns(true);
        departure.setStretchAllColumns(true);

        TableRow arrInfo = new TableRow(this);
        TableRow depInfo = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        arrInfo.setLayoutParams(lp);
        depInfo.setLayoutParams(lp);


        TextView at = new TextView(this);
        TextView dt = new TextView(this);
        TextView ati = new TextView(this);
        TextView dti = new TextView(this);
        TextView atii = new TextView(this);
        TextView atiii= new TextView(this);
        TextView dtii = new TextView(this);
        TextView dtiii = new TextView(this);
        at.setText(TRAIN);
        dt.setText(TRAIN);
        ati.setText(TIME_IN);
        atii.setText(DELAY);
        atiii.setText(FROM);
        dti.setText(TIME_OUT);
        dtii.setText(DELAY);
        dtiii.setText(TO);
        arrInfo.addView(at);
        arrInfo.addView(ati);
        arrInfo.addView(atii);
        arrInfo.addView(atiii);
        depInfo.addView(dt);
        depInfo.addView(dti);
        depInfo.addView(dtii);
        depInfo.addView(dtiii);

        arrive.addView(arrInfo);
        departure.addView(depInfo);
    }


    // ----------------------------------------------------------------------------------------------

    /**
     * Formats time from ISO8601 (yyyy-MM-ddTHH:mm:ss.SSSZ) string to HH:mm dd.MM.
     * @param time Time string to format
     * @return Formatted time string.
     */
    private String parseTime(String time) {

        // Service provides times in yyyy-MM-dd T HH:mm:ss.SSSZ UTC datetime format
        SimpleDateFormat input = new SimpleDateFormat(DATE_FORMAT);
        input.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date parsed = input.parse(time);
            // Format the time to HH:mm dd.MM. and change timezone to device default
            input = new SimpleDateFormat(NEW_DATE_FORMAT);
            input.setTimeZone(TimeZone.getDefault());

            return input.format(parsed);
        } catch (ParseException e) {
            // ADD BETTER ERROR HANDLING
            return ERROR + e;
        }
    }

    /** Check if trains schedule is in the future from current time.
     @param time Time to check if it is in the future.
     @return If value is in the future, boolean
     */

    private boolean isInFuture(String time) throws ParseException {
        // Get UTC time format
        SimpleDateFormat input = new SimpleDateFormat(DATE_FORMAT);
        input.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsed = input.parse(time);
        // Format the times to milliseconds long format and compare
        Long current = System.currentTimeMillis();
        Long currT = parsed.getTime();

        return currT >= current;
    }

    // ----------------------------------------------------------------------------------------------

    /**
     * Sort trains according to scheduled time rather than train name
     * @param trains List of trains to sort
     * @param station Station where the schedule takes place
     * @param direction Train is either ARRIVAL or DEPARTURE
     * @return
     */

    private List<Train> sortTrainsBySchedule(List<Train> trains, final String station, final String direction) {
        Collections.sort(trains, new Comparator<Train>() {
            @Override
            public int compare(Train o1, Train o2) {
                List<TimeTableRow> ttr1 = o1.getTimeTableRows();
                List<TimeTableRow> ttr2 = o2.getTimeTableRows();

                /*
                    Initial row setup assumes that there is a stop on the predefined station.
                    (Which will be looked for with the loop next.)
                    Assumption should hold since train-list is searched with the same station assumption.
                 */
                TimeTableRow toComp1 = ttr1.get(0);
                TimeTableRow toComp2 = ttr2.get(0);

                // Get the correct station info (schedule)
                for(int i = 0; i < ttr1.size(); i++) {
                    if(Objects.equals(ttr1.get(i).getStationShortCode(), station)) {
                        if(Objects.equals(ttr1.get(i).getType(), direction)) {
                            toComp1 = ttr1.get(i);
                            break;
                        }
                    }
                }
                for(int j = 0; j < ttr2.size(); j++) {
                    if(Objects.equals(ttr2.get(j).getStationShortCode(), station)) {
                        if(Objects.equals(ttr2.get(j).getType(), direction)) {
                            toComp2 = ttr2.get(j);
                            break;
                        }
                    }
                }

                // Time format:
                SimpleDateFormat input = new SimpleDateFormat(DATE_FORMAT);
                input.setTimeZone(TimeZone.getTimeZone("UTC"));

                // Compare times in milliseconds
                try {
                    Date time1 = input.parse(toComp1.getScheduledTime());
                    Date time2 = input.parse(toComp2.getScheduledTime());
                    if(time1.getTime() < time2.getTime()) {
                        return -1;
                    } else if (time1.getTime() == time2.getTime()) {
                        return 0;
                    } else {
                        return 1;
                    }


                } catch (ParseException e) {
                    // ADD PROPER ERROR HANDLING
                    return 0;
                }
            }
        });

        return trains;
    }
}



