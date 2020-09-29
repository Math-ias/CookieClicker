package com.cookie.mocks;

import com.cookie.ProductionBuff;
import com.cookie.ProductionEffect;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * A Mocked Clicking Buff to apply for testing.
 */
public class MockClickingBuff implements ProductionBuff {

  private final long timeLeft;
  private final long timeTotal;

  /**
   * Create a new MockClickingBuff.
   *
   * @param timeLeft  The amount of time left in this buff in ticks.
   * @param timeTotal The amount of time total for this buff in ticks.
   * @throws IllegalArgumentException If the timeLeft is more than the timeTotal, or either times
   *                                  are non-positive.
   */
  public MockClickingBuff(long timeLeft, long timeTotal) {
    if (timeLeft <= 0 || timeTotal <= 0) {
      throw new IllegalArgumentException("Unable to use non-positive times.");
    } else if (timeLeft > timeTotal) {
      throw new IllegalArgumentException("Unable to use timeLeft that is more than timeTotal.");
    }
    this.timeLeft = timeLeft;
    this.timeTotal = timeTotal;
  }

  @Override
  public long getTimeLeft() {
    return timeLeft;
  }

  @Override
  public long getTimeTotal() {
    return timeTotal;
  }

  @Override
  public Optional<ProductionBuff> warp(long ticks) {
    if (ticks < 0) {
      throw new IllegalArgumentException("Unable to warp by negative amount of time.");
    }
    if (timeLeft - ticks <= 0) {
      return Optional.empty();
    } else {
      return Optional.of(new MockClickingBuff(timeLeft - ticks, timeTotal));
    }
  }

  @Override
  public Collection<ProductionEffect> getEffects() {
    return Collections.singleton(MockClickingEffects.TIMES2);
  }
}
