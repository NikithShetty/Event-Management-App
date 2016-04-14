package com.nikith_shetty.vgroup;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nikith_Shetty on 14/04/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private List<eventData> eventDataList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView view){
            super(view);
            cardView = view;
        }
    }

    public RVAdapter(List<eventData> list){
        eventDataList = list;
    }

    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_events,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView eventName,college,fee,details,coordInfo,venue;
        eventName = (TextView)cardView.findViewById(R.id.card_event_name);
        college = (TextView)cardView.findViewById(R.id.card_event_college);
        fee = (TextView)cardView.findViewById(R.id.card_event_fee);
        details = (TextView)cardView.findViewById(R.id.card_event_details);
        coordInfo = (TextView)cardView.findViewById(R.id.card_event_coordInfo);
        venue = (TextView)cardView.findViewById(R.id.card_event_venue);
        eventName.setText(eventDataList.get(position).getEventName());
        college.setText(eventDataList.get(position).getCollege());
        fee.setText(eventDataList.get(position).getFee());
        details.setText(eventDataList.get(position).getDetails());
        coordInfo.setText(eventDataList.get(position).getCoordinatorInfo());
        venue.setText(eventDataList.get(position).getVenue());
    }

    @Override
    public int getItemCount() {

        return eventDataList.size();
    }
}
