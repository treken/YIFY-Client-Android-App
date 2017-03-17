package yts.mnf.com.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import yts.mnf.com.Activity.DetailsActivity;
import yts.mnf.com.Fragment.DetailFragment;
import yts.mnf.com.Models.Movie;
import yts.mnf.com.Models.Torrent;
import yts.mnf.com.R;
import yts.mnf.com.Tools.Config;
import yts.mnf.com.Tools.DetailsTransition;

import static android.R.attr.bitmap;
import static android.R.attr.mode;

/**
 * Created by muneef on 22/01/17.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {


    private Context mContext;
    private List<Movie> mModels;
    FragmentManager fm;

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public RecycleAdapter(Context mContext, List<Movie> models, FragmentManager fm) {
        this.mContext = mContext;
        this.mModels = models;
        this.fm = fm;
    }
    public void replaceItems(List<Movie> models){
        this.mModels = models;
        notifyDataSetChanged();
    }
    public void addItems(List<Movie> models){
        this.mModels.addAll(models);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView movieTitle, movieYear,tvRating;
        public ImageView moviePoster, overflow;
        public RelativeLayout relativeLayout;
        public CardView cv;
       // TagView tagGroup;
        public ViewHolder(View view) {
            super(view);
            movieTitle = (TextView) view.findViewById(R.id.movie_title);
            moviePoster = (ImageView) view.findViewById(R.id.poster);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.name_relative);
            cv = (CardView) view.findViewById(R.id.card_view);
            movieYear = (TextView) view.findViewById(R.id.movie_year);
            tvRating = (TextView) view.findViewById(R.id.rate_tv_adapter);

            //  tagGroup = (TagView) view.findViewById(R.id.tag_group);

            // count = (TextView) view.findViewById(R.id.count);
            //thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
          //  overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Movie movie = mModels.get(position);

        holder.movieTitle.setText(movie.getTitle());
        if(movie.getYear()!=null){
            holder.movieYear.setText(movie.getYear().toString());
        }
        holder.tvRating.setText(movie.getRating()+"");

        //holder.tagGroup.removeAll();
        Glide.clear(holder.moviePoster);


        if (movie.getMediumCoverImage() != null) { // simulate an optional url from the data item
            holder.moviePoster.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(movie.getMediumCoverImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.moviePoster);
            Glide
                    .with(mContext)
                    .load(movie.getMediumCoverImage())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    // .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))

                    .into(new SimpleTarget<Bitmap>(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            //holder.moviePoster.setImageBitmap(resource); // Possibly runOnUiThread()
                            if(holder.relativeLayout!=null){
                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                    public void onGenerated(Palette p) {
                                        // Use generated instance
                                        holder.relativeLayout.setBackgroundColor(p.getDarkVibrantColor(mContext.getResources().getColor(R.color.grey700)));
                                        //holder.tvRating.setTextColor(Config.manipulateColor(p.getDarkVibrantColor(mContext.getResources().getColor(R.color.white)),0.9f));
                                    }
                                });
                            }
                        }
                    });

        } else {
            // clear when no image is shown, don't use holder.imageView.setImageDrawable(null) to do the same
            Glide.clear(holder.moviePoster);
            holder.moviePoster.setVisibility(View.GONE);
        }
       /* Glide
                .with(mContext)
                .load(movie.getMediumCoverImage())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
               // .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))

                .into(new SimpleTarget<Bitmap>(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        holder.moviePoster.setImageBitmap(resource); // Possibly runOnUiThread()
                        if(holder.relativeLayout!=null){
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette p) {
                                    // Use generated instance
                                    holder.relativeLayout.setBackgroundColor(p.getVibrantColor(mContext.getResources().getColor(R.color.grey700)));
                                }
                            });
                        }
                    }
                });*/
       /* for (Torrent torr : movie.getTorrents() ) {
            Tag tag = new Tag(torr.getQuality().toString());
            tag.layoutColor = mContext.getResources().getColor(R.color.grey800);
            tag.tagTextColor = mContext.getResources().getColor(R.color.grey600);
            tag.tagTextSize =10;
            holder.tagGroup.addTag(tag);
            Log.e("ADapter","for loop   "+torr.getQuality().toString());

        }*/
       //Config.loadImage(holder.moviePoster,movie.getMediumCoverImage(),holder.relativeLayout);
       /* Picasso.with(mContext)
                .load(movie.getMediumCoverImage())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                /* Save the bitmap or do something with it here */
                        //Set it in the ImageView
              /*          Log.e("ADapter","succes loading image bitmap ");



                        holder.moviePoster.setImageBitmap(bitmap);
                        if(holder.relativeLayout!=null){
                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette p) {
                                    // Use generated instance
                                    holder.relativeLayout.setBackgroundColor(p.getVibrantColor(mContext.getResources().getColor(R.color.grey700)));
                                }
                            });
                        }
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.e("Adapter","failed loading image bitmap ");


                    }
                }); */

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* ViewCompat.setTransitionName(holder.moviePoster, "poster_n");

                DetailFragment kittenDetails = DetailFragment.newInstance("","");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    kittenDetails.setSharedElementEnterTransition(new DetailsTransition());
                    kittenDetails.setEnterTransition(new Fade());
                    kittenDetails.setExitTransition(new Fade());
                    kittenDetails.setSharedElementReturnTransition(new DetailsTransition());
                }


                fm.beginTransaction()
                        .addSharedElement(holder.moviePoster, "poster_n")
                        .replace(R.id.content_main, DetailFragment.newInstance("","")).commit();
            }*/
                Gson gS = new Gson();
                String movieJson = gS.toJson(movie);
                Intent detailAct = new Intent(mContext, DetailsActivity.class);
                detailAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Pair<View, String> p1 = Pair.create((View)holder.moviePoster, "poster");
                Pair<View, String> p2 = Pair.create((View)holder.movieTitle, "title");
                Pair<View, String> p3 = Pair.create((View)holder.tvRating, "rating");


                detailAct.putExtra("movie_json", movieJson);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, p1,p2,p3);
                mContext.startActivity(detailAct,options.toBundle());

            }
        });



    }


}
