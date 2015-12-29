package ch.imlee.maturarbeit.activities;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;


public class PlayerTypeInfo extends ChooseActivity{
    private final String NAME;
    private final String DESCRIPTION;
    private final Bitmap BITMAP;
    int bitmapSize;

    public PlayerTypeInfo(String name, String description, Bitmap bitmap){
        NAME = name;
        DESCRIPTION = description;
        BITMAP = bitmap;
    }

    public void setActive(){
        playerTypeDescription.setText(DESCRIPTION);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setBitmapSize(playerTypeDescription.getTop() - playerTypeGroup.getBottom());
                Log.d("bitmapSize", "bitmapSize is " + bitmapSize);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(bitmapSize <= 0){
                            playerTypeImage.setVisibility(View.GONE);
                        } else {
                            playerTypeImage.setVisibility(View.VISIBLE);
                            playerTypeImage.setImageBitmap(Bitmap.createScaledBitmap(BITMAP, bitmapSize, bitmapSize, false));
                        }
                    }
                });
            }
        }).start();
    }

    private void setBitmapSize(int size){
        bitmapSize = size;
    }

    public String getName(){
        return NAME;
    }
}
