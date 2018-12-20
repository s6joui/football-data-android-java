package tech.joeyck.livefootball.utilities;

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import android.view.View;
import android.widget.ImageView;

public class AnimationUtils {

    public static void loopAnimation(ImageView imageView){
        imageView.setVisibility(View.VISIBLE);
        Drawable drawable = imageView.getDrawable();
        if (Build.VERSION.SDK_INT >= 23 && drawable instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable animation = (AnimatedVectorDrawable)drawable;
            animation.registerAnimationCallback(new Animatable2.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    animation.start();
                }
            });
            animation.start();
        } else if (drawable instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat animation = (AnimatedVectorDrawableCompat)drawable;
            animation.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    animation.start();
                }
            });
            animation.start();
        }
    }

    public static void stopAnimation(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if (Build.VERSION.SDK_INT >= 23 && drawable instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable animation = (AnimatedVectorDrawable)drawable;
            animation.stop();
            animation.clearAnimationCallbacks();
        } else if (drawable instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat animation = (AnimatedVectorDrawableCompat)drawable;
            animation.stop();
            animation.clearAnimationCallbacks();
        }
        imageView.setVisibility(ImageView.INVISIBLE);
    }

}
