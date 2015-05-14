package com.varungupta.googleimagesearch;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

interface SettingsDialogListener {
    void onSaveSettingsClicked();
}
/**
 * Created by varungupta on 5/13/15.
 */
public class SettingsDialog extends DialogFragment {

    private SettingsDialogListener listener;

    public SettingsDialog() {

    }

    public static SettingsDialog getInstance(SettingsDialogListener listener) {
        SettingsDialog dialog = new SettingsDialog();
        dialog.listener = listener;

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settings, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        SearchSettings settings = SearchSettings.getInstance();
        setSpinnerAdapter(view, R.id.spnrImageSize, R.array.ArrayImageSize, settings.imageSize);
        setSpinnerAdapter(view, R.id.spnrImageColor, R.array.ArrayImageColor, settings.imageColor);
        setSpinnerAdapter(view, R.id.spnrImageType, R.array.ArrayImageType, settings.imageType);

        if (settings.website != null) {
            EditText etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
            etSiteFilter.setText(settings.website);
        }

        Button saveButton = (Button) view.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);

                SearchSettings settings = SearchSettings.getInstance();
                settings.saveSettings(getSpinnerValue(view, R.id.spnrImageSize),
                        getSpinnerValue(view, R.id.spnrImageColor),
                        getSpinnerValue(view, R.id.spnrImageType),
                        etSiteFilter.getText().toString());

                listener.onSaveSettingsClicked();
                dismiss();
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void setSpinnerAdapter(View view, int spinnerResId, int textArrayResId, String value) {

        Spinner spinner = (Spinner) view.findViewById(spinnerResId);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                textArrayResId, R.layout.spinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        if (value != null) {
            int spinnerPosition = arrayAdapter.getPosition(value);
            spinner.setSelection(spinnerPosition);
        }
    }

    private String getSpinnerValue(View view, int spinnerResId) {
        Spinner spinner = (Spinner) view.findViewById(spinnerResId);
        return spinner.getSelectedItem().toString();
    }

}
