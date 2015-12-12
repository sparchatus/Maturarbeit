package ch.imlee.maturarbeit.activities;

import android.graphics.Bitmap;

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
        ChooseActivity.playerTypeImage.setImageBitmap(BITMAP);
        ChooseActivity.playerTypeDescription.setText(DESCRIPTION);
    }

    public String getName(){
        return NAME;
    }
}
