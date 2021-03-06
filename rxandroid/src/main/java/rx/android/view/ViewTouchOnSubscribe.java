package rx.android.view;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

import static rx.android.internal.Preconditions.checkUiThread;

final class ViewTouchOnSubscribe implements Observable.OnSubscribe<MotionEvent> {
  private final View view;
  private final Func1<? super MotionEvent, Boolean> handled;

  public ViewTouchOnSubscribe(View view, Func1<? super MotionEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super MotionEvent> subscriber) {
    checkUiThread();

    View.OnTouchListener listener = new View.OnTouchListener() {
      @Override public boolean onTouch(View v, @NonNull MotionEvent event) {
        if (handled.call(event)) {
          subscriber.onNext(event);
          return true;
        }
        return false;
      }
    };

    Subscription subscription = Subscriptions.create(new Action0() {
      @Override public void call() {
        view.setOnTouchListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnTouchListener(listener);
  }
}
