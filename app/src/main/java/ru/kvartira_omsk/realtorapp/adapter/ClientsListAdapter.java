package ru.kvartira_omsk.realtorapp.adapter;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.kvartira_omsk.realtorapp.DBWork;
import ru.kvartira_omsk.realtorapp.R;
import ru.kvartira_omsk.realtorapp.dto.ClientDTO;


/**
 * Created by Иван on 14.03.2016.
 */
public class ClientsListAdapter extends RecyclerView.Adapter<ClientsListAdapter.ClientsViewHolder>{
    private DBWork dbHelper;
    private List<ClientDTO> data;

    public ClientsListAdapter(List<ClientDTO> data) {
        this.data = data;
    }

    @Override
    public ClientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);

        return new ClientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientsViewHolder holder, int position) {
        ClientDTO item = data.get(position);
        holder.titleClient.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ClientsViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewClient;
        TextView titleClient;

        public ClientsViewHolder(View itemView) {
            super(itemView);

            //cardViewClient = (CardView) itemView.findViewById(R.id.cardViewClient);
            titleClient = (TextView) itemView.findViewById(R.id.titleClient);
        }

    }

}
