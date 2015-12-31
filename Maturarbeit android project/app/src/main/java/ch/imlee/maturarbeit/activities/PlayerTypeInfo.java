package ch.imlee.maturarbeit.activities;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;


public class PlayerTypeInfo extends ChooseActivity{
    private final String NAME;
    private final String DESCRIPTION;
    private final Bitmap BITMAP;
    private int bitmapSize;

    public PlayerTypeInfo(String name, String description, Bitmap bitmap){
        NAME = name;
        DESCRIPTION = description;
        BITMAP = bitmap;
    }

    // this method adds an image and a description of the selected PlayerType to the screen
    public void setActive(){
        playerTypeDescription.setText(DESCRIPTION);
        // after we set the description, we need a small delay for the System Thread to update the position of the playerTypeDescription TextView
        // we add the delay in a separate Thread to not pause the System Thread
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
                        // if the calculated bitmap size would be smaller than 0, it would throw an Exception, so only display it if it's > 0
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
