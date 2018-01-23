package com.dicosta.gpioclient.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dicosta.gpioclient.R;
import com.dicosta.gpioclient.viewmodel.ScanResultViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diego on 23/01/18.
 */

public class ScanResultAdapter extends RecyclerView.Adapter<ScanResultAdapter.ScanResultViewHolder> {

    public interface OnScanResultClickListener {
        void onAdapterViewClick(String macAddress);
    }


    private final List<ScanResultViewModel> mItems = new ArrayList<>();
    private final HashMap<String, Integer> mItemsMap = new HashMap<>();
    private OnScanResultClickListener mListener;

    public void setOnScanResultClickListener(OnScanResultClickListener listener) {
        mListener = listener;
    }

    @Override
    public ScanResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_result, parent, false);
        return new ScanResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScanResultViewHolder holder, int position) {
        final ScanResultViewModel scanResultViewModel = mItems.get(position);

        holder.bind(scanResultViewModel, mListener);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addScanResult(ScanResultViewModel bleScanResult) {
        // Not the best way to ensure distinct devices, just for sake on the demo.
        if (mItemsMap.containsKey(bleScanResult.getMacAddress())) {
            int position = mItemsMap.get(bleScanResult.getMacAddress());
            mItems.set(position, bleScanResult);
            notifyItemChanged(position);
        } else {
            mItems.add(bleScanResult);
            mItemsMap.put(bleScanResult.getMacAddress(), mItems.size() - 1);
            notifyItemInserted(mItems.size() - 1);
        }
    }

    static class ScanResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView nameText;
        @BindView(R.id.tv_mac)
        TextView macText;
        @BindView(R.id.tv_rssi)
        TextView rssiText;

        ScanResultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final ScanResultViewModel scanResultViewModel, OnScanResultClickListener listener) {
            nameText.setText(scanResultViewModel.getName());
            macText.setText(scanResultViewModel.getMacAddress());
            rssiText.setText(String.valueOf(scanResultViewModel.getRSSI()));


            int rssiColor;

            if (Math.abs(scanResultViewModel.getRSSI()) > 80) {
                rssiColor = ContextCompat.getColor(rssiText.getContext(), R.color.rssi_bad);
            } else if (Math.abs(scanResultViewModel.getRSSI()) > 70) {
                rssiColor = ContextCompat.getColor(rssiText.getContext(), R.color.rssi_medium);
            } else {
                rssiColor = ContextCompat.getColor(rssiText.getContext(), R.color.rssi_good);
            }

            rssiText.setTextColor(rssiColor);

            itemView.setOnClickListener(v -> listener.onAdapterViewClick(scanResultViewModel.getMacAddress()));
        }
    }
}
