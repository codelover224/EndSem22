package com.example.krushna.endsem;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BottomSheet extends BottomSheetDialogFragment implements MainActivity.ActivityListener{

    private BottomSheetListener sheetListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.bottom_sheet,container,false);

        TextView name = v.findViewById(R.id.bs_tv_name);
        TextView mobno = v.findViewById(R.id.bs_tv_mobnpo);

        return v;
    }

    @Override
    public void onInfo(MainActivity activity) {

    }

    public interface BottomSheetListener{
        void onButtonClicked(MainLogin ml);
    }

    public void onAttach(Context context){
        super.onAttach(context);

        sheetListener=(BottomSheetListener) context;
    }
}
