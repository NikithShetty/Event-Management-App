package adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikith_shetty.vgroup.R;

import java.util.List;

import helper.classes.Global;
import models.EventData;

/**
 * Created by Nikith_Shetty on 14/04/2016.
 */
public class RVAdapter_events extends RecyclerView.Adapter<RVAdapter_events.ViewHolder> {
    private Listener listener;
    private List<EventData> eventDataList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView view){
            super(view);
            cardView = view;
        }
    }

    public RVAdapter_events(List<EventData> list){
        eventDataList = list;
    }

    @Override
    public RVAdapter_events.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv;
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_events,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            createEventsCard(holder, position);
    }

    @Override
    public int getItemCount() {
        return eventDataList.size();
    }

    private void createEventsCard(ViewHolder holder, final int position){
        //setup view
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
        //coordInfo.setText(eventDataList.get(position).getCoordinatorInfo());
        venue.setText(eventDataList.get(position).getVenue().getArea());
        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onClick(eventDataList.get(position));
            }
        });
    }

    public interface Listener{
        void onClick(EventData data);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }
}
