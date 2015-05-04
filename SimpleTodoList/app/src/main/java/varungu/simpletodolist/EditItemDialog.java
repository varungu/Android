package varungu.simpletodolist;

import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Varun on 4/24/2015.
 */
public class EditItemDialog extends DialogFragment {
    public EditItemDialog() {
        // Empty constructor required for DialogFragment
    }

    public static EditItemDialog newInstance(Item item) {
        EditItemDialog frag = new EditItemDialog();
        Bundle args = new Bundle();
        args.putSerializable();
        frag.setArguments(args);
        return frag;
    }

}
