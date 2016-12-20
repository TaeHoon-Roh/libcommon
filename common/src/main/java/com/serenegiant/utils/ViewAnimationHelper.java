package com.serenegiant.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.serenegiant.common.R;

/**
 * Created by saki on 2016/10/13.
 *
 */
public class ViewAnimationHelper {
	private static final String TAG = ViewAnimationHelper.class.getSimpleName();

	private static final long DEFAULT_DURATION_MS = 500L;

	public static final int ANIMATION_UNKNOWN = -1;
	public static final int ANIMATION_FADE_OUT = 0;
	public static final int ANIMATION_FADE_IN = 1;
	public static final int ANIMATION_ZOOM_IN = 2;
	public static final int ANIMATION_ZOOM_OUT = 3;

	public interface ViewAnimationListener {
		public void onAnimationStart(@NonNull final Animator animator, @NonNull final View target, final int animationType);
		public void onAnimationEnd(@NonNull final Animator animator, @NonNull final View target, final int animationType);
		public void onAnimationCancel(@NonNull final Animator animator, @NonNull final View target, final int animationType);
	}

	/**
	 * アルファ値を0→1まで変化(Viewをフェードイン)させる
	 * @param target
	 * @param duration 0以下ならデフォルト値(0.5秒)
	 * @param startDelay
	 * @param listener
	 */
	@SuppressLint("NewApi")
	public static void fadeIn(final View target, final long duration, final long startDelay, final ViewAnimationListener listener) {
//		if (DEBUG) Log.v(TAG, "fadeIn:target=" + target);
		if (target == null) return;
		UIThreadHelper.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				target.setVisibility(View.VISIBLE);
				target.setTag(R.id.anim_type, ANIMATION_FADE_IN);	// フェードインの時の印
				target.setTag(R.id.anim_listener, listener);
				target.setScaleX(1.0f);
				target.setScaleY(1.0f);
				target.setAlpha(0.0f);
				final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "alpha", 0f, 1f );
				objectAnimator.addListener(mAnimatorListener);
				if (BuildCheck.isJellyBeanMR2())
					objectAnimator.setAutoCancel(true);		// API >= 18 同じターゲットに対して別のAnimatorが開始したら自分をキャンセルする
				objectAnimator.setDuration(duration > 0 ? duration : DEFAULT_DURATION_MS);
				objectAnimator.setStartDelay(startDelay > 0 ? startDelay : 0);	// 開始までの時間
			    objectAnimator.start();						// アニメーションを開始
			}
		});
	}

	/**
	 * アルファ値を1→0まで変化(Viewをフェードアウト)させる
	 * @param target
	 * @param duration 0以下ならデフォルト値(0.5秒)
	 * @param startDelay
	 * @param listener
	 */
	@SuppressLint("NewApi")
	public static void fadeOut(final View target, final long duration, final long startDelay, final ViewAnimationListener listener) {
//		if (DEBUG) Log.v(TAG, "fadeOut,target=" + target);
		if ((target != null) && (target.getVisibility() == View.VISIBLE)) {
			UIThreadHelper.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					target.setTag(R.id.anim_type, ANIMATION_FADE_OUT);	// フェードアウトの印
					target.setTag(R.id.anim_listener, listener);
					target.setScaleX(1.0f);
					target.setScaleY(1.0f);
					target.setAlpha(1.0f);
					final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "alpha", 1f, 0f );
					objectAnimator.addListener(mAnimatorListener);
					if (BuildCheck.isAndroid4_3())
						objectAnimator.setAutoCancel(true);		// API >= 18 同じターゲットに対して別のAnimatorが開始したら自分をキャンセルする
					objectAnimator.setDuration(duration > 0 ? duration : DEFAULT_DURATION_MS);
					objectAnimator.setStartDelay(startDelay > 0 ? startDelay : 0);	// 開始までの時間
					objectAnimator.start();						// アニメーションを開始
				}
			});
		}
	}

	/**
	 * スケールを0→1まで変化(Viewをズームイン)させる
	 * @param target
	 * @param duration 0以下ならデフォルト値(0.5秒)
	 * @param startDelay
	 * @param listener
	 */
	@SuppressLint("NewApi")
	public static void zoomIn(final View target, final long duration, final long startDelay, final ViewAnimationListener listener) {
//		if (DEBUG) Log.v(TAG, "zoomIn:target=" + target);
		if (target == null) return;
		UIThreadHelper.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				target.setVisibility(View.VISIBLE);
				target.setTag(R.id.anim_type, ANIMATION_ZOOM_IN);	// ズームインの時の印
				target.setTag(R.id.anim_listener, listener);
				target.setScaleX(0.0f);
				target.setScaleY(0.0f);
				target.setAlpha(1.0f);
				final PropertyValuesHolder scale_x = PropertyValuesHolder.ofFloat( "scaleX", 0.01f, 1f);
				final PropertyValuesHolder scale_y = PropertyValuesHolder.ofFloat( "scaleY", 0.01f, 1f);
				final ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(target, scale_x, scale_y);
				objectAnimator.addListener(mAnimatorListener);
				if (BuildCheck.isJellyBeanMR2())
					objectAnimator.setAutoCancel(true);		// API >= 18 同じターゲットに対して別のAnimatorが開始したら自分をキャンセルする
				objectAnimator.setDuration(duration > 0 ? duration : DEFAULT_DURATION_MS);
				objectAnimator.setStartDelay(startDelay > 0 ? startDelay : 0);	// 開始までの時間
			    objectAnimator.start();						// アニメーションを開始
			}
		});
	}

	/**
	 * スケールを1→0まで変化(Viewをズームアウト)させる
	 * @param target
	 * @param duration 0以下ならデフォルト値(0.5秒)
	 * @param startDelay
	 * @param listener
	 */
	@SuppressLint("NewApi")
	public static void zoomOut(final View target, final long duration, final long startDelay, final ViewAnimationListener listener) {
//		if (DEBUG) Log.v(TAG, "zoomIn:target=" + target);
		if (target == null) return;
		UIThreadHelper.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				target.setVisibility(View.VISIBLE);
				target.setTag(R.id.anim_type, ANIMATION_ZOOM_OUT);	// ズームアウトの時の印
				target.setTag(R.id.anim_listener, listener);
				target.setScaleX(1.0f);
				target.setScaleY(1.0f);
				target.setAlpha(1.0f);
				final PropertyValuesHolder scale_x = PropertyValuesHolder.ofFloat( "scaleX", 1f, 0f);
				final PropertyValuesHolder scale_y = PropertyValuesHolder.ofFloat( "scaleY", 1f, 0f);
				final ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(target, scale_x, scale_y);
				objectAnimator.addListener(mAnimatorListener);
				if (BuildCheck.isJellyBeanMR2())
					objectAnimator.setAutoCancel(true);		// API >= 18 同じターゲットに対して別のAnimatorが開始したら自分をキャンセルする
				objectAnimator.setDuration(duration > 0 ? duration : DEFAULT_DURATION_MS);
				objectAnimator.setStartDelay(startDelay > 0 ? startDelay : 0);	// 開始までの時間
				objectAnimator.start();						// アニメーションを開始
			}
		});
	}

	/**
	 * アニメーション用コールバックリスナー
	 */
	private static final Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
		@Override
		public void onAnimationStart(final Animator animator) {
			onAnimation(animator, 0);
		}
		@Override
		public void onAnimationEnd(final Animator animator) {
			onAnimation(animator, 1);
		}
		@Override
		public void onAnimationCancel(final Animator animator) {
			onAnimation(animator, 2);
		}
		@Override
		public void onAnimationRepeat(final Animator animation) {
		}
	};

	private static void onAnimation(final Animator animator, final int event) {
		if (animator instanceof ObjectAnimator) {
			final ObjectAnimator anim = (ObjectAnimator)animator;
			final View target = (View)anim.getTarget();
			if (target == null) return;	// これはありえないはずだけど

			final ViewAnimationListener listener = (ViewAnimationListener)target.getTag(R.id.anim_listener);
			final int animType;
			try {
				animType = (Integer)target.getTag(R.id.anim_type);
			} catch (final Exception e) {
				return;
			}
			if (listener != null) {
				UIThreadHelper.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							switch (event) {
							case 0:
								listener.onAnimationStart(anim, target, animType);
								break;
							case 1:
								listener.onAnimationEnd(anim, target, animType);
								break;
							case 2:
								listener.onAnimationCancel(anim, target, animType);
								break;
							}
						} catch (final Exception e) {
							Log.w(TAG, e);
						}
					}
				});
			}
		}
	}
}
