package br.com.rf.dropchallenge.beerdetail;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.rf.dropchallenge.R;
import br.com.rf.dropchallenge.model.Beer;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BeerDetailActivity extends AppCompatActivity {

    @BindView(R.id.beer_detail_name)
    TextView mTextName;
    @BindView(R.id.beer_detail_abv)
    TextView mTextAbv;
    @BindView(R.id.beer_detail_description)
    TextView mTextDescription;
    @BindView(R.id.beer_detail_hope)
    TextView mTextHope;
    @BindView(R.id.beer_detail_ingredients)
    TextView mTextIngredients;
    @BindView(R.id.beer_detail_method)
    TextView mTextMethod;
    @BindView(R.id.beer_detail_img)
    ImageView mImgBeer;
    @BindView(R.id.beer_detail_layout_hope)
    LinearLayout mHopeLayout;
    @BindView(R.id.beer_detail_layout_ingredients)
    LinearLayout mIngredientsLayout;
    @BindView(R.id.beer_detail_duration)
    TextView mTextDuration;
    @BindView(R.id.beer_detail_btn_start_pause)
    Button mBtnStartPause;
    @BindView(R.id.beer_detail_btn_cancel)
    Button mBtnCancel;

    @BindView(R.id.toolbar)
    Toolbar mTollbar;

    Integer mDuration;
    long mCurrentCountDown = -1;

    android.os.CountDownTimer mCountDownTimer;

    public static final String EXTRA_BEER = "EXTRA_BEER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mTollbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Beer Detail");

        Beer beer = (Beer) getIntent().getSerializableExtra(EXTRA_BEER);

        showBeerDetails(beer);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showBeerDetails(@NonNull Beer beer) {

        mTextName.setText(getString(R.string.lbl_name, beer.name));
        mTextAbv.setText(getString(R.string.lbl_abv, beer.abv));
        mTextDescription.setText(getString(R.string.lbl_description, beer.description));
        if (!TextUtils.isEmpty(beer.image_url)) {
            Glide.with(this)
                    .load(beer.image_url)
                    .into(mImgBeer);
        }
        if (beer.ingredients != null) {
            if (beer.ingredients.hops != null && beer.ingredients.hops.size() > 0) {
                for (Beer.Ingredients.Hops hop : beer.ingredients.hops) {
                    TextView textView = new TextView(this);
                    textView.setText(hop.getName() + " " + hop.amount.getValue() + " " + hop.amount.getUnit());
                    mHopeLayout.addView(textView);
                }
            }
            if (beer.ingredients.malt != null && beer.ingredients.malt.size() > 0) {
                for (Beer.Ingredients.Malt malt : beer.ingredients.malt) {
                    TextView textView = new TextView(this);
                    textView.setText(malt.getName() + " " + malt.amount.getValue() + " " + malt.amount.getUnit());
                    mIngredientsLayout.addView(textView);
                }
            }
        }
        if (beer.method != null && beer.method.mash_temp != null && beer.method.mash_temp.size() > 0 && beer.method.mash_temp.get(0).duration != null) {
            mDuration = beer.method.mash_temp.get(0).duration;
            mCurrentCountDown = mDuration * 1000;
            mTextDuration.setText(getString(R.string.lbl_duration, mDuration.toString()));
            mTextDuration.setVisibility(View.VISIBLE);
            mBtnStartPause.setVisibility(View.VISIBLE);
        }
    }

    public void startCountDown(long time) {
        mBtnCancel.setVisibility(View.VISIBLE);
        mCountDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinishedl) {
                mCurrentCountDown = millisUntilFinishedl;
                int timeToFinish = (int) millisUntilFinishedl / 1000;
                mTextDuration.setText(getString(R.string.lbl_duration, "" + timeToFinish));
            }

            @Override
            public void onFinish() {
                mTextDuration.setText(getString(R.string.lbl_duration, "0"));
                mCurrentCountDown = -1;
            }
        }.start();

    }

    public void pauseCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    public void cancelCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
            mCurrentCountDown = mDuration * 1000;
        }
    }

    @OnClick(R.id.beer_detail_btn_start_pause)
    public void onStartPauseClicked() {
        if (mBtnStartPause.getText().toString().equalsIgnoreCase("start")) {
            mBtnStartPause.setText("Pause");
            startCountDown(mCurrentCountDown);
        } else if (mBtnStartPause.getText().toString().equalsIgnoreCase("resume")) {
            mBtnStartPause.setText("Pause");
            startCountDown(mCurrentCountDown);
        } else {
            mBtnStartPause.setText("Resume");
            pauseCountDown();
        }
    }

    @OnClick(R.id.beer_detail_btn_cancel)
    public void onCancelClicked(){
        cancelCountDown();
        mTextDuration.setText(getString(R.string.lbl_duration, mDuration.toString()));
        mBtnCancel.setVisibility(View.GONE);
        mBtnStartPause.setText("Start");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
