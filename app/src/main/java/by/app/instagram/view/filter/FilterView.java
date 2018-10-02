package by.app.instagram.view.filter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import by.app.instagram.R;

public class FilterView extends RelativeLayout implements IFilterView.View {

    private static String TAG = FilterView.class.getName();

    IFilterView.Click cLick;

    public interface FilterViewClickListener {
        public void clickApply(View v);
        public void clickCancel(View v);
    }

    private FilterViewClickListener clickListener;
    private TypeFilter typeFilter;
    private TypeSpinnerFilter typeSpinnerFilter;

    private Spinner spinner;
    public CardView card_apply, card_cancel, card;
    LinearLayout linear_select_month;
    RelativeLayout rel_period_1, rel_period_2;
    TextView tv_period_1, tv_period_2;
    EditText et_counts_posts;

    private int count_post = 100;
    Long period_1, period_2;
    Calendar dateAndTime;
    SimpleDateFormat df;


    int DIALOG_DATE = 1;
    int myYear = 2018;
    int myMonth = 06;
    int myDay = 31;

    DatePickerDialog tpd;

    public FilterView(Context context, IFilterView.Click click){
        super(context);
        init();
        this.cLick = click;
    }

    public FilterView(Context context) {
        super(context);
        init();
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        inflate(getContext(), R.layout.filter, this);
        et_counts_posts = (EditText) findViewById(R.id.et_counts_posts);
        tv_period_1 = (TextView) findViewById(R.id.tv_period_1);
        tv_period_2 = (TextView) findViewById(R.id.tv_period_2);
        rel_period_1 = (RelativeLayout) findViewById(R.id.rel_period_1);
        rel_period_2 = (RelativeLayout) findViewById(R.id.rel_period_2);
        linear_select_month = (LinearLayout) findViewById(R.id.filter_linear_select_month);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        this.card_apply = (CardView) findViewById(R.id.card_apply);
        this.card_cancel = (CardView) findViewById(R.id.card_cancel);
        this.card = (CardView) findViewById(R.id.card);
        initSpinnerDefault();
        initClickListener();
        initEtListener();
        initTvCurrentDate();
    }

