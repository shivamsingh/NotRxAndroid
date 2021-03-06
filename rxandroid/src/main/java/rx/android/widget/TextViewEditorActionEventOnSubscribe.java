package rx.android.widget;

import android.view.KeyEvent;
import android.widget.TextView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

import static rx.android.internal.Preconditions.checkUiThread;

final class TextViewEditorActionEventOnSubscribe
    implements Observable.OnSubscribe<TextViewEditorActionEvent> {
  private final TextView view;
  private final Func1<? super TextViewEditorActionEvent, Boolean> handled;

  public TextViewEditorActionEventOnSubscribe(TextView view,
      Func1<? super TextViewEditorActionEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super TextViewEditorActionEvent> subscriber) {
    checkUiThread();

    TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
        TextViewEditorActionEvent event = TextViewEditorActionEvent.create(v, actionId, keyEvent);
        if (handled.call(event)) {
          subscriber.onNext(event);
          return true;
        }
        return false;
      }
    };

    Subscription subscription = Subscriptions.create(new Action0() {
      @Override public void call() {
        view.setOnEditorActionListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnEditorActionListener(listener);
  }
}
