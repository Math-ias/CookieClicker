package com.cookie.mocks;

import com.cookie.CookieClicker;
import com.cookie.ClickingProductionEffect;

/**
 * A collection of clicking effects.
 */
public enum MockClickingEffects implements ClickingProductionEffect {
  TIMES2;

  @Override
  public double getEffect(CookieClicker stats) {
    return 2;
  }

  @Override
  public TERM getTerm() {
    return TERM.MULTIPLIER;
  }
}