    @Override
    public void initSpinnerDefault() {
        try {
            String[] spinArr = new String[]{
                getResources().getString(R.string.all_months),
                getResources().getString(R.string.current_month),
                getResources().getString(R.string.select_months),
                getResources().getString(R.string.count_posts)
            };

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, spinArr);

            spinner.setAdapter(adapter);
            typeSpinnerFilter = TypeSpinnerFilter.All;

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i){
                        case 0:
                            typeSpinnerFilter = TypeSpinnerFilter.All;
                            break;
                        case 1:
                            typeSpinnerFilter = TypeSpinnerFilter.CurrentMonth;
                            break;
                        case 2:
                            typeSpinnerFilter = TypeSpinnerFilter.SelectMonth;
                            break;
                        case 3:
                            typeSpinnerFilter = TypeSpinnerFilter.CountPosts;
                            break;
                    }
                    Log.e(TAG, "select spinner = "+ typeSpinnerFilter.toString());
                    updateContentFilter();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            String d ="";
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initSpinnerPosts() {
        try {
            String[] spinArr = new String[]{
                    getResources().getString(R.string.all_months),
                    getResources().getString(R.string.current_month),
                    getResources().getString(R.string.select_months)
            };

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, spinArr);

            spinner.setAdapter(adapter);
            typeSpinnerFilter = TypeSpinnerFilter.All;

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i){
                        case 0:
                            typeSpinnerFilter = TypeSpinnerFilter.All;
                            break;
                        case 1:
                            typeSpinnerFilter = TypeSpinnerFilter.CurrentMonth;
                            break;
                        case 2:
                            typeSpinnerFilter = TypeSpinnerFilter.SelectMonth;
                            break;
                    }
                    Log.e(TAG, "select spinner = "+ typeSpinnerFilter.toString());
                    updateContentFilter();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            String d ="";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initSpinnerHashtags() {
        try {
            String[] spinArr = new String[]{
                    getResources().getString(R.string.all_months),
                    getResources().getString(R.string.current_month),
                    getResources().getString(R.string.select_months),
                    getResources().getString(R.string.count_posts)
            };

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, spinArr);

            spinner.setAdapter(adapter);
            typeSpinnerFilter = TypeSpinnerFilter.All;

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i){
                        case 0:
                            typeSpinnerFilter = TypeSpinnerFilter.All;
                            break;
                        case 1:
                            typeSpinnerFilter = TypeSpinnerFilter.CurrentMonth;
                            break;
                        case 2:
                            typeSpinnerFilter = TypeSpinnerFilter.SelectMonth;
                            break;
                        case 3:
                            typeSpinnerFilter = TypeSpinnerFilter.CountPosts;
                            break;
                    }
                    Log.e(TAG, "select spinner = "+ typeSpinnerFilter.toString());
                    updateContentFilter();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            String d ="";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void initClickListener() {
        rel_period_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tpd = new DatePickerDialog(getContext(), myCallBackPeriod1, myYear, myMonth, myDay);
                tpd.getDatePicker().setMaxDate(new Date().getTime());
                tpd.show();
            }
        });

        rel_period_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tpd = new DatePickerDialog(getContext(), myCallBackPeriod2, myYear, myMonth, myDay);
                tpd.getDatePicker().setMaxDate(new Date().getTime());
                tpd.show();
            }
        });
    }

    @Override
    public void initEtListener() {

        et_counts_posts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    int col = Integer.parseInt(editable.toString());
                    count_post = col;
                    Log.e(TAG, "count post = "+ String.valueOf(col));
                }
                catch (Exception e){

                }
            }
        });

    }

    @Override
    public void initTvCurrentDate() {
        dateAndTime = Calendar.getInstance();
        period_2 = dateAndTime.getTimeInMillis();
        df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        tv_period_2.setText(df.format(period_2));
        period_1 = dateAndTime.getTimeInMillis();
        try{
            myYear = dateAndTime.get(Calendar.YEAR);
            myMonth = dateAndTime.get(Calendar.MONTH);
            myDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
        }
        catch (Exception e){}

        Log.e(TAG, "period 2 = "+String.valueOf(period_1));
        Log.e(TAG, "period 2 = "+ df.format(period_1));
    }

    @Override
    public void showFilter() {
        YoYo.with(Techniques.SlideInDown).duration(1000).playOn(card);
    }

    @Override
    public void hideFilter() {
        YoYo.with(Techniques.SlideOutUp).duration(1000).playOn(card);
    }

    @Override
    public void animClick() {

    }

    @Override
    public void updateFilter() {
        if(typeFilter == TypeFilter.Feed) initSpinnerDefault();
        else if(typeFilter == TypeFilter.Posts) initSpinnerPosts();
        else if(typeFilter == TypeFilter.Auditory) initSpinnerPosts();
        else if(typeFilter == TypeFilter.Hashtags) initSpinnerHashtags();
    }

    @Override
    public void updateContentFilter() {
        if(typeSpinnerFilter == TypeSpinnerFilter.All ||
                typeSpinnerFilter == TypeSpinnerFilter.CurrentMonth) {
            hideInpunCount();
            hideSelectDate();
        }else if(typeSpinnerFilter == TypeSpinnerFilter.SelectMonth){
            hideInpunCount();
            showSelectDate();
        }else if(typeSpinnerFilter == TypeSpinnerFilter.CountPosts){
            hideSelectDate();
            showInputCount();
        }
    }

    @Override
    public void showSelectDate() {
        linear_select_month.setVisibility(VISIBLE);
    }

    @Override
    public void hideSelectDate() {
        linear_select_month.setVisibility(GONE);
    }

    @Override
    public void showInputCount() {
        et_counts_posts.setVisibility(VISIBLE);
    }

    @Override
    public void hideInpunCount() {
        et_counts_posts.setVisibility(GONE);
    }

    @Override
    public void setTypeFilter(TypeFilter _typeFilter) {
        typeFilter = _typeFilter;
        updateFilter();
    }

    @Override
    public void setTypeSpinnerFilter(TypeSpinnerFilter _typeSpinnerFilter) {
        typeSpinnerFilter = _typeSpinnerFilter;
        updateContentFilter();
    }

    @Override
    public TypeSpinnerFilter getTypeSpinnerFilter() {
        return typeSpinnerFilter;
    }

    @Override
    public int getCountPosts() {
        return count_post;
    }

    @Override
    public long getPeriod1() {
        return period_1;
    }

    @Override
    public long getPeriod2() {
        return period_2;
    }


    DatePickerDialog.OnDateSetListener myCallBackPeriod1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            dateAndTime.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            period_1 = dateAndTime.getTimeInMillis();
            tv_period_1.setText(df.format(period_1));

            Log.e(TAG, "period 1 = "+String.valueOf(period_1));
            Log.e(TAG, "period 1 = "+ df.format(period_1));

        }
    };

    DatePickerDialog.OnDateSetListener myCallBackPeriod2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            dateAndTime.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            period_2 = dateAndTime.getTimeInMillis();
            tv_period_2.setText(df.format(period_2));

            Log.e(TAG, "period 2 = "+String.valueOf(period_2));
            Log.e(TAG, "period 2 = "+ df.format(period_2));
        }
    };

}
