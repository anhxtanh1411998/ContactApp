package com.gdfdfdfdfd.contactapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomAdapter extends ArrayAdapter<ListItems> {
    private Context context;
    private ArrayList<ListItems> listItems, tempCustomer, suggestions;
    private int resource;


    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ListItems> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listItems = objects;
        this.tempCustomer = new ArrayList<>(objects);
        this.suggestions = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHold viewHold;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_items_contact, parent, false);

            viewHold = new ViewHold();
            viewHold.NameContact = convertView.findViewById(R.id.tv_name);
            viewHold.PhoneContact = convertView.findViewById(R.id.tv_phone);
            viewHold.AvatarContact = convertView.findViewById(R.id.ima_avatar);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        ListItems contact = listItems.get(position);
        viewHold.NameContact.setText(contact.getName());
        viewHold.PhoneContact.setText(contact.getPhone());
        if (contact.getAvatar()) {
            viewHold.AvatarContact.setBackgroundResource(R.drawable.man);
        } else {
            viewHold.AvatarContact.setBackgroundResource(R.drawable.woman);
        }
        return convertView;
    }

    static class ViewHold {
        ImageView AvatarContact;
        TextView NameContact;
        TextView PhoneContact;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ListItems items : tempCustomer) {
                    if (items.getName().toLowerCase().contains(constraint.toString().toLowerCase()) || items.getPhone().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(items);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<ListItems> c = (ArrayList<ListItems>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ListItems cast : c) {
                    add(cast);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };

//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//
//        if (charText.length() != 0) {
//            listItems.clear();
//            for (ListItems wp : listItems) {
//                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    listItems.add(wp);
//                }
//            }
//        } else {
//
//        }
//        notifyDataSetChanged();
//    }


}
