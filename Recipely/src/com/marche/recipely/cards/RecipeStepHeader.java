package com.marche.recipely.cards;

import it.gmariotti.cardslib.library.internal.CardHeader;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marche.recipely.R;

/**
 * This class provides an example of custom of inner layout in Header.
 * It uses carddemo_example_card1_header_inner.
 * <p/>
 * You have to override the {@link #setupInnerViewElements(android.view.ViewGroup, android.view.View)});
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class RecipeStepHeader extends CardHeader {
	
	int i = -1;

    public RecipeStepHeader(Context context, int i) {
        super(context, R.layout.title_card_header);       
        this.i = i+1;      
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null) {
            TextView t1 = (TextView) view.findViewById(R.id.text_exmple_card1);
            if (t1 != null){
                t1.setText("Step " + i);
            }
        }
    }
}
