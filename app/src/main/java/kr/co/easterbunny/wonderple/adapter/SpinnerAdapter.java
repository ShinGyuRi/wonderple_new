package kr.co.easterbunny.wonderple.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import kr.co.easterbunny.wonderple.R;
import kr.co.easterbunny.wonderple.databinding.ViewSpinnerItemBinding;

/**
 * Created by scona on 2017-05-22.
 */

public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> icon;
    private List<String> text;
    private LayoutInflater inflater;

    public SpinnerAdapter(Context context, List<Integer> icon, List<String> text) {
        this.context = context;
        this.icon = icon;
        this.text = text;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        if (text != null) {
            return text.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return text.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewSpinnerItemBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.view_spinner_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ViewSpinnerItemBinding) convertView.getTag();
        }

        binding.imgIcon.setBackgroundResource(icon.get(position));

        binding.tvTag.setText(text.get(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewSpinnerItemBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.view_spinner_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ViewSpinnerItemBinding) convertView.getTag();
        }

        binding.imgIcon.setBackgroundResource(icon.get(position));

        binding.tvTag.setText(text.get(position));

        return convertView;
    }
}
