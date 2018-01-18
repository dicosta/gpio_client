package com.dicosta.gpioclient.adapter;

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
        mItems.clear();
        mItems.addAll(items);
    }

    public void setLightItemAdapterListener(LightItemAdapterListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_light, parent, false);
        LightItemViewHolder holder = new LightItemViewHolder(v, new ViewHolderListener() {
            @Override
            public void switchButtonClicked(LightItemViewHolder vh) {
                if (mListener != null) {
                    Light light = mItems.get(vh.getAdapterPosition());

                    if (light.isLightOff()) {
                        mListener.onSwitchTurnOnClicked(light);
                    } else {
                        mListener.onSwitchTurnOffClicked(light);
                    }
                }
            }

            @Override
            public void blinkButtonClicked(LightItemViewHolder vh) {
                if (mListener != null) {
                    Light light = mItems.get(vh.getAdapterPosition());

                    if (light.isLighBlink()) {
                        mListener.onStopBlinkClicked(light);
                    } else {
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

        viewHolder.imgBulb.setSelected(currentLight.isLightOn() || currentLight.isLighBlink());
        viewHolder.txtName.setText(currentLight.getName());

        viewHolder.btnBlink.setText(currentLight.isLighBlink() ? R.string.stop_blink : R.string.start_blink);
        viewHolder.btnSwitch.setText(currentLight.isLightOn() ? R.string.turn_off: R.string.turn_on);
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
                    viewHolderListener.switchButtonClicked(LightItemViewHolder.this);
                }
            });

            btnBlink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolderListener.blinkButtonClicked(LightItemViewHolder.this);
                }
            });
        }
    }

    interface ViewHolderListener {
        void switchButtonClicked(LightItemViewHolder vh);
        void blinkButtonClicked(LightItemViewHolder vh);
    }
}
