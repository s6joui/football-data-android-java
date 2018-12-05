package tech.joeyck.livefootball.utilities;

import android.animation.ObjectAnimator;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.view.View;
import android.widget.ImageView;

import tech.joeyck.livefootball.R;

public class AnimationUtils {

    public static void loopAnimation(ImageView imageView){
        imageView.setVisibility(View.VISIBLE);
        imageView.animate().alpha(1).setStartDelay(100);
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
        imageView.animate().alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(ImageView.INVISIBLE);
            }
        });
    }

}
