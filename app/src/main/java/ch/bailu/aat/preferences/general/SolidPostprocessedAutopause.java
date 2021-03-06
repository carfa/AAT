package ch.bailu.aat.preferences.general;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidAutopause;

public class SolidPostprocessedAutopause extends SolidAutopause {
    protected final static String KEY="pautopause";


    public SolidPostprocessedAutopause(Context c) {
        super(c, KEY, 0);
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_post_autopause);
    }
}
