package ru.kvartira_omsk.realtorapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.kvartira_omsk.realtorapp.DBWork;
import ru.kvartira_omsk.realtorapp.MainActivity;
import ru.kvartira_omsk.realtorapp.R;
import ru.kvartira_omsk.realtorapp.ViewClientActivity;
import ru.kvartira_omsk.realtorapp.ViewEventActivity;
import ru.kvartira_omsk.realtorapp.dto.ClientDTO;
import ru.kvartira_omsk.realtorapp.dto.EventDTO;
import ru.kvartira_omsk.realtorapp.fragment.ClientsFragment;


/**
 * Created by Иван on 14.03.2016.
 */
public class ClientsListAdapter extends RecyclerView.Adapter<ClientsListAdapter.ClientsViewHolder>{
    private DBWork dbHelper;
    private static List<ClientDTO> data;
    Context context;
    LayoutInflater inflater;


    //ContextMenu.ContextMenuInfo info;

    public ClientsListAdapter(List<ClientDTO> data) {

        this.data = data;
    }

    /*public ClientsListAdapter(){

    }*/

    @Override
    public ClientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);

        return new ClientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientsViewHolder holder, int position) {
        ClientDTO item = data.get(position);
        holder.titleClient.setText(item.getTitle());
        holder.popupMenuIconClient.setOnClickListener(clickListener);
        holder.popupMenuIconClient.setTag(holder);


    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClientsViewHolder vholder = (ClientsViewHolder) v.getTag();
            int position = vholder.getAdapterPosition();
            //Log.d(TAG, "onClick " + position);
            Toast.makeText(v.getContext(),"This is position "+position, Toast.LENGTH_LONG ).show();

            //PopupMenu popup = new PopupMenu(v.getContext(), v);

        }
    };

    @Override
    public int getItemCount() {

        return data.size();
    }

    public static class ClientsViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener  {
        //CardView cardViewClient;
        TextView titleClient;
        ImageView popupMenuIconClient;

        public ClientsViewHolder(View itemView) {
            super(itemView);

            //cardViewClient = (CardView) itemView.findViewById(R.id.cardViewClient);
            titleClient = (TextView) itemView.findViewById(R.id.titleClient);
            //itemView.setOnCreateContextMenuListener(this);

            popupMenuIconClient = (ImageView) itemView.findViewById(R.id.popupmenu_client_icon);
            popupMenuIconClient.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
            //popupMenuIconClient.setOnClickListener(this);
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
            ClientsViewHolder vholder = (ClientsViewHolder) v.getTag();
            int position = vholder.getAdapterPosition();
            ClientDTO item = data.get(position);
            int idClient = (int) item.id;
            menu.setHeaderTitle(item.getTitle());

            menu.add(3, 1, idClient, R.string.menu_edit);
            menu.add(3, 2, idClient, R.string.menu_delete);
            menu.add(3, 3, idClient, R.string.menu_addobject);
            menu.add(3, 4, idClient, R.string.menu_addevent);
            menu.add(3, 5, idClient, R.string.menu_call);


        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(),"Click item", Toast.LENGTH_LONG ).show();
            Intent intent_viewclient = new Intent(v.getContext(), ViewClientActivity.class);
            ClientDTO item = data.get(getPosition());
            long selPosition = (long) Long.valueOf(item.id);
            intent_viewclient.putExtra("idviewclient", selPosition);
            v.getContext().startActivity(intent_viewclient);
        }



    }

}
