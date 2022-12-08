package com.example.javaapplication.Activity.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.javaapplication.Activity.Model.Data.Kabupaten.ListItemKabupaten;
import com.example.javaapplication.Activity.Model.Data.Kota.ListKotaItem;
import com.example.javaapplication.Activity.Model.Data.Province.ListItem;
import com.example.javaapplication.Activity.Model.Data.Village.VillageListItem;
import com.example.javaapplication.Activity.ProvinsiInterface;
import com.example.javaapplication.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

public class ProvinsiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ListItem> listItemArrayList;
    ArrayList<ListItemKabupaten> listItemKabupatens;
    ArrayList<ListKotaItem> listKotaItems;
    ArrayList<VillageListItem> villageListItemArrayList;

    Context context;
    private static final int PLACEHOLDER = 0;
    private static final int ITEM = 1;
    ProvinsiInterface pCallbackk;
    Dialog cmDialogFrag;
    String locationType;

    public ProvinsiAdapter(ArrayList<ListItem> listItemArrayList,
                           ArrayList<ListItemKabupaten> listItemKabupatens,
                           ArrayList<ListKotaItem> listKotaItems,
                           ArrayList<VillageListItem> villageListItemArrayList,
                           Context context,
                           ProvinsiInterface provinsiInterface,
                           Dialog cmDialogFrag,
                           String locationType) {

        this.listItemArrayList = listItemArrayList;
        this.listItemKabupatens = listItemKabupatens;
        this.listKotaItems = listKotaItems;
        this.villageListItemArrayList = villageListItemArrayList;
        this.context = context;
        this.pCallbackk = provinsiInterface;
        this.cmDialogFrag = cmDialogFrag;
        this.locationType = locationType;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case PLACEHOLDER :
                View viewItemPlaceholder = inflater.inflate(R.layout.card_placeholder, parent, false);
                viewHolder = new PlaceHolder(viewItemPlaceholder);
                break;
            case ITEM :
                View viewItemUser = inflater.inflate(R.layout.card_user, parent, false);
                viewHolder = new ProvinsiHolder(viewItemUser);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case PLACEHOLDER:
                final PlaceHolder placeHolder = (PlaceHolder) holder;
                if(locationType.equals("province")) {
                    placeHolder.tvCityDialog.setText("Please Choose your Desired Province");
                }else if(locationType.equals("district")){
                    placeHolder.tvCityDialog.setText("Please Choose your Desired Districts");
                }else{
                    placeHolder.tvCityDialog.setText("Please Choose your Desired City");
                }
            break;
            case ITEM:
                final ProvinsiHolder provinsiHolder = (ProvinsiHolder) holder;
                if(locationType.equals("province")){
                    provinsiHolder.tvProvinsiNama.setText(listItemArrayList.get(position).getName());
                    provinsiHolder.lnrCvProvinsi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            locationType = "province";
                            pCallbackk.getProvinsi(listItemArrayList.get(position).getName(),
                                    listItemArrayList.get(position).getPostalType(),
                                    listItemArrayList.get(position).getLookupId(),
                                    locationType,
                                    null);
                            cmDialogFrag.dismiss();
                        }
                    });
                }else if(locationType.equals("district")){
                    provinsiHolder.tvProvinsiNama.setText(listItemKabupatens.get(position).getName());
                    provinsiHolder.lnrCvProvinsi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            locationType = "district";
                            pCallbackk.getProvinsi(listItemKabupatens.get(position).getName(),
                                    listItemKabupatens.get(position).getPostalType(),
                                    listItemKabupatens.get(position).getLookupId(),
                                    locationType,
                                    null);
                            cmDialogFrag.dismiss();
                        }
                    });
                }else if(locationType.equals("city")){
                    provinsiHolder.tvProvinsiNama.setText(listKotaItems.get(position).getName());
                    provinsiHolder.lnrCvProvinsi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            locationType = "city";
                            pCallbackk.getProvinsi(listKotaItems.get(position).getName(),
                                    listKotaItems.get(position).getPostalType(),
                                    listKotaItems.get(position).getLookupId(),
                                    locationType,
                                    null);
                            cmDialogFrag.dismiss();
                        }
                    });
                }else{
                    provinsiHolder.tvProvinsiNama.setText(villageListItemArrayList.get(position).getName());
                    provinsiHolder.lnrCvProvinsi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            locationType = "village";
                            pCallbackk.getProvinsi(villageListItemArrayList.get(position).getName(),
                                    villageListItemArrayList.get(position).getPostalType(),
                                    villageListItemArrayList.get(position).getLookupId(),
                                    locationType,
                                    villageListItemArrayList.get(position).getZipCode());
                            cmDialogFrag.dismiss();
                        }
                    });
                }

                break;
        }
    }


    @Override
    public int getItemCount() {
        if(locationType.equals("province")) {
            return listItemArrayList.size();
        }else if(locationType.equals("district")){
            return listItemKabupatens.size();
        }else if(locationType.equals("city")){
            return listKotaItems.size();
        }else{
            return villageListItemArrayList.size();

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return PLACEHOLDER;
        }  else {
            return ITEM;
        }
    }

    public class PlaceHolder extends RecyclerView.ViewHolder{
        TextView tvCityDialog;

        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            tvCityDialog = itemView.findViewById(R.id.tv_city_dialog);
        }
    }


    public class ProvinsiHolder extends RecyclerView.ViewHolder{

        TextView tvProvinsiNama;
        LinearLayout lnrCvProvinsi;

        public ProvinsiHolder(@NonNull View itemView) {
            super(itemView);
            tvProvinsiNama = itemView.findViewById(R.id.tv_nama_provinsi);
            lnrCvProvinsi = itemView.findViewById(R.id.lnr_cv_provinsi);

        }
    }
}