package ch.bailu.aat.preferences.presets;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.OldSolidMET;
import ch.bailu.aat.preferences.SolidString;
import ch.bailu.aat.preferences.Storage;

public class SolidMET extends SolidString {
    private final int preset;

    public SolidMET(Context c, int i) {
        super(c, SolidMET.class.getSimpleName()+ "_" + i);
        preset = i;
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_met);
    }

    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();

        if (Storage.DEF_VALUE.equals(r)) {
            r = new OldSolidMET(getContext(), preset).getValueAsString();
        }
        return r;
    }




    public float getMETValue() {
        String val = getValueAsString();

        final int from = 0; int to = val.indexOf(' ');

        float r = 0f;


        if (to > from) {
            try {
                String met = val.substring(from, to);
                r = Float.valueOf(met);
            } catch (NumberFormatException e) {
                r = 0f;
            }
        }

        if (r > 20f || r < 0f) r = 0f;
        return r;
    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {

        String[] array = getContext().getResources().getStringArray(R.array.p_met_list);

        Collections.addAll(list, array);

        return list;
    }
}
