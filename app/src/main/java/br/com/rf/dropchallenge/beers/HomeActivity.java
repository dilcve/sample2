package br.com.rf.dropchallenge.beers;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.rf.dropchallenge.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

    }
}
