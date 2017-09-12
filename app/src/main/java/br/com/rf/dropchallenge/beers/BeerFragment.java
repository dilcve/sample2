package br.com.rf.dropchallenge.beers;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.rf.dropchallenge.R;
import br.com.rf.dropchallenge.beerdetail.BeerDetailActivity;
import br.com.rf.dropchallenge.model.Beer;
import br.com.rf.dropchallenge.source.BeersDataSource;
import br.com.rf.dropchallenge.source.BeersRepository;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BeerFragment extends Fragment {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    BeersAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @BindView(R.id.beers_loading)
    View mLoadingView;
    @BindView(R.id.beers_no_data)
    View mNoDataView;
    @BindView(R.id.beers_no_data_msg)
    TextView mNoDataMsg;

    EndlessRecyclerViewAdapter mEndlessRecyclerViewAdapter;

    private List<Beer> mBeers = new ArrayList<>();

    BeersRepository mBeersRepository;

    private static final String ARG_BEER_TYPE = "beer_type";
    private int mBeerType;

    public BeerFragment() {
        // Required empty public constructor
    }

    public static BeerFragment newInstance(int beerType) {
        BeerFragment fragment = new BeerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BEER_TYPE, beerType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBeerType = getArguments().getInt(ARG_BEER_TYPE, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beers, container, false);
        ButterKnife.bind(this, view);

        mBeersRepository = BeersRepository.provideBeersRepository(getContext());

        getBeers(mBeerType);

        return view;
    }

    public void loadList(List<Beer> beers) {
        setLoadingIndicator(false);
        mAdapter = new BeersAdapter(beers, mItemListener);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mEndlessRecyclerViewAdapter = new EndlessRecyclerViewAdapter(mAdapter, new EndlessRecyclerViewAdapter.RequestToLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mBeerType == Beer.TYPE_RANDOM) {
                    for (int i = 0; i < 9 ; i++) {
                        getMoreBeers(mBeerType);
                    }
                }else{
                    getMoreBeers(mBeerType);
                }
            }
        });

        mRecyclerView.setAdapter(mEndlessRecyclerViewAdapter);
    }

    public void showLoadingMoreBeersError() {
        mEndlessRecyclerViewAdapter.onDataReady(true);
        Toast.makeText(getContext(), R.string.lbl_load_more_error, Toast.LENGTH_SHORT).show();
    }

    public void getBeers(int type) {
        setLoadingIndicator(true);

        mBeersRepository.getBeers(type, new BeersDataSource.LoadBeersCallback() {
            @Override
            public void onBeersLoaded(List<Beer> beers) {
                loadList(beers);
            }

            @Override
            public void onDataNotAvailable() {
                showNoResult();
            }
        });
    }

    public void getMoreBeers(int type) {
        mBeersRepository.getMoreBeers(type, new BeersDataSource.LoadBeersCallback() {
            @Override
            public void onBeersLoaded(List<Beer> beers) {
                mAdapter.appendItems(beers);
                mEndlessRecyclerViewAdapter.onDataReady(true);
            }

            @Override
            public void onDataNotAvailable() {
                mEndlessRecyclerViewAdapter.onDataReady(false);
            }
        });
    }

    public void hideNoResultView() {
        mNoDataView.setVisibility(View.GONE);
    }

    public void showNoResult() {
        mNoDataMsg.setText(R.string.lbl_no_data_available);
        mNoDataView.setVisibility(View.VISIBLE);
    }

    public void setLoadingIndicator(boolean active) {
        hideNoResultView();
        if (active) {
            mLoadingView.setVisibility(View.VISIBLE);
        } else {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    public void openBeerDetails(Beer beer) {
        Intent intent = new Intent(getContext(), BeerDetailActivity.class);
        intent.putExtra(BeerDetailActivity.EXTRA_BEER, beer);
        startActivity(intent);
    }

    /**
     * Listener for clicks on users in the RecyclerView.
     */
    BeersAdapter.BeerItemListener mItemListener = new BeersAdapter.BeerItemListener() {

        @Override
        public void onBeerClick(Beer clickedBeer) {
            openBeerDetails(clickedBeer);
        }

    };

}
