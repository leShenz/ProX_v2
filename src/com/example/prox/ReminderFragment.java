package com.example.prox;

 
import com.radaee.reader.R;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReminderFragment extends Fragment {
	
	public ReminderFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);
        
        Intent intent = new Intent(getActivity(), com.example.prox.reminder.MainActivity.class);
		startActivity(intent);
		
        return rootView;
    }
}
