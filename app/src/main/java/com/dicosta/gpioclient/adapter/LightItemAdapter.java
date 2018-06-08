package com.dicosta.gpioclient.adapter;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dicosta.gpioclient.R;
import com.dicosta.gpioclient.domain.Light;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diego on 17/01/18.
 */

public class LightItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public interface LightItemAdapterListener {
        void onSwitchTurnOnClicked(Light light);
        void onSwitchTurnOffClicked(Light light);
        void onStartBlinkClicked(Light light);
        void onStopBlinkClicked(Light light);
    }

    private List<Light> mItems = new ArrayList<>();
    private LightItemAdapterListener mListener;

    public void setItems(List<Light> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new LightItemDiffCallback(items, mItems));

        mItems.clear();
        mItems.addAll(items);

        diffResult.dispatchUpdatesTo(this);
    }

    public void setLightItemAdapterListener(LightItemAdapterListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_light, parent, false);
        LightItemViewHolder holder = new LightItemViewHolder(v, new ViewHolderListener() {
            @Override
            public void switchButtonClicked(int adapterPosition) {
                if (mListener != null) {
                    Light light = mItems.get(adapterPosition);

                    if (light.isLightOff() || light.isLightBlink()) {
                        light.setState(Light.STATE_ON);
                        notifyItemChanged(adapterPosition);
                        mListener.onSwitchTurnOnClicked(light);
                    } else {
                        light.setState(Light.STATE_OFF);
                        notifyItemChanged(adapterPosition);
                        mListener.onSwitchTurnOffClicked(light);
                    }
                }
            }

            @Override
            public void blinkButtonClicked(int adapterPosition) {
                if (mListener != null) {
                    Light light = mItems.get(adapterPosition);

                    if (light.isLightBlink()) {
                        light.setState(Light.STATE_OFF);
                        notifyItemChanged(adapterPosition);
                        mListener.onStopBlinkClicked(light);
                    } else {
                        light.setState(Light.STATE_BLINK);
                        notifyItemChanged(adapterPosition);
                        mListener.onStartBlinkClicked(light);
                    }
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Light currentLight = mItems.get(holder.getAdapterPosition());

        LightItemViewHolder viewHolder = (LightItemViewHolder)holder;
        viewHolder.bind(currentLight);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class LightItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.btn_switch)
        Button btnSwitch;
        @BindView(R.id.btn_blink)
        Button btnBlink;
        @BindView(R.id.img_bulb)
        ImageView imgBulb;

        ViewHolderListener viewHolderListener;

        public LightItemViewHolder(View itemView, ViewHolderListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.viewHolderListener = listener;

            btnSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolderListener.switchButtonClicked(getAdapterPosition());
                }
            });

            btnBlink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolderListener.blinkButtonClicked(getAdapterPosition());
                }
            });
        }

        public void bind(Light light) {
            txtName.setText(light.getName());
            imgBulb.setSelected(light.isLightOn() || light.isLightBlink());

            btnBlink.setText(light.isLightBlink() ? R.string.stop_blink : R.string.start_blink);
            btnSwitch.setText(light.isLightOn() ? R.string.turn_off: R.string.turn_on);
        }
    }

    interface ViewHolderListener {
        void switchButtonClicked(int adapterPosition);
        void blinkButtonClicked(int adapterPosition);
    }
}
