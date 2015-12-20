package ch.imlee.maturarbeit.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import ch.imlee.maturarbeit.R;

public class PlayerTypeInfo {
    private final String NAME;
    private final String DESCRIPTION;
    private final Bitmap BITMAP;

    public PlayerTypeInfo(String name, String description, Bitmap bitmap){
        NAME = name;
        DESCRIPTION = description;
        BITMAP = bitmap;
    }

    public void setActive(){
        ChooseActivity.playerTypeDescription.setText(DESCRIPTION);
        ChooseActivity.relativeLayout.refreshDrawableState();

        int bitmapSize = ChooseActivity.playerTypeDescription.getTop() - ChooseActivity.playerTypeGroup.getBottom();
        Log.d("bitmapSize", "bitmapSize is " + bitmapSize);
        if(bitmapSize <= 0){
            ChooseActivity.playerTypeImage.setVisibility(View.GONE);
        } else {
            ChooseActivity.playerTypeImage.setVisibility(View.VISIBLE);
            ChooseActivity.playerTypeImage.setImageBitmap(Bitmap.createScaledBitmap(BITMAP, bitmapSize, bitmapSize, false));
        }

    }

    public String getName(){
        return NAME;
    }
}
