package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewMentionUserItemBinding;
import kr.co.easterbunny.wonderple.mention.StringUtils;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadMentionUserListResult;

/**
 * Adapter to the mentions list shown to display the result of an '@' mention.
 */
public class UsersAdapter extends RecyclerArrayAdapter<LoadMentionUserListResult.Users, UsersAdapter.UserViewHolder> {

    /**
     * {@link Context}.
     */
    private final Context context;

    /**
     * Current search string typed by the user.  It is used highlight the query in the
     * search results.  Ex: @bill.
     */
    private String currentQuery;

    /**
     * {@link ForegroundColorSpan}.
     */
    private final ForegroundColorSpan colorSpan;

    public UsersAdapter(final Context context) {
        this.context = context;
        final int orange = ContextCompat.getColor(context, R.color.link);
        this.colorSpan = new ForegroundColorSpan(orange);
    }

    /**
     * Setter for what user has queried.
     */
    public void setCurrentQuery(final String currentQuery) {
        if (StringUtils.isNotBlank(currentQuery)) {
            this.currentQuery = currentQuery.toLowerCase(Locale.US);
        }
    }

    /**
     * Create UI with views for user name and picture.
     */
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.view_mention_user_item, parent, false);
        return new UserViewHolder(view);
    }

    /**
     * Display user name and picture.
     */
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final LoadMentionUserListResult.Users mentionsUser = getItem(position);

        if(mentionsUser != null && StringUtils.isNotBlank(mentionsUser.getName())) {
            holder.binding.tvUsername.setText(mentionsUser.getName(), TextView.BufferType.SPANNABLE);
            highlightSearchQueryInUserName(holder.binding.tvUsername.getText());
            if (StringUtils.isNotBlank(mentionsUser.getProfileImg())) {
                holder.binding.imgProfile.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(mentionsUser.getProfileImg())
                        .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                        .into(holder.binding.imgProfile);
            } else {
                holder.binding.imgProfile.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Highlights the current search text in the mentions list.
     */
    private void highlightSearchQueryInUserName(CharSequence userName) {
        if (StringUtils.isNotBlank(currentQuery)) {
            int searchQueryLocation = userName.toString().toLowerCase(Locale.US).indexOf(currentQuery);

            if (searchQueryLocation != -1) {
                Spannable userNameSpannable = (Spannable) userName;
                userNameSpannable.setSpan(
                        colorSpan,
                        searchQueryLocation,
                        searchQueryLocation + currentQuery.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    /**
     * View holder for user.
     */
    static class UserViewHolder extends RecyclerView.ViewHolder {

        private ViewMentionUserItemBinding binding;

        UserViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
