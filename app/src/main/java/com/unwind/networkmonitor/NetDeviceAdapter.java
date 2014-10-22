package com.unwind.networkmonitor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by dan on 10/22/14.
 */
public class NetDeviceAdapter extends RecyclerView.Adapter<NetDeviceAdapter.ViewHolder> {
    private List<InetAddress> addresses;
    private int rowLayout;
    private Context mContext;

    public NetDeviceAdapter(List<InetAddress> addresses, int rowLayout, Context mContext) {
        this.addresses = addresses;
        this.rowLayout = rowLayout;
        this.mContext = mContext;
    }


    @Override
    public NetDeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NetDeviceAdapter.ViewHolder viewHolder, int i) {
        InetAddress address = addresses.get(i);
        viewHolder.deviceName.setText(address.getHostName());
        viewHolder.deviceIp.setText(address.getAddress().toString());

    }

    @Override
    public int getItemCount() {
        return addresses == null ? 0 : addresses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView deviceName;
        public TextView deviceIp;

        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = (TextView) itemView.findViewById(R.id.deviceName);
            deviceIp = (TextView) itemView.findViewById(R.id.deviceIp);
        }
    }
}
