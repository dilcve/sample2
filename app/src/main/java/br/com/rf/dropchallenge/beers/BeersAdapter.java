package br.com.rf.dropchallenge.beers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.rf.dropchallenge.R;
import br.com.rf.dropchallenge.model.Beer;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BeersAdapter extends RecyclerView.Adapter<BeersAdapter.ViewHolder> {

    private List<Beer> beers;
    private BeerItemListener mItemListener;

    public BeersAdapter(List<Beer> beers, BeerItemListener itemListener) {
        this.beers = new ArrayList<>(beers);
        this.mItemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Context context = holder.itemView.getContext();
        final Beer beer = beers.get(position);

        holder.mTextBeerName.setText(beer.name);
        holder.mTextBeerAbv.setText(beer.getAbv());

        if (!TextUtils.isEmpty(beer.image_url)) {
            Glide.with(context)
                    .load(beer.image_url)
                    .dontAnimate()
                    .into(holder.mImgBeer);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onBeerClick(beer);
            }
        });

        holder.itemView.setClickable(true);
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_beer_img)
        ImageView mImgBeer;
        @BindView(R.id.item_beer_name)
        TextView mTextBeerName;
        @BindView(R.id.item_beer_abv)
        TextView mTextBeerAbv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_beer;
    }

    public List<Beer> getBeerList() {
        return beers;
    }

    public void appendItems(List<Beer> items) {
        int count = getItemCount();
        beers.addAll(items);
        notifyItemRangeInserted(count, items.size());
    }

    public void removeAnItem(int position) {
        beers.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        beers.clear();
    }

    public interface BeerItemListener {

        void onBeerClick(Beer clickedBeer);

    }
}
