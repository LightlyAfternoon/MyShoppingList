package ru.rsue.Karnaukhova.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AlertDialog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import org.jetbrains.annotations.NotNull;
import ru.rsue.Karnaukhova.CountCost;
import ru.rsue.Karnaukhova.R;
import ru.rsue.Karnaukhova.database.ItemBaseHelper;
import ru.rsue.Karnaukhova.database.ItemDbSchema;
import ru.rsue.Karnaukhova.dto.ItemDTO;
import ru.rsue.Karnaukhova.dto.ItemInListDTO;
import ru.rsue.Karnaukhova.dto.mapper.ItemDTOMapper;
import ru.rsue.Karnaukhova.dto.mapper.WeightUnitDTOMapper;
import ru.rsue.Karnaukhova.entity.Item;
import ru.rsue.Karnaukhova.entity.ItemInList;
import ru.rsue.Karnaukhova.entity.WeightUnit;
import ru.rsue.Karnaukhova.repository.WeightUnitRepository;

public class ItemOnDateAdapter extends ArrayAdapter<ItemInList> {
    LayoutInflater mInflater;
    int mLayout;
    List<ItemInList> mItemsInList;
    Context mContext;
    SQLiteDatabase mDatabase;
    double mCost = 0;
    View sepLine;
    TextView allCost;
    View sepLine1;
    TextView nameView;
    TextView dateView;
    TextView countView;
    TextView weightUnitView;
    TextView priceView;
    CheckBox boughtCheckBox;
    ImageView itemColor;

    public ItemOnDateAdapter(Context context, int resource, List<ItemInList> itemsInList) {
        super(context, resource, itemsInList);

        mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();

        mItemsInList = itemsInList;
        mLayout = resource;
        mInflater = LayoutInflater.from(context);
    }

    double sumCost(String lastDate) throws ExecutionException, InterruptedException {
        for (ItemInList i: mItemsInList) {
            if (String.valueOf(i.getAddDate()).equals(lastDate)) {
                mCost = CountCost.CountCost(i, mCost, mContext);
            }
        }
        return mCost;
    }

//    ItemCursorWrapper queryItemWithUUID(String uuid) {
//        Cursor cursor = mDatabase.query(ItemDbSchema.ItemTable.NAME,
//                null,
//                ItemDbSchema.ItemTable.Cols.UUID + " = ?",
//                new String[]{uuid},
//                null,
//                null,
//                null);
//        return new ItemCursorWrapper(cursor);
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(mLayout, parent, false);

        itemColor = view.findViewById(R.id.item_color);
        sepLine = view.findViewById(R.id.sep_line2);
        allCost = view.findViewById(R.id.list_item_all_cost);
        sepLine1 = view.findViewById(R.id.sep_line1);
        nameView = view.findViewById(R.id.list_item_name);
        dateView = view.findViewById(R.id.list_item_date);
        countView = view.findViewById(R.id.list_item_count);
        weightUnitView = view.findViewById(R.id.list_item_weight_unit);
        priceView = view.findViewById(R.id.list_item_price);
        boughtCheckBox = view.findViewById(R.id.list_item_is_bought);

        ItemInList itemInList = mItemsInList.get(position);
//        ItemCursorWrapper cursorWrapperItem = queryItemWithUUID(itemInList.getItemId().toString());
//
//        Item item;
//        try {
//            cursorWrapperItem.moveToFirst();
//            item = (cursorWrapperItem.getItem());
//        }
//        finally {
//            cursorWrapperItem.close();
//        }

        ObjectMapper objectMapper = new ObjectMapper();
        ItemInList finalItemInList = itemInList;
        Item item;

