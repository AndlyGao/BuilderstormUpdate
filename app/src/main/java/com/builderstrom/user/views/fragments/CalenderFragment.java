package com.builderstrom.user.views.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.views.adapters.ScheduleMonthAdapter;

public class CalenderFragment extends BaseRecyclerViewFragment {
    private RecyclerView.Adapter adapter;

    @Override protected void pagination() {

    }

    @Override protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override protected RecyclerView.Adapter getRecyclerAdapter() {
        return null;
    }

    @Override protected void setData() {

    }

    @Override protected void pullDownToRefresh() {

    }

    @Override protected int getLayoutId() {
        return R.layout.fragment_calender;
    }

    @Override protected void bindView(View view) {
        if (recyclerView != null) {
            adapter=new ScheduleMonthAdapter(getContext(), CommonMethods.getCurrentDate(CommonMethods.DF_1), new ScheduleMonthAdapter.ClickCallback() {
                @Override public void onDateClicked(String selectedDate_ISO, String selectedDate, TextView textView) {

                }

                @Override public void onCopyMonth() {

                }

                @Override public void onClearMonth() {

                }
            });
            recyclerView.setAdapter(adapter);
        }
    }
}
