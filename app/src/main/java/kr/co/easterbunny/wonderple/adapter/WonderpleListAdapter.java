package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewWonderpleListItemBinding;
import kr.co.easterbunny.wonderple.library.WonderpleLib;
import kr.co.easterbunny.wonderple.library.util.Definitions;
import kr.co.easterbunny.wonderple.library.util.NetworkUtil;
import kr.co.easterbunny.wonderple.library.util.ResourceUtils;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.LoadWonderResult;
import kr.co.easterbunny.wonderple.model.LoadWonderpleResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by scona on 2017-04-06.
 */

public class WonderpleListAdapter extends RecyclerView.Adapter<WonderpleListAdapter.BindingHolder> {

    private Context context;
    private List<LoadWonderpleResult.Wonderple> wonderples = new ArrayList<>();
    private List<LoadWonderResult.Wonder> wonders = new ArrayList<>();

    private String uid;
    private String listType;

    public class BindingHolder extends RecyclerView.ViewHolder  {
        private final ViewWonderpleListItemBinding binding;

        public BindingHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

    }

    public WonderpleListAdapter(Context context, List<LoadWonderpleResult.Wonderple> wonderples, String uid, String listType) {
        this.context = context;
        this.wonderples = wonderples;
        this.uid = uid;
        this.listType = listType;
    }

    public WonderpleListAdapter(Context context, String uid, List<LoadWonderResult.Wonder> wonders, String listType) {
        this.context = context;
        this.wonders = wonders;
        this.uid = uid;
        this.listType = listType;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_wonderple_list_item, parent, false);
        return new BindingHolder(view);
    }

    String profileImagePath = "";
    String username = "";
    String realname = "";
    String followCheck = "";
    String shareStatus = "";
    String auid = "";
    String category = "";

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {

        if (listType.equals(Definitions.LIST_TYPE.WONDERPLE)) {
            profileImagePath = wonderples.get(position).getProfileImage();
            username = wonderples.get(position).getName();
            realname = wonderples.get(position).getRealName();
            followCheck = wonderples.get(position).getFollowCheck();
            shareStatus = wonderples.get(position).getShareStatus();
            auid = wonderples.get(position).getUid();
        } else if (listType.equals(Definitions.LIST_TYPE.WONDER)) {
            profileImagePath = wonders.get(position).getProfileImage();
            username = wonders.get(position).getName();
            realname = wonders.get(position).getRealName();
            followCheck = wonders.get(position).getFollowCheck();
            shareStatus = wonders.get(position).getShareStatus();
            auid = wonders.get(position).getUid();
            category = wonders.get(position).getCategoryText();
        }

        if (!"".equals(profileImagePath)) {
            Glide.with(context)
                    .load(profileImagePath)
                    .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                    .into(holder.binding.imgProfile);
        }

        holder.binding.tvUsername.setText(username);

        if (!"".equals(realname)) {
            holder.binding.tvRealname.setVisibility(View.VISIBLE);
            holder.binding.tvRealname.setText("(" + realname + ")");
        }

        setBtnFollow(holder, position);

        if (!"".equals(category)) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.tvUsername.getLayoutParams();
//            ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//            newParams.setMargins(params.leftMargin, ResourceUtils.dpToPx(6), params.rightMargin, params.bottomMargin);
//            holder.binding.tvUsername.setLayoutParams(newParams);

            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.binding.tvUsername.getLayoutParams();
            marginLayoutParams.setMargins(params.leftMargin, ResourceUtils.dpToPx(2), params.rightMargin, 0);

            holder.binding.tvUsername.setLayoutParams(marginLayoutParams);

            holder.binding.tvCategory.setVisibility(View.VISIBLE);
            holder.binding.tvCategory.setText(category);
        }

    }

    @Override
    public int getItemCount() {
        if (listType.equals(Definitions.LIST_TYPE.WONDERPLE)) {
            if (wonderples != null) {
                return wonderples.size();
            }
        } else if (listType.equals(Definitions.LIST_TYPE.WONDER)) {
            if (wonders != null) {
                return wonders.size();
            }
        }
        return 0;
    }

    public void addWonderple(List<LoadWonderpleResult.Wonderple> nextWonderples) {
        wonderples.addAll(nextWonderples);
        notifyDataSetChanged();
    }

    public void addWonder(List<LoadWonderResult.Wonder> nextWonders) {
        wonders.addAll(nextWonders);
        notifyDataSetChanged();
    }

    private void setBtnFollow(BindingHolder holder, int position) {
        if (uid.equals(auid)) {
            holder.binding.btnFollow.setVisibility(View.GONE);
        }

        if ("0".equals(followCheck)) {
            holder.binding.btnFollow.setBackgroundResource(R.drawable.wonderple_following);
            holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.binding.btnFollow.setBackgroundResource(R.drawable.wonderple_follower);
                    if (listType.equals(Definitions.LIST_TYPE.WONDERPLE)) {
                        WonderpleLib.getInstance().follow(uid, wonderples.get(holder.getAdapterPosition()).getUid());
                        wonderples.get(position).setFollowCheck("X");
                    } else if (listType.equals(Definitions.LIST_TYPE.WONDER)) {
                        WonderpleLib.getInstance().follow(uid, wonders.get(holder.getAdapterPosition()).getUid());
                        wonders.get(position).setFollowCheck("X");
                    }
                    notifyDataSetChanged();
                }
            });
        } else if ("1".equals(followCheck)) {
            holder.binding.btnFollow.setBackgroundResource(R.drawable.wonderple_request);
            holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.binding.btnFollow.setBackgroundResource(R.drawable.wonderple_follower);
                    if (listType.equals(Definitions.LIST_TYPE.WONDERPLE)) {
                        WonderpleLib.getInstance().follow(uid, wonderples.get(holder.getAdapterPosition()).getUid());
                        wonderples.get(position).setFollowCheck("X");
                    } else if (listType.equals(Definitions.LIST_TYPE.WONDER)) {
                        WonderpleLib.getInstance().follow(uid, wonderples.get(holder.getAdapterPosition()).getUid());
                        wonders.get(position).setFollowCheck("X");
                    }
                    notifyDataSetChanged();
                }
            });
        } else {
            if ("0".equals(shareStatus)) {
                holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.binding.btnFollow.setBackgroundResource(R.drawable.wonderple_following);
                        if (listType.equals(Definitions.LIST_TYPE.WONDERPLE)) {
                            WonderpleLib.getInstance().follow(uid, wonderples.get(holder.getAdapterPosition()).getUid());
                            wonderples.get(position).setFollowCheck("0");
                        } else if (listType.equals(Definitions.LIST_TYPE.WONDER)) {
                            WonderpleLib.getInstance().follow(uid, wonders.get(holder.getAdapterPosition()).getUid());
                            wonders.get(position).setFollowCheck("0");
                        }
                        notifyDataSetChanged();
                    }
                });
            } else if ("1".equals(shareStatus)  || "2".equals(shareStatus) || "3".equals(shareStatus)) {
                holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.binding.btnFollow.setBackgroundResource(R.drawable.wonderple_request);
                        if (listType.equals(Definitions.LIST_TYPE.WONDERPLE)) {
                            WonderpleLib.getInstance().follow(uid, wonderples.get(holder.getAdapterPosition()).getUid());
                            wonderples.get(position).setFollowCheck("1");
                        } else if (listType.equals(Definitions.LIST_TYPE.WONDER)) {
                            WonderpleLib.getInstance().follow(uid, wonders.get(holder.getAdapterPosition()).getUid());
                            wonders.get(position).setFollowCheck("1");
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
