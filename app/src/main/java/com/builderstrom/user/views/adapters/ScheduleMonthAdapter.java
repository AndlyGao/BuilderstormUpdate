package com.builderstrom.user.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class ScheduleMonthAdapter extends RecyclerView.Adapter<ScheduleMonthAdapter.ViewHolder> implements View.OnClickListener {
    private Calendar mCalendar;
    @SuppressLint("NewApi")
    private int month, year;
    private String LOG_TAG = "ScheduleMonthAdapter";
    private TextView mTvSelectedSites;
    @SuppressWarnings("unused")
    @SuppressLint({"NewApi", "NewApi", "NewApi", "NewApi"})
    private final DateFormat dateFormatter = new DateFormat();
    private final String[] weekdayscalender = new String[]{"S", "M", "T",
            "W", "T", "F", "S"};
    private TextView mTvCurrentMonth, mTvSelectedDay, mTvSelectedDayOld;
    private GridView mGridViewMonth;
    private Animation mAnimationLeft, mAnimationRight, mAnimationUp, mAnimationDown;
    private PopupWindow popup;
    private GridView mGridViewDays;
    private String mServerTime = "", selectedDate = "";
    private ArrayList<String> colorList;
    private ClickCallback callBack;
    Context context;


    public ScheduleMonthAdapter(Context context, String dateTime, ClickCallback callBack) {
        this.context = context;
        this.callBack = callBack;
        mServerTime = dateTime;
        showPopUp();

    }

    private void showPopUp() {
        try {
            popup = new PopupWindow(context);
            View layout = LayoutInflater.from(context).inflate(R.layout.popup_month, null);
            popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popup.setContentView(layout);
            popup.setOutsideTouchable(true);
            popup.setFocusable(true);
            popup.setBackgroundDrawable(new BitmapDrawable());
            RelativeLayout copy_week = layout.findViewById(R.id.copy_week);

            copy_week.setOnClickListener(view -> {
                Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
                popup.dismiss();
                callBack.onCopyMonth();
                //tODO
            });

            RelativeLayout clear_week = layout.findViewById(R.id.clear_week);
            clear_week.setOnClickListener(view -> {
                popup.dismiss();
                clearMonth();
            });

            RelativeLayout today = layout.findViewById(R.id.rl_today);
            today.setOnClickListener(view -> {
                setCurrentMonth();
                popup.dismiss();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCurrentMonth() {
        try {
            Log.d(LOG_TAG, "setCurrentMonth server time is " +
                    mServerTime);
            int mCurrentYear = Integer.parseInt(mServerTime.substring(0, 4).replace("-", ""));
            int mCurrentMonth = Integer.parseInt(mServerTime.substring(5, 7).replace("-", ""));
            int mCurrentDayOfMonth = Integer.parseInt(mServerTime.substring(8, 10).replace("-", ""));
            Log.d(LOG_TAG, "setCurrentMonth month" + mCurrentMonth + "  " +
                    "year  " + mCurrentYear);

            String selectedMonth = mCurrentMonth > 9 ? mCurrentMonth + "" : "0" + mCurrentMonth;

            getAllMonthDataFromApi(mCurrentYear + "-" + selectedMonth + "-01", mCurrentMonth, mCurrentYear);

            callBack.onDateClicked(mServerTime, mCurrentDayOfMonth + " " + getMonthName(mCurrentMonth - 1) + " " + mCurrentYear, mTvSelectedSites);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearMonth() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(Html.fromHtml("<font color='#000000'>Clear Record</font>"));
        builder.setMessage("Are you sure to clear the records?");
        builder.setPositiveButton("YES", null);
        builder.setNegativeButton("NO", null);
        AlertDialog alert = builder.create();
        alert.show();

        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.parseColor("#FF4372"));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#56C364"));

        pbutton.setOnClickListener(view -> {
            Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
            callBack.onClearMonth();
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView currentMonth;
        private Button selectedDayMonthYearButton;
        ImageView mBtnDots, prevMonth, nextMonth;

        public ViewHolder(View itemView) {
            super(itemView);

            mBtnDots = itemView.findViewById(R.id.btn_dots);
            selectedDayMonthYearButton = itemView
                    .findViewById(R.id.selectedDayMonthYear);
            prevMonth = itemView.findViewById(R.id.prevMonth);
            currentMonth = itemView.findViewById(R.id.currentMonth);
            nextMonth = itemView.findViewById(R.id.nextMonth);
            mGridViewDays = itemView.findViewById(R.id.recylarViewDays);
            mGridViewMonth = itemView.findViewById(R.id.calendar);
            mTvSelectedSites = itemView.findViewById(R.id.tv_all_sites);

            mGridViewMonth.setOnItemClickListener((adapterView, view, i, l) -> {
                mTvSelectedDay = view.findViewById(R.id.tv_day);
                setSelectedDay(mTvSelectedDay);
                callBack.onDateClicked(getSortedDate(), selectedDate, mTvSelectedSites);
            });

//            mTvSelectedSites.setOnClickListener(view -> {
//                Intent intent = new Intent(context, SitesActivity.class);
//                context.startActivity(intent);
//            });

            WeekDaysAdapter weekdaysadapter = new WeekDaysAdapter(context, weekdayscalender);
            mGridViewDays.setAdapter(weekdaysadapter);

            setCurrentMonth();

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_calendar_month, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            // FIRST HEADER, LEFT RIGHT NAVIGATION AND 3 DOTS OVERFLOW
            mCalendar = Calendar.getInstance(Locale.getDefault());
            month = mCalendar.get(Calendar.MONTH) + 1;
            year = mCalendar.get(Calendar.YEAR);

            Log.d(LOG_TAG, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
                    + year);


            holder.selectedDayMonthYearButton.setText("Selected: ");
            mAnimationLeft = AnimationUtils.loadAnimation(context, R.anim.month_lefttoright);
            mAnimationRight = AnimationUtils.loadAnimation(context, R.anim.month_righttoleft);

            mAnimationUp = AnimationUtils.loadAnimation(context, R.anim.month_slide_down);
            mAnimationDown = AnimationUtils.loadAnimation(context, R.anim.month_slide_up);

            holder.prevMonth.setOnClickListener(this);

            mTvCurrentMonth = holder.currentMonth;

            mTvCurrentMonth.setText(getMonthName(month - 1) + " " + year);

            holder.nextMonth.setOnClickListener(this);

            holder.mBtnDots.setOnClickListener(v -> {
                if (popup != null)
                    popup.showAsDropDown(holder.mBtnDots);
            });

            mGridViewMonth.setSelected(true);
            mGridViewMonth.performClick();

            setCurrentMonth();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public int getItemCount() {
        return 1;

    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    /**
     * Set Week Days on top
     */
    private class WeekDaysAdapter extends BaseAdapter {
        Context context;
        String[] finalweekDays;

        WeekDaysAdapter(Context context, String[] weekdayscalender) {
            this.context = context;
            this.finalweekDays = weekdayscalender;
        }

        @Override
        public int getCount() {
            return finalweekDays.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.week_gridcell, parent, false);
                TextView calendar_day_gridcell_day = row.findViewById(R.id.calendar_day_gridcell_day);
                calendar_day_gridcell_day.setText(finalweekDays[position] + "");
            }
            return row;
        }
    }

    /**
     * @param month month
     * @param year  year
     */
    private void setGridCellAdapterToDate(int month, int year, List<String> colorList) {
        GridCellAdapter mCalendarAdapter = new GridCellAdapter(context, month, year, colorList);
        mCalendar.set(year, month - 1, mCalendar.get(Calendar.DAY_OF_MONTH));

        mTvCurrentMonth.setText(getMonthName(month - 1) + " " + year);
        mCalendarAdapter.notifyDataSetChanged();
        mGridViewMonth.setAdapter(mCalendarAdapter);

    }

    private String getMonthName(int monthIndex) {
        String month = "";
        switch (monthIndex) {
            case 0:
                month = "Jan";
                break;
            case 1:
                month = "Feb";
                break;
            case 2:
                month = "Mar";
                break;
            case 3:
                month = "Apr";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "Jun";
                break;
            case 6:
                month = "Jul";
                break;
            case 7:
                month = "Aug";
                break;
            case 8:
                month = "Sep";
                break;
            case 9:
                month = "Oct";
                break;
            case 10:
                month = "Nov";
                break;
            case 11:
                month = "Dec";
                break;
        }

        return month;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.prevMonth) {
            mTvCurrentMonth.startAnimation(mAnimationDown);
            mGridViewDays.startAnimation(mAnimationLeft);
            mGridViewMonth.startAnimation(mAnimationLeft);
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            Log.d(LOG_TAG, "Setting Prev Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);

            String selectedMonth = month > 9 ? month + "" : "0" + month;

            getAllMonthDataFromApi(year + "-" + selectedMonth + "-01", month, year);
        }
        if (v.getId() == R.id.nextMonth) {
            mTvCurrentMonth.startAnimation(mAnimationUp);
            mGridViewDays.startAnimation(mAnimationRight);
            mGridViewMonth.startAnimation(mAnimationRight);

            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }

            Log.d(LOG_TAG, "Setting Next Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);

            String selectedMonth = month > 9 ? month + "" : "0" + month;

            getAllMonthDataFromApi(year + "-" + selectedMonth + "-01", month, year);
        }

    }


    // Inner Class
    private class GridCellAdapter extends BaseAdapter {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        List<String> colorList;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"JAN", "FEB", "MAR",
                "APR", "MAY", "JUN", "JUL", "AUG", "SEP",
                "OCT", "NOV", "DEC"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private TextView mTvDay;
        boolean isCurrentMonth = true;

        // Days in Current Month
        GridCellAdapter(Context context,
                        int month, int year, List<String> colorList) {
            super();
            this._context = context;
            this.list = new ArrayList<>();
            this.colorList = colorList;

            Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
                    + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());


            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            isCurrentMonth = month == currentMonth;

            Log.d(tag, "CheckMonth: " + month + " == " + currentMonth + "  isCurrentMonth :" + isCurrentMonth);

            // Print Month
            printMonth(month, year, isCurrentMonth, colorList);

        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm month
         * @param yy year
         */
        private void printMonth(int mm, int yy, boolean isCurrentMonth, List<String> colorList) {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces;
            int daysInPrevMonth;
            int prevMonth;
            int prevYear;
            int nextMonth;
            int nextYear;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
                    + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day:" + currentWeekDay + " is "
                    + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                Log.d(tag,
                        "PREV MONTH:= "
                                + prevMonth
                                + " => "
                                + getMonthAsString(prevMonth)
                                + " "
                                + String.valueOf((daysInPrevMonth
                                - trailingSpaces + DAY_OFFSET)
                                + i));
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                Log.d(currentMonthName, String.valueOf(i) + " "
                        + getMonthAsString(currentMonth) + " " + yy);

                if (colorList.size() > 0) {

                    if (i == getCurrentDayOfMonth() && isCurrentMonth) {
                        list.add(String.valueOf(i) + "-BLACK_" + colorList.get(i - 1) + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
                    } else {
                        list.add(String.valueOf(i) + "-" + colorList.get(i - 1) + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
                    }

                } else {

                    if (i == getCurrentDayOfMonth() && isCurrentMonth) {
                        list.add(String.valueOf(i) + "-BLACK" + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
                    } else {
                        list.add(String.valueOf(i) + "-WHITE" + "-"
                                + getMonthAsString(currentMonth) + "-" + yy);
                    }
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.screen_gridcell, parent, false);
            }
            // Get a reference to the Day mTvDay
            mTvDay = row.findViewById(R.id.tv_day);


            // ACCOUNT FOR SPACING

            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];

            // Set the Day GridCell
            mTvDay.setText(theday);


            mTvDay.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

            mGridViewMonth.setSelected(true);

            mTvDay.setTextColor(ContextCompat.getColor(context,
                    R.color.white));
            mTvDay.setBackgroundResource(R.drawable.shape_circle_transparent);

            if (day_color[1].equalsIgnoreCase("GREY")) {
                mTvDay.setTextColor(ContextCompat.getColor(context, R.color.lightgray));
                mTvDay.setVisibility(View.INVISIBLE);
                mTvDay.setEnabled(false);
            }

            if (day_color[1].equalsIgnoreCase("WHITE")) {
                mTvDay.setTextColor(ContextCompat.getColor(context,
                        R.color.white));
            }

            if (day_color[1].equalsIgnoreCase("BLACK_GREEN")) {
                mTvDay.setBackgroundResource(R.drawable.shape_circle_white_green_border);
                setSelectedDay(mTvDay);
            } else if (day_color[1].equalsIgnoreCase("BLACK_RED")) {
                mTvDay.setBackgroundResource(R.drawable.shape_circle_red_white);
                setSelectedDay(mTvDay);
            } else if (day_color[1].equalsIgnoreCase("BLACK_BLUE")) {
                mTvDay.setBackgroundResource(R.drawable.shape_circle_white_blue_border);
                setSelectedDay(mTvDay);
            } else if (day_color[1].equalsIgnoreCase("BLACK_ORANGE") || day_color[1].equalsIgnoreCase("BLACK_YELLOW")) {
                mTvDay.setBackgroundResource(R.drawable.shape_circle_white_orange_border);
                setSelectedDay(mTvDay);
            } else if (day_color[1].equalsIgnoreCase("BLACK_WHITE")) {
                mTvDay.setBackgroundResource(R.drawable.shape_circle_white);
                setSelectedDay(mTvDay);
            } else if (day_color[1].equalsIgnoreCase("BLACK")) {
                mTvDay.setBackgroundResource(R.drawable.shape_circle_white);
                setSelectedDay(mTvDay);
            }

            mGridViewMonth.performClick();

            if (day_color[1].equalsIgnoreCase("RED") || day_color[1].equalsIgnoreCase("GREEN") || day_color[1].equalsIgnoreCase("ORANGE") || day_color[1].equalsIgnoreCase("YELLOW") || day_color[1].equalsIgnoreCase("BLUE")) {
                setDaysBackground(mTvDay, day_color[1]);
            }

            return row;
        }

        int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }

//    /**
//     * @param date ISO format 2016-12-13
//     */
    private void getAllMonthDataFromApi(String date, int month, int year) {

        if (date != null || !date.isEmpty()) {
            try {

                Log.d(LOG_TAG, "Setting Month in GridCellAdapter: " + "Month: "
                        + month + " Year: " + year);

                colorList = new ArrayList<>();


//                if (commonResponse.getData().size() > 0) {
//
//                    for (int i = 0; i < commonResponse.getData().size(); i++) {
//                        colorList.add(commonResponse.getData().get(i).getColor());
//                    }
//
//                } else {
//                    for (int i = 0; i < 31; i++) {
//                        colorList.add("WHITE");
//                    }
//                }
                for (int i = 0; i < 31; i++) {
                    colorList.add("WHITE");
                }

                setGridCellAdapterToDate(month, year, colorList);

            } catch (Exception e) {
                e.printStackTrace();
            }

//            WebApi mWebApi = RetrofitClient.createService(WebApi.class, context);
//            CallProgressWheel.showLoadingDialog(context, "Loading...");
//            HashMap<String, String> params = new HashMap<>();
//            params.put("monthFilterDate", date);
//            params.put("limit", "0");
//            params.put("skip", "0");
//
//            mWebApi.getScheduleSiteListMonth(params, new Callback<ScheduleWeekPojo>() {
//                @Override
//                public void success(ScheduleWeekPojo commonResponse, retrofit.client.Response response) {
//                    CallProgressWheel.dismissLoadingDialog();
//                    try {
//
//                        Log.d(LOG_TAG, "Setting Month in GridCellAdapter: " + "Month: "
//                                + month + " Year: " + year);
//
//                        colorList = new ArrayList<>();
//
//
//                        if (commonResponse.getData().size() > 0) {
//
//                            for (int i = 0; i < commonResponse.getData().size(); i++) {
//                                colorList.add(commonResponse.getData().get(i).getColor());
//                            }
//
//                        } else {
//                            for (int i = 0; i < 31; i++) {
//                                colorList.add("WHITE");
//                            }
//                        }
//
//                        setGridCellAdapterToDate(month, year, colorList);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    CallProgressWheel.dismissLoadingDialog();
//                }
//            });

        }
    }

    private void setDaysBackground(TextView textView, String backgroundColor) {
        if (textView != null) {
            textView.setTextColor(Color.WHITE);

            Drawable backgroundDrawable = null;

            if (backgroundColor.equalsIgnoreCase("RED")) {
                backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_red);
            } else if (backgroundColor.equalsIgnoreCase("ORANGE") || backgroundColor.equalsIgnoreCase("YELLOW")) {
                backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_orange);
            } else if (backgroundColor.equalsIgnoreCase("GREEN")) {
                backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_green);
            } else if (backgroundColor.equalsIgnoreCase("BLUE")) {
                backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_blue);
            }

            textView.setBackground(backgroundDrawable);

            Log.d(LOG_TAG, " textView" + textView.getText().
                    toString());
            Log.d(LOG_TAG, " currentMonth" + mTvCurrentMonth.getText().toString());
        }
    }


    private void setSelectedDay(TextView textView) {
        try {

            Drawable previousDrawable = textView.getBackground();

//            CommonMethods.myLog("setSelectedDay", "previousDrawable: " + previousDrawable);

            removeSelectedDay();
            if (textView != null) {

                textView.setTextColor(Color.BLACK);

                if (previousDrawable.getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.shape_circle_green).getConstantState())) {
                    textView.setBackgroundResource(R.drawable.shape_circle_white_green_border);
                } else if (previousDrawable.getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.shape_circle_red).getConstantState())) {
                    textView.setBackgroundResource(R.drawable.shape_circle_red_white);
                } else if (previousDrawable.getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.shape_circle_orange).getConstantState())) {
                    textView.setBackgroundResource(R.drawable.shape_circle_white_orange_border);
                } else if (previousDrawable.getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.shape_circle_blue).getConstantState())) {
                    textView.setBackgroundResource(R.drawable.shape_circle_white_blue_border);
                } else if (previousDrawable.getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.shape_circle_white_green_border).getConstantState())) {
                    textView.setBackgroundResource(R.drawable.shape_circle_white_green_border);
                } else if (previousDrawable.getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.shape_circle_red_white).getConstantState())) {
                    textView.setBackgroundResource(R.drawable.shape_circle_red_white);
                } else if (previousDrawable.getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.shape_circle_white_orange_border).getConstantState())) {
                    textView.setBackgroundResource(R.drawable.shape_circle_white_orange_border);
                } else if (previousDrawable.getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.shape_circle_white_blue_border).getConstantState())) {
                    textView.setBackgroundResource(R.drawable.shape_circle_white_blue_border);
                } else {
                    textView.setBackgroundResource(R.drawable.shape_circle_white);
                }
                mTvSelectedDayOld = textView;
                //
                Log.d(LOG_TAG, " textView" + textView.getText().
                        toString());
                Log.d(LOG_TAG, " currentMonth" + mTvCurrentMonth.getText().toString());
                selectedDate = textView.getText().toString() + " " + mTvCurrentMonth.getText().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeSelectedDay() {
        try {
            if (mTvSelectedDayOld != null) {
                mTvSelectedDayOld.setTextColor(Color.WHITE);

                if (colorList != null && colorList.size() > 0) {

                    try {
                        Drawable backgroundDrawable;

                        int index = Integer.parseInt(mTvSelectedDayOld.getText().toString());

                        String backgroundColor = colorList.get(index - 1);

                        if (backgroundColor.equalsIgnoreCase("RED")) {
                            backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_red);
                        } else if (backgroundColor.equalsIgnoreCase("ORANGE") || backgroundColor.equalsIgnoreCase("YELLOW")) {
                            backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_orange);
                        } else if (backgroundColor.equalsIgnoreCase("GREEN")) {
                            backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_green);
                        } else if (backgroundColor.equalsIgnoreCase("BLUE")) {
                            backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_blue);
                        } else {
                            backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_transparent);
                        }

                        mTvSelectedDayOld.setBackground(backgroundDrawable);

                    } catch (Exception e) {
                        e.printStackTrace();
                        mTvSelectedDayOld.setBackgroundResource(R.drawable.shape_circle_transparent);
                    }
                } else {
                    mTvSelectedDayOld.setBackgroundResource(R.drawable.shape_circle_transparent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSortedDate() {
        String date;
        String[] dateArray = selectedDate.split(Pattern.quote(" "));
        String day = Integer.parseInt(dateArray[0]) > 9 ? dateArray[0] + "" : "0" + dateArray[0];
        int monthIndex = getMonthIndex(dateArray[1].trim()
        );
        String monthNew = monthIndex > 9 ? monthIndex + "" : "0" + monthIndex;
        date = dateArray[2] + "-" + monthNew + "-" + day;
        return date;
    }

    private int getMonthIndex(String monthName) {
        monthName = monthName.trim().toLowerCase();
        int month = 1;
        switch (monthName) {
            case "jan":
                month = 1;
                break;
            case "feb":
                month = 2;
                break;
            case "mar":
                month = 3;
                break;
            case "apr":
                month = 4;
                break;
            case "may":
                month = 5;
                break;
            case "jun":
                month = 6;
                break;
            case "jul":
                month = 7;
                break;
            case "aug":
                month = 8;
                break;
            case "sep":
                month = 9;
                break;
            case "oct":
                month = 10;
                break;
            case "nov":
                month = 11;
                break;
            case "dec":
                month = 12;
                break;
        }
        return month;
    }

    public interface ClickCallback {
        void onDateClicked(String selectedDate_ISO, String selectedDate, TextView textView);

        void onCopyMonth();

        void onClearMonth();
    }

}

