package ru.kvartira_omsk.realtorapp.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.kvartira_omsk.realtorapp.DBWork;
import ru.kvartira_omsk.realtorapp.R;
import ru.kvartira_omsk.realtorapp.dto.EventDTO;
import ru.kvartira_omsk.realtorapp.ViewEventActivity;


/**
 * Created by Иван on 14.03.2016.
 */
public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.EventsViewHolder>{
    private DBWork dbHelper;
    private static List<EventDTO> data;
    Context context;
    LayoutInflater inflater;


    //ContextMenu.ContextMenuInfo info;

    public EventsListAdapter(List<EventDTO> data) {

        this.data = data;
    }

    /*public ClientsListAdapter(){

    }*/

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);

        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, int position) {
        EventDTO item = data.get(position);
        holder.titleEvent.setText(item.getTitle());
        holder.whenEvent.setText(item.getDateEvent() + " " + item.getTimeEvent());
        holder.popupMenuIconEvent.setOnClickListener(clickListener);
        holder.popupMenuIconEvent.setTag(holder);


    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EventsViewHolder vholder = (EventsViewHolder) v.getTag();
            int position = vholder.getAdapterPosition();
            //Log.d(TAG, "onClick " + position);
            //Toast.makeText(v.getContext(),"This is position "+position, Toast.LENGTH_LONG ).show();

            //PopupMenu popup = new PopupMenu(v.getContext(), v);

        }
    };

    @Override
    public int getItemCount() {

        return data.size();
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
        //CardView cardViewClient;
        TextView titleEvent;
        TextView whenEvent;
        ImageView popupMenuIconEvent;

        public EventsViewHolder(View itemView) {
            super(itemView);

            //cardViewClient = (CardView) itemView.findViewById(R.id.cardViewClient);
            titleEvent = (TextView) itemView.findViewById(R.id.titleEvent);
            whenEvent = (TextView) itemView.findViewById(R.id.whenEvent);
            //itemView.setOnCreateContextMenuListener(this);

            popupMenuIconEvent = (ImageView) itemView.findViewById(R.id.popupmenu_event_icon);
            popupMenuIconEvent.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
            //popupMenuIconEvent.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(context, v);


        }*/




        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            /*new ClientsListAdapter().info = menuInfo;
            String strResult;
            if (menuInfo == null) strResult = "No";
            else strResult = "Yes";


            menu.setHeaderTitle(strResult);*/
            EventsViewHolder vholder = (EventsViewHolder) v.getTag();
            int position = vholder.getAdapterPosition();
            EventDTO item = data.get(position);
            int idEvent = (int) item.id;
            menu.setHeaderTitle(item.getTitle());

            menu.add(2, 1, idEvent, R.string.menu_edit);
            menu.add(2, 2, idEvent, R.string.menu_delete);


        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(v.getContext(),"Click item", Toast.LENGTH_LONG ).show();
            Intent intent_viewevent = new Intent(v.getContext(), ViewEventActivity.class);
            EventDTO item = data.get(getPosition());
            long selPosition = (long) Long.valueOf(item.id);
            intent_viewevent.putExtra("idviewevent", selPosition);
            v.getContext().startActivity(intent_viewevent);
        }



    }

}
