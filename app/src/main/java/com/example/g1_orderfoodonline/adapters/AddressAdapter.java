package com.example.g1_orderfoodonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g1_orderfoodonline.R;
import com.example.g1_orderfoodonline.models.DeliveryAddress;
import com.example.g1_orderfoodonline.utils.LogUtils;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private static final String TAG = "AddressAdapter";
    
    private Context context;
    private List<DeliveryAddress> addresses;
    private AddressListener listener;
    private boolean isSelectionMode;

    public interface AddressListener {
        void onAddressSelected(DeliveryAddress address);
        void onEditAddress(DeliveryAddress address);
        void onDeleteAddress(DeliveryAddress address);
        void onSetDefaultAddress(DeliveryAddress address);
    }

    public AddressAdapter(Context context, List<DeliveryAddress> addresses, AddressListener listener, boolean isSelectionMode) {
        this.context = context;
        this.addresses = addresses;
        this.listener = listener;
        this.isSelectionMode = isSelectionMode;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
            return new AddressViewHolder(view);
        } catch (Exception e) {
            LogUtils.error(TAG, "Error creating view holder", e);
            throw e;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        try {
            DeliveryAddress address = addresses.get(position);
            
            holder.textViewName.setText(address.getName());
            holder.textViewPhone.setText(address.getPhone());
            holder.textViewAddress.setText(address.getFullAddress());
            holder.checkBoxDefault.setChecked(address.isDefault());
            
            // Xử lý sự kiện click vào địa chỉ
            holder.itemView.setOnClickListener(v -> {
                if (isSelectionMode && listener != null) {
                    listener.onAddressSelected(address);
                }
            });
            
            // Xử lý sự kiện click vào checkbox
            holder.checkBoxDefault.setOnClickListener(v -> {
                if (listener != null && holder.checkBoxDefault.isChecked() && !address.isDefault()) {
                    listener.onSetDefaultAddress(address);
                }
            });
            
            // Xử lý sự kiện click vào nút sửa
            holder.buttonEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditAddress(address);
                }
            });
            
            // Xử lý sự kiện click vào nút xóa
            holder.buttonDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteAddress(address);
                }
            });
            
            // Ẩn/hiện các nút tùy theo chế độ
            if (isSelectionMode) {
                holder.buttonEdit.setVisibility(View.GONE);
                holder.buttonDelete.setVisibility(View.GONE);
                holder.checkBoxDefault.setEnabled(false);
            } else {
                holder.buttonEdit.setVisibility(View.VISIBLE);
                holder.buttonDelete.setVisibility(View.VISIBLE);
                holder.checkBoxDefault.setEnabled(true);
            }
        } catch (Exception e) {
            LogUtils.error(TAG, "Error binding view holder", e);
        }
    }

    @Override
    public int getItemCount() {
        return addresses != null ? addresses.size() : 0;
    }

    public void updateData(List<DeliveryAddress> newAddresses) {
        this.addresses = newAddresses;
        notifyDataSetChanged();
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPhone, textViewAddress;
        CheckBox checkBoxDefault;
        ImageButton buttonEdit, buttonDelete;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            checkBoxDefault = itemView.findViewById(R.id.checkBoxDefault);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
