package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewSearchBrandBinding;
import kr.co.easterbunny.wonderple.databinding.ViewSearchCosmeticBinding;
import kr.co.easterbunny.wonderple.databinding.ViewSearchCosmeticHeaderBinding;
import kr.co.easterbunny.wonderple.library.util.JSLog;
import kr.co.easterbunny.wonderple.model.CustomBitmapPool;
import kr.co.easterbunny.wonderple.model.SearchCosmeticResult;

/**
 * Created by scona on 2017-06-12.
 */

public class SearchCosmeticAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter<SearchCosmeticAdapter.HeaderBindingHolder> {

    public static final int ITEM_BRAND = 0;
    public static final int ITEM_COSMETIC = 1;

    private Context context;
    private List<SearchCosmeticResult.Item> items = new ArrayList<>();
    private List<SearchCosmeticResult.Brand> brands = new ArrayList<>();
    private List<Object> objects = new ArrayList<>();

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public SearchCosmeticAdapter(Context context, List<SearchCosmeticResult.Item> items, List<SearchCosmeticResult.Brand> brands) {
        this.context = context;
        this.items = items;
        this.brands = brands;
        if (brands != null) {
            objects.addAll(brands);
        }
        if (items != null) {
            objects.addAll(items);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == ITEM_BRAND) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_search_brand, parent, false);
            return new BrandBindingHolder(view);
        } else if (viewType == ITEM_COSMETIC) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_search_cosmetic, parent, false);
            return new CosmeticBindingHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);

        if (itemType == ITEM_BRAND) {
            Glide.with(context)
                    .load(brands.get(position).getLogoUrl())
                    .into(((BrandBindingHolder) holder).brandBinding.imgBrand);

            ((BrandBindingHolder) holder).brandBinding.tvBrandName.setText(brands.get(position).getBrandName());
        } else if (itemType == ITEM_COSMETIC) {
            int r = Integer.parseInt(items.get(position).getR());
            int g = Integer.parseInt(items.get(position).getG());
            int b = Integer.parseInt(items.get(position).getB());

            Glide.with(context)
                    .load(items.get(position).getImageUrl())
                    .into(((CosmeticBindingHolder) holder).cosmeticBinding.imgCosmetic);

            Glide.with(context)
                    .load(R.drawable.posting_tagbox)
                    .bitmapTransform(new ColorFilterTransformation(new CustomBitmapPool(), Color.rgb(r, g, b)), new CropCircleTransformation(new CustomBitmapPool()))
                    .into(((CosmeticBindingHolder) holder).cosmeticBinding.imgItemColor);

            ((CosmeticBindingHolder) holder).cosmeticBinding.tvBrandItemName.setText(context.getString(R.string.str_brand_item_name, items.get(position).getBrandName(), items.get(position).getItemName()));
            ((CosmeticBindingHolder) holder).cosmeticBinding.tvColorName.setText(items.get(position).getColoName());

            ((CosmeticBindingHolder) holder).cosmeticBinding.btnCosmetic.setTag(new Integer(position));

            if (position == 0 && ((CosmeticBindingHolder) holder).cosmeticBinding.btnCosmetic.isChecked()) {
                lastChecked = ((CosmeticBindingHolder) holder).cosmeticBinding.btnCosmetic;
                lastCheckedPos = 0;
            }
            ((CosmeticBindingHolder) holder).cosmeticBinding.btnCosmetic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBox cb = (CheckBox) buttonView;
                    int clickedPos = ((Integer) cb.getTag()).intValue();

                    if (isChecked) {
                        if (lastChecked != null) {
                            lastChecked.setChecked(false);
                        }
                        lastChecked = cb;
                        lastCheckedPos = clickedPos;

                        ((CosmeticBindingHolder) holder).cosmeticBinding.btnCosmetic.setButtonDrawable(R.drawable.search_item_select);
                    } else {
                        lastChecked = null;
                        ((CosmeticBindingHolder) holder).cosmeticBinding.btnCosmetic.setButtonDrawable(null);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (objects.get(position) instanceof SearchCosmeticResult.Brand) {
            return ITEM_BRAND;
        } else if (objects.get(position) instanceof SearchCosmeticResult.Item) {
            return ITEM_COSMETIC;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return objects == null ? 0 : objects.size();
    }

    public class CosmeticBindingHolder extends RecyclerView.ViewHolder{
        private ViewSearchCosmeticBinding cosmeticBinding;

        public CosmeticBindingHolder(View itemView) {
            super(itemView);
            cosmeticBinding = DataBindingUtil.bind(itemView);
        }
    }

    public class BrandBindingHolder extends RecyclerView.ViewHolder{
        private ViewSearchBrandBinding brandBinding;

        public BrandBindingHolder(View itemView) {
            super(itemView);
            brandBinding = DataBindingUtil.bind(itemView);
        }
    }



    @Override
    public long getHeaderId(int position) {
        long result = -1;

        if (objects.size() > 0 && brands != null) {
            if (objects.get(position) instanceof SearchCosmeticResult.Brand) {
                brands.get(position).setbChar("Basdf");
                result = brands.get(position).getbChar().toString().charAt(0);
            } else if (objects.get(position) instanceof SearchCosmeticResult.Item) {
                items.get(position).setaChar("Aasdf");
                result = items.get(position).getaChar().toString().charAt(0);
            }
        }
        return result;
    }

    @Override
    public HeaderBindingHolder onCreateHeaderViewHolder(ViewGroup parent) {
        JSLog.D("TEST!!!!!!!!", new Throwable());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_search_cosmetic_header, parent, false);
        return new HeaderBindingHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderBindingHolder holder, int position) {
        int itemType = getItemViewType(position);
        if (itemType == ITEM_BRAND) {
            holder.binding.tvHeader.setText(context.getString(R.string.str_count_brand_search, brands.size()));
        } else if (itemType == ITEM_COSMETIC) {
            holder.binding.tvHeader.setText(context.getString(R.string.str_count_item_search, items.size()));
        }
    }


    public class HeaderBindingHolder extends RecyclerView.ViewHolder{
        private ViewSearchCosmeticHeaderBinding binding;

        public HeaderBindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public static int getLastCheckedPos() {
        return lastCheckedPos;
    }
}