        try {
            item = ItemDTOMapper.INSTANCE.mapToEntity(objectMapper.readValue(Executors.newSingleThreadExecutor().
                    submit(() -> getItemByUUID(finalItemInList.getItemId())).get(), ItemDTO.class), finalItemInList.getItemId());
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        WeightUnit weightUnit;
        try {
            weightUnit = WeightUnitDTOMapper.INSTANCE.mapToEntity(Executors.newSingleThreadExecutor().submit(() -> WeightUnitRepository.get(mContext).getWeightUnitOfItem(item)).get());
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        weightUnitView.setText(weightUnit.getName());

        if (item.getColor() != null) {
            itemColor.setBackgroundColor(Color.parseColor(item.getColor()));
        }

        nameView.setText(item.getName());
        if (itemInList.getIsPriority()) {
            nameView.setTypeface(Typeface.DEFAULT_BOLD);
            nameView.setTextColor(Color.parseColor("#FF0000"));
        }
        if (itemInList.getQuantityBought() >= itemInList.getCount()) {
            nameView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            nameView.setTextColor(Color.parseColor("#696969"));
        }
        String count = "";
        if (itemInList.getQuantityBought() > 0) {
            if (itemInList.getQuantityBought() == (int) itemInList.getQuantityBought()) {
                count += (int) itemInList.getQuantityBought() + "\n" + "--" + "\n";
            } else {
                count += itemInList.getQuantityBought() + "\n" + "--" + "\n";
            }
        }
        if (itemInList.getCount() == (int) itemInList.getCount()) {
            count += (int) itemInList.getCount();
        } else {
            count += itemInList.getCount();
        }
        countView.setText(count);
        dateView.setText(DateFormat.getDateInstance(DateFormat.FULL).format(itemInList.getBuyOnDate()));
        if (weightUnitView.getText().equals("шт.") || weightUnitView.getText().equals("кг") || weightUnitView.getText().equals("л")){
            priceView.setText(String.valueOf(item.getPriceForOne() * itemInList.getCount()));
        }
        else {
            priceView.setText(String.valueOf((item.getPriceForOne() / 100) * itemInList.getCount()));
        }

        String lastDate = "";
        String date = String.valueOf(itemInList.getBuyOnDate());
        int pos = position;
        if (pos != 0) {
            itemInList = mItemsInList.get(pos - 1);
            lastDate = String.valueOf(itemInList.getBuyOnDate());
            itemInList = mItemsInList.get(pos);
        }

        if (!date.equals(lastDate)) {
            mCost = 0;
            try {
                allCost.setText(String.valueOf(sumCost(date)));
            } catch (ExecutionException | InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            sepLine.setVisibility(View.VISIBLE);
            allCost.setVisibility(View.VISIBLE);
            sepLine1.setVisibility(View.VISIBLE);
            dateView.setVisibility(View.VISIBLE);
        }
        else {
            sepLine.setVisibility(View.GONE);
            allCost.setVisibility(View.GONE);
            sepLine1.setVisibility(View.GONE);
            dateView.setVisibility(View.GONE);
        }

        if (itemInList.getQuantityBought() >= itemInList.getCount()) {
            boughtCheckBox.setChecked(true);
        } else {
            boughtCheckBox.setChecked(false);
        }

        Button delItem = view.findViewById(R.id.list_item_delete);

        delItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemsInList.remove(position);
//                mDatabase.execSQL("delete from " + ItemDbSchema.ItemInListTable.NAME +
//                        " where " + ItemDbSchema.ItemInListTable.Cols.UUID + " = '" + finalItemInList.getId() + "'");

                if (deleteItemInList(finalItemInList) == HttpURLConnection.HTTP_NOT_FOUND) {
                    Toast.makeText(mContext, "Не удалось удалить", Toast.LENGTH_LONG).show();
                }

                notifyDataSetChanged();
            }
        });

        boughtCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View view = mInflater.inflate(R.layout.count_bought_items, null);
                TextInputEditText textInputEditText = view.findViewById(R.id.count_bought_items_edit_text);
                if (finalItemInList.getQuantityBought() == 0) {
                    textInputEditText.setText(String.valueOf(finalItemInList.getCount()));
                } else {
                    textInputEditText.setText(String.valueOf(finalItemInList.getQuantityBought()));
                }
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(getContext()).setView(view).setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {if (textInputEditText.getText() != null && !textInputEditText.getText().toString().isEmpty() && Float.parseFloat(textInputEditText.getText().toString()) > 0) {
                        finalItemInList.setQuantityBought(Float.parseFloat(textInputEditText.getText().toString()));
                    } else {
                        finalItemInList.setQuantityBought(0);
                    }
                        mDatabase.execSQL("update " + ItemDbSchema.ItemInListTable.NAME +
                                " set " + ItemDbSchema.ItemInListTable.Cols.QUANTITYBOUGHT + " = " + finalItemInList.getQuantityBought() +
                                " where " + ItemDbSchema.ItemInListTable.Cols.UUID + " = '" + finalItemInList.getUuid() + "'");

                        notifyDataSetChanged();

                        dialogInterface.dismiss();
                    }
                }).create();

                alertDialog.show();
            }
        });

        return view;
    }

    @NotNull
    private String getItemByUUID(UUID uuid) {
        HttpURLConnection httpURLConnection;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL("http://10.0.2.2:8080/MyShoppingListBackend/item/" + uuid);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("GET");

//            try (DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream())) {
//                ObjectMapper objectMapper = new ObjectMapper();
//                ItemDTO itemDTO = new ItemDTO(uuid);
//
//                dataOutputStream.writeBytes(objectMapper.writeValueAsString(itemDTO));
//                dataOutputStream.flush();
//            }

            httpURLConnection.connect();

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            }

            httpURLConnection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringBuilder.toString();
    }

    @NotNull
    private int deleteItemInList(ItemInList itemInList) {
        HttpURLConnection httpURLConnection;
        int responseCode;

        try {
            URL url = new URL("http://10.0.2.2:8080/MyShoppingListBackend/list/" + itemInList.getListId() + "/item/" + itemInList.getItemId());

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("DELETE");

//            try (DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream())) {
//                ObjectMapper objectMapper = new ObjectMapper();
//                ItemInListDTO itemInListDTO = new ItemInListDTO(itemInList.getUuid());
//
//                dataOutputStream.writeBytes(objectMapper.writeValueAsString(itemInListDTO));
//                dataOutputStream.flush();
//            }

            httpURLConnection.connect();
            responseCode = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return responseCode;
    }
}