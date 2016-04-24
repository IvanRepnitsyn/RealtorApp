package ru.kvartira_omsk.realtorapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.kvartira_omsk.realtorapp.DBWork;
import ru.kvartira_omsk.realtorapp.MainActivity;
import ru.kvartira_omsk.realtorapp.R;
import ru.kvartira_omsk.realtorapp.dto.ClientDTO;
import ru.kvartira_omsk.realtorapp.fragment.ClientsFragment;


/**
 * Created by Иван on 14.03.2016.
 */
public class ClientsListAdapter extends RecyclerView.Adapter<ClientsListAdapter.ClientsViewHolder>{
    private DBWork dbHelper;
    private List<ClientDTO> data;
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
        holder.popupMenuIcon.setOnClickListener(clickListener);
        holder.popupMenuIcon.setTag(holder);


    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ClientsViewHolder vholder = (ClientsViewHolder) v.getTag();
            int position = vholder.getAdapterPosition();
            //Log.d(TAG, "onClick " + position);
            Toast.makeText(v.getContext(),"This is position "+position, Toast.LENGTH_LONG ).show();
            PopupMenu popup = new PopupMenu(v.getContext(), v);

        }
    };

    @Override
    public int getItemCount() {

        return data.size();
    }

    public static class ClientsViewHolder extends RecyclerView.ViewHolder  {
        //CardView cardViewClient;
        TextView titleClient;
        ImageView popupMenuIcon;

        public ClientsViewHolder(View itemView) {
            super(itemView);

            //cardViewClient = (CardView) itemView.findViewById(R.id.cardViewClient);
            titleClient = (TextView) itemView.findViewById(R.id.titleClient);
            //itemView.setOnCreateContextMenuListener(this);

            popupMenuIcon = (ImageView) itemView.findViewById(R.id.popupmenu_icon);
            //popupMenuIcon.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(context, v);


        }*/




        /*@Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            new ClientsListAdapter().info = menuInfo;
            String strResult;
            if (menuInfo == null) strResult = "No";
            else strResult = "Yes";

            menu.setHeaderTitle(strResult);
            menu.add(0, 1, 0, R.string.menu_delete);


        }*/

    }

}
