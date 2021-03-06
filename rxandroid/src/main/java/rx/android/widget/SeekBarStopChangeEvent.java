package rx.android.widget;

import android.support.annotation.NonNull;
import android.widget.SeekBar;

public final class SeekBarStopChangeEvent extends SeekBarChangeEvent {
  public static SeekBarStopChangeEvent create(@NonNull SeekBar view) {
    return new SeekBarStopChangeEvent(view);
  }

  private SeekBarStopChangeEvent(@NonNull SeekBar view) {
    super(view);
  }

  @Override public boolean equals(Object o) {
    return o instanceof SeekBarStopChangeEvent && ((SeekBarStopChangeEvent) o).view() == view();
  }

  @Override public int hashCode() {
    return view().hashCode();
  }

  @Override public String toString() {
    return "SeekBarStopChangeEvent{view=" + view() + '}';
  }
}
