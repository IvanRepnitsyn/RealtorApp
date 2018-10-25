package ru.kvartira_omsk.realtorapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.kvartira_omsk.realtorapp.AddObjectActivity;
import ru.kvartira_omsk.realtorapp.DBWork;
import ru.kvartira_omsk.realtorapp.R;
import ru.kvartira_omsk.realtorapp.ViewObjectActivity;
import ru.kvartira_omsk.realtorapp.dto.ObjectDTO;


/**
 * Created by Иван on 14.03.2016.
 */
public class ObjectsListAdapter extends RecyclerView.Adapter<ObjectsListAdapter.ObjectsViewHolder>{
    private DBWork dbHelper;
    private static List<ObjectDTO> data;
    Context context;
    LayoutInflater inflater;


    //ContextMenu.ContextMenuInfo info;

    public ObjectsListAdapter(List<ObjectDTO> data) {

        this.data = data;
    }

    /*public ClientsListAdapter(){

    }*/

    @Override
    public ObjectsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.object_item, parent, false);

        return new ObjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ObjectsViewHolder holder, int position) {
        ObjectDTO item = data.get(position);
        holder.titleObject.setText(item.getTitle());
        holder.strNumberRoom.setText(item.getNumberRoom() + "-комн.");
        holder.popupMenuIconObject.setOnClickListener(clickListener);
        holder.popupMenuIconObject.setTag(holder);


    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ObjectsViewHolder vholder = (ObjectsViewHolder) v.getTag();
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

    public static class ObjectsViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
        //CardView cardViewClient;
        TextView titleObject;
        TextView strNumberRoom;
        ImageView popupMenuIconObject;
        private static final int OBJECT_ACTIVITY_VIEW = 16;

        public ObjectsViewHolder(View itemView) {
            super(itemView);

            //cardViewClient = (CardView) itemView.findViewById(R.id.cardViewClient);
            titleObject = (TextView) itemView.findViewById(R.id.titleObject);
            strNumberRoom = (TextView) itemView.findViewById(R.id.countRoomObject);
            //itemView.setOnCreateContextMenuListener(this);

            popupMenuIconObject = (ImageView) itemView.findViewById(R.id.popupmenu_object_icon);
            popupMenuIconObject.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
            //popupMenuIconObject.setOnClickListener(this);
            //Исправить
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
            ObjectsViewHolder vholder = (ObjectsViewHolder) v.getTag();
            int position = vholder.getAdapterPosition();
            ObjectDTO item = data.get(position);
            int idObject = (int) item.id;
            menu.setHeaderTitle(item.getTitle());

            menu.add(1, 1, idObject, R.string.menu_edit);
            menu.add(1, 2, idObject, R.string.menu_delete);


        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(),"Click item", Toast.LENGTH_LONG ).show();
            Intent intent_viewobject = new Intent(v.getContext(), ViewObjectActivity.class);
            ObjectDTO item = data.get(getPosition());
            long selPosition = (long) Long.valueOf(item.id);
            intent_viewobject.putExtra("idviewobject", selPosition);
            v.getContext().startActivity(intent_viewobject);
            Toast.makeText(v.getContext(),"Clicked item " +selPosition, Toast.LENGTH_LONG ).show();
        }



    }

}
