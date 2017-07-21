package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewMyNewsItemBinding;
import kr.co.easterbunny.wonderple.library.ParentActivity;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadNewsResult;

import static kr.co.easterbunny.wonderple.library.util.Definitions.NEWS_TYPE.APPROVAL_FOLLOW;
import static kr.co.easterbunny.wonderple.library.util.Definitions.NEWS_TYPE.FOLLOWED;
import static kr.co.easterbunny.wonderple.library.util.Definitions.NEWS_TYPE.ITEM_TALK;
import static kr.co.easterbunny.wonderple.library.util.Definitions.NEWS_TYPE.LIKE;
import static kr.co.easterbunny.wonderple.library.util.Definitions.NEWS_TYPE.POST;
import static kr.co.easterbunny.wonderple.library.util.Definitions.NEWS_TYPE.WONDER;

/**
 * Created by scona on 2017-05-29.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.BindingHolder> {

    private Context context;
    private List<LoadNewsResult.News> newsList = new ArrayList<>();
    private String uid;
    private String followCheck;
    private String shareStatus;

    public NewsAdapter(Context context, List<LoadNewsResult.News> newsList, String uid) {
        this.context = context;
        this.newsList = newsList;
        this.uid = uid;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_my_news_item, parent, false);
        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        if ("X".equals(newsList.get(position).getCheckStatus())) {
            holder.binding.imgRefreshDot.setVisibility(View.GONE);
        }

        setNews(newsList.get(position).getType(), holder, position);
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder{

        private ViewMyNewsItemBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.setMyNewsItem(this);
        }

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_profile:
                case R.id.tv_username:
                    ((ParentActivity) context).movePageActivity(context, uid, newsList.get(getAdapterPosition()).getNewsUid());
                    break;
            }
        }
    }

    private void setNews(String newsType, final BindingHolder holder, final int position) {

        followCheck = newsList.get(position).getFollowCheck();
        shareStatus = newsList.get(position).getShareStatus();

        Glide.with(context)
                .load(newsList.get(position).getNewsProfileImage())
                .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                .into(holder.binding.imgProfile);

        if (newsType.equals(LIKE) || newsType.equals(POST) || newsType.equals(WONDER) || newsType.equals(ITEM_TALK)) {
            holder.binding.image.setVisibility(View.VISIBLE);
            holder.binding.btnFollow.setVisibility(View.GONE);

            Glide.with(context)
                    .load(newsList.get(position).getNewsImage())
                    .into(holder.binding.image);

            if (!newsType.equals(ITEM_TALK)) {
                holder.binding.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ParentActivity) context).movePostDetailActivity(context, uid, newsList.get(holder.getAdapterPosition()).getNewsUid(), newsList.get(holder.getAdapterPosition()).getNewsIid(), newsList.get(holder.getAdapterPosition()).getNewsImage());
                    }
                });
            }

            if (newsType.equals(LIKE)) {
                holder.binding.tvDesc.setText(context.getString(R.string.str_news_like));
            } else if (newsType.equals(POST)) {
                holder.binding.tvDesc.setText(context.getString(R.string.str_news_comment));
            } else if (newsType.equals(WONDER)) {
                holder.binding.tvDesc.setText(context.getString(R.string.str_news_wonder));
            } else if (newsType.equals(ITEM_TALK)) {
                holder.binding.tvDesc.setText(context.getString(R.string.str_news_item_talk));
            }


        } else if (newsType.equals(FOLLOWED) || newsType.equals(APPROVAL_FOLLOW)) {
            holder.binding.btnFollow.setVisibility(View.VISIBLE);
            holder.binding.image.setVisibility(View.INVISIBLE);

            setBtnFollow(holder, position);

            JSLog.D("follow check: " + position + "    " + newsList.get(position).getFollowCheck(), new Throwable());
            if (newsType.equals(FOLLOWED)) {
                holder.binding.tvDesc.setText(context.getString(R.string.str_news_followed));
            } else if (newsType.equals(APPROVAL_FOLLOW)) {
                holder.binding.tvDesc.setText(context.getString(R.string.str_news_approval_follow));
            }
        }

        holder.binding.tvUsername.setText(newsList.get(position).getNewsName());
        holder.binding.tvTime.setText(setDays(position));
    }

    private String setDays(final int position) {
        String days = "";

        if ("0".equals(newsList.get(position).getDays())) {
            days = context.getString(R.string.str_today);
        } else if (Integer.parseInt(newsList.get(position).getDays())>0 && Integer.parseInt(newsList.get(position).getDays())<7) {
            days = context.getString(R.string.str_before_days, newsList.get(position).getDays());
        } else if (Integer.parseInt(newsList.get(position).getDays())>=7 && Integer.parseInt(newsList.get(position).getDays())<30) {
            days = context.getString(R.string.str_before_weeks, String.valueOf(Integer.parseInt(newsList.get(position).getDays()) / 7));
        } else if (Integer.parseInt(newsList.get(position).getDays()) >= 30 && Integer.parseInt(newsList.get(position).getDays()) < 365) {
            days = context.getString(R.string.str_before_month, String.valueOf(Integer.parseInt(newsList.get(position).getDays()) / 30));
        } else {
            days = context.getString(R.string.str_before_years, String.valueOf(Integer.parseInt(newsList.get(position).getDays()) / 365));
        }

        return days;
    }

    public void add(List<LoadNewsResult.News> nextNews) {
        newsList.addAll(nextNews);
        notifyDataSetChanged();
    }

    private void setBtnFollow(final BindingHolder holder, final int position) {
        if ("0".equals(followCheck)) {
            holder.binding.btnFollow.setBackgroundResource(R.drawable.wonderple_following);
            holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unFollow(holder, holder.getAdapterPosition());
                }
            });
        } else if ("1".equals(followCheck)) {
            holder.binding.btnFollow.setBackgroundResource(R.drawable.wonderple_request);
            holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unFollow(holder, holder.getAdapterPosition());
                }
            });
        } else {
            if ("0".equals(shareStatus)) {
                holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.binding.btnFollow.setBackgroundResource(R.drawable.wonderple_following);
                        WonderpleLib.getInstance().follow(uid, newsList.get(holder.getAdapterPosition()).getNewsUid());
                        newsList.get(holder.getAdapterPosition()).setFollowCheck("0");
                        notifyDataSetChanged();
//                        notifyItemChanged(holder.getAdapterPosition());
                    }
                });
            } else {
                holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.binding.btnFollow.setBackgroundResource(R.drawable.wonderple_request);
                        WonderpleLib.getInstance().follow(uid, newsList.get(holder.getAdapterPosition()).getNewsUid());
                        newsList.get(holder.getAdapterPosition()).setFollowCheck("1");
                        notifyDataSetChanged();
//                        notifyItemChanged(holder.getAdapterPosition());
                    }
                });
            }
        }
    }

    private void unFollow(final BindingHolder holder, final int position) {
        holder.binding.btnFollow.setBackgroundResource(R.drawable.wonderple_follower);
        WonderpleLib.getInstance().follow(uid, newsList.get(holder.getAdapterPosition()).getNewsUid());
        newsList.get(position).setFollowCheck("X");
        notifyDataSetChanged();
//        notifyItemChanged(position);
    }
}
