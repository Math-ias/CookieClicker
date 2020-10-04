package com.cookie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A SimpleCookieClicker is a simple approach to cookie clicker state management.
 * <p>
 * Upgrades are priced at unit price. But the price of buildings is scaled by a factor of 1.15 each
 * time. See <a href="https://cookieclicker.fandom.com/wiki/Building">the wiki for more
 * details.</a>
 * <p>
 */
public class SimpleCookieClicker implements CookieClicker {

  // The following are properties we must store as part of the save.
  final long ticks;
  final Map<BuildingType, Integer> inventory;
  final Set<ProductionUpgrade> upgrades;
  final List<ProductionBuff> buffs;
  final double clickingRate;
  final double currentBank;
  final double cookiesBaked;
  final double handmadeCookies;
  final double cookieClicks;

  // Calculated measures.
  final Map<BuildingType, Double> buildingRates;
  final double cookiesPerClick;

  /**
   * Create a new instance of a SimpleCookieClicker with the following properties.
   *
   * @param ticks           The non-negative number of ticks elapsed in this game.
   * @param inventory       The current non-null building inventory of this game.
   * @param upgrades        The current non-null upgrade inventory of this game.
   * @param buffs           The current non-null collection of buffs.
   * @param clickingRate    The non-negative clicking rate set for this game.
   * @param currentBank     The non-negative current amount of cookies owned.
   * @param cookiesBaked    The non-negative total amount of cookies made all-time.
   * @param handmadeCookies The non-negative total amount of cookies made by clicking the big
   *                        cookie.
   * @param cookieClicks    The non-negative total number of times the big cookie has been clicked.
   * @throws IllegalArgumentException If ticks, clickingRate, currentBank, cookiesBaked,
   *                                  handmadeCookies, or cookieClicks are negative.
   * @throws NullPointerException     If inventory, upgrades, or buffs are null.
   */
  public SimpleCookieClicker(long ticks,
                             Map<BuildingType, Integer> inventory,
                             Set<ProductionUpgrade> upgrades,
                             List<ProductionBuff> buffs,
                             double clickingRate,
                             double currentBank,
                             double cookiesBaked, double handmadeCookies, double cookieClicks) {
    // We can start with input validation.
    if (ticks < 0 ||
            clickingRate < 0 ||
            currentBank < 0 ||
            cookiesBaked < 0 ||
            handmadeCookies < 0 ||
            cookieClicks < 0) {
      throw new IllegalArgumentException("Cannot use negative ticks," +
              " currentBank, cookiesBaked, handmadecookies, or cookieClicks.");
    }

    // Now we mix field assignment with validation.
    this.ticks = ticks;
    this.inventory = Objects.requireNonNull(inventory, "Null building inventory.");
    this.upgrades = Objects.requireNonNull(upgrades, "Null upgrades inventory.");
    this.buffs = Objects.requireNonNull(buffs, "Null buffs inventory.");
    this.clickingRate = clickingRate;
    this.currentBank = currentBank;
    this.cookiesBaked = cookiesBaked;
    this.handmadeCookies = handmadeCookies;
    this.cookieClicks = cookieClicks;

    // Now we have to calculate building rates, and cookies per click.
    // This gets complicated with effects.

    // There are a few sources of effects, so I combine them all first.
    Stream<ProductionEffect> effects = Stream.concat(upgrades.stream()
                    .flatMap(upgrade -> upgrade.getEffects().stream()),
            getActiveProductionBuffs().stream().flatMap(buff -> buff.getEffects().stream()));

    // Effect are bucketed into if they are a BuildingProductionEffect or not.
    List<BuildingProductionEffect> buildingProductionEffects = new ArrayList<>();
    List<ClickingProductionEffect> clickingProductionEffects = new ArrayList<>();

    // A visitor to consume effects and drop them into the correct bucket.
    // We need to do this because building effects are processed first,
    // and then clicking production effects are processed second.

    ProductionEffectVisitor<Boolean> bucketVisitor = new ProductionEffectVisitor<>() {
      @Override
      public Boolean applyToBuildingProductionEffect(BuildingProductionEffect bpe) {
        return buildingProductionEffects.add(bpe);
      }

      @Override
      public Boolean applyToClickingProductionEffect(ClickingProductionEffect cpe) {
        return clickingProductionEffects.add(cpe);
      }
    };

    effects.forEach(effect -> effect.accept(bucketVisitor));

    // First we process building production terms.
    // We bucket by building type, and then by term.
    // And while we are here we compute the constants.
    Map<BuildingType, Map<ProductionEffect.TERM, List<Double>>> buildingNumbers = buildingProductionEffects.stream()
            .collect(Collectors.groupingBy(BuildingProductionEffect::getTarget,
                    Collectors.groupingBy(BuildingProductionEffect::getTerm,
                            Collectors.mapping(effect -> effect.getEffect(this),
                                    Collectors.toList()))));

    // Now we combine the results for each building to create a new buildings rate.
    Map<BuildingType, Double> newBuildingRates = new LinkedHashMap<>();
    for (Map.Entry<BuildingType, Integer> buildingEntry : inventory.entrySet()) {
      Map<ProductionEffect.TERM, List<Double>> effectNumbers = buildingNumbers.getOrDefault(buildingEntry.getKey(),
              Collections.EMPTY_MAP);

      List<Double> constants = effectNumbers.getOrDefault(ProductionEffect.TERM.CONSTANT, Collections.EMPTY_LIST);
      List<Double> multipliers = effectNumbers.getOrDefault(ProductionEffect.TERM.MULTIPLIER, Collections.EMPTY_LIST);

      double constant = constants.stream()
              .reduce(0.0, (a, b) -> a + b);
      double multiplier = multipliers.stream()
              .reduce(1.0, (a, b) -> a * b);

      newBuildingRates.put(buildingEntry.getKey(),
              buildingEntry.getValue() *
                      (multiplier * buildingEntry.getKey().rate() + constant));
    }
    this.buildingRates = Map.copyOf(newBuildingRates);

    // Now we can process clicking production since it could depend on building production.
    // We bucket by the term, and then compute the constants.
    // It's safe to let the effect query building rates now since we set them above.
    Map<ProductionEffect.TERM, List<Double>> clickingNumbers = clickingProductionEffects.stream()
            .collect(Collectors.groupingBy(ClickingProductionEffect::getTerm,
                    Collectors.mapping(effect -> effect.getEffect(this), Collectors.toList())));

    List<Double> clickingConstants = clickingNumbers.getOrDefault(ProductionEffect.TERM.CONSTANT, Collections.EMPTY_LIST);
    List<Double> clickingMultipliers = clickingNumbers.getOrDefault(ProductionEffect.TERM.MULTIPLIER, Collections.EMPTY_LIST);

    this.cookiesPerClick = 1 *
            clickingMultipliers.stream().reduce(1.0, (a, b) -> a * b) +
            clickingConstants.stream().reduce(0.0, (a, b) -> a + b);
  }

  /**
   * Create a fresh game instance of a SimpleCookieClicker.
   */
  public SimpleCookieClicker() {
    this(0,
            Collections.EMPTY_MAP,
            Collections.EMPTY_SET,
            Collections.EMPTY_LIST,
            0,
            0,
            0,
            0,
            0);
  }

  @Override
  public CookieClicker warp(long ticks) {
    // Input validation.
    if (ticks < 0) {
      throw new IllegalArgumentException("Unable to warp by negative amount of ticks.");
    } else if (ticks == 0) {
      return this; // Easy way out.
    }

    // It's not enough to extrapolate from current rates.
    // Instead, we need to seek to where those calculated measures change.
    // And then warp again.
    OptionalLong nextBuffExpiration = getActiveProductionBuffs().stream()
            .mapToLong(ProductionBuff::getTimeLeft)
            .min();
    long ticksToWarp = nextBuffExpiration.isPresent() ?
            Math.min(ticks, nextBuffExpiration.getAsLong()) : ticks;

    double newCookieClicks = clickingRate * ticksToWarp;
    double newHandmadeCookies = newCookieClicks * getCookiesPerClick();
    double newBuildingBakedCookies = buildingRates.values().stream()
            .mapToDouble(rate -> rate * ticksToWarp)
            .sum();
    double newlyBakedCookies = newHandmadeCookies + newBuildingBakedCookies;
    List<ProductionBuff> newBuffs = buffs.stream()
            .flatMap(buff -> buff.warp(ticksToWarp).stream())
            .collect(Collectors.toList());

    // It's safe to warp without fearing for infinite recursion because we have a zero base case.

    return new SimpleCookieClicker(this.ticks + ticksToWarp,
            this.inventory,
            this.upgrades,
            newBuffs,
            this.clickingRate,
            this.currentBank + newlyBakedCookies,
            this.cookiesBaked + newlyBakedCookies,
            this.handmadeCookies + newHandmadeCookies,
            this.cookieClicks + newCookieClicks)
            .warp(ticks - ticksToWarp);
  }

  @Override
  public CookieClicker barterBuildings(BuildingType buildingType, int amount) {
    Objects.requireNonNull(buildingType);
    int typeOwned = inventory.getOrDefault(buildingType, 0);
    if (typeOwned + amount < 0) { // Prevent amount exception on price check.
      throw new IllegalArgumentException("The number of buildings does not permit this sale.");
    }

    double bankCharge = getBarteringBuildingAmount(buildingType, amount);
    if (currentBank - bankCharge < 0) {
      throw new IllegalArgumentException("This sale is unaffordable.");
    } else {
      Map<BuildingType, Integer> inventoryCopy = new LinkedHashMap<>(this.inventory);
      inventoryCopy.put(buildingType, typeOwned + amount);
      return new SimpleCookieClicker(this.ticks,
              Map.copyOf(inventoryCopy),
              this.upgrades,
              this.buffs,
              this.clickingRate,
              this.currentBank - bankCharge,
              this.cookiesBaked, this.handmadeCookies, this.cookieClicks);
    }
  }

  @Override
  public CookieClicker buyUpgrade(ProductionUpgrade upgrade) {
    Objects.requireNonNull(upgrade);

    double upgradePrice = getUpgradePrice(upgrade);
    if (upgrades.contains(upgrade)) {
      throw new IllegalArgumentException("Upgrade already owned cannot purchase.");
    } else if (!upgrade.purchasable(this)) {
      throw new IllegalArgumentException("Upgrade is not purchasable.");
    } else if (upgradePrice > currentBank) {
      throw new IllegalArgumentException("Unable to afford the current upgrade purchase.");
    } else {
      Set<ProductionUpgrade> upgradesCopy = new LinkedHashSet<>(this.upgrades);
      upgradesCopy.add(upgrade);
      return new SimpleCookieClicker(this.ticks,
              this.inventory,
              Set.copyOf(upgradesCopy),
              this.buffs,
              this.clickingRate,
              this.currentBank - upgradePrice,
              this.cookiesBaked, this.handmadeCookies, this.cookieClicks);
    }
  }

  @Override
  public CookieClicker registerBuff(ProductionBuff buff) {
    Objects.requireNonNull(buff);

    List<ProductionBuff> buffsCopy = new LinkedList<>(this.buffs);
    buffsCopy.add(buff);
    return new SimpleCookieClicker(this.ticks,
            this.inventory,
            this.upgrades,
            List.copyOf(buffsCopy),
            this.clickingRate,
            this.currentBank,
            this.cookiesBaked, this.handmadeCookies, this.cookieClicks);
  }

  @Override
  public CookieClicker setClickingRate(double rate) {
    if (rate < 0) {
      throw new IllegalArgumentException("Unable to set negative clicking rate.");
    }
    return new SimpleCookieClicker(this.ticks,
            this.inventory,
            this.upgrades,
            this.buffs,
            rate,
            this.currentBank,
            this.cookiesBaked, this.handmadeCookies, this.cookieClicks);
  }

  @Override
  public CookieClicker adjustBank(double cookies) {
    if (this.currentBank + cookies < 0) {
      throw new IllegalArgumentException("Cannot ask for illegal bank state.");
    } else {
      return new SimpleCookieClicker(this.ticks,
              this.inventory,
              this.upgrades,
              this.buffs,
              this.clickingRate,
              this.currentBank + cookies,
              this.cookiesBaked + Math.max(cookies, 0),
              this.handmadeCookies,
              this.cookieClicks);
    }
  }

  @Override
  public long getTicks() {
    return this.ticks;
  }

  @Override
  public double getCurrentBank() {
    return this.currentBank;
  }

  @Override
  public Map<BuildingType, Integer> getBuildingInventory() {
    return Map.copyOf(this.inventory);
  }

  @Override
  public Set<ProductionUpgrade> getProductionUpgrades() {
    return Set.copyOf(this.upgrades);
  }

  @Override
  public Collection<ProductionBuff> getActiveProductionBuffs() {
    return this.buffs.stream()
            .filter(buff -> buff.getTimeLeft() > 0)
            .collect(Collectors.toUnmodifiableList());
  }

  @Override
  public double getCookiesBaked() {
    return this.cookiesBaked;
  }

  @Override
  public double getHandmadeCookies() {
    return this.handmadeCookies;
  }

  @Override
  public double getCookieClicks() {
    return this.cookieClicks;
  }

  @Override
  public double getClickingRate() {
    return this.clickingRate;
  }

  @Override
  public double getCookiesPerClick() {
    return this.cookiesPerClick;
  }

  @Override
  public double getRate(BuildingType target) {
    Objects.requireNonNull(target);
    return buildingRates.getOrDefault(target, 0.0);
  }

  private static double PRICE_GROWTH_FACTOR = 1.15;
  private static double REFUND_REDUCTION = 0.25;

  @Override
  public double getBarteringBuildingAmount(BuildingType target, int amount) {
    Objects.requireNonNull(target);

    int targetOwned = inventory.getOrDefault(target, amount);
    if (amount < 0 && amount < -targetOwned) {
      throw new IllegalArgumentException("Unable to provide refund price for more buildings than are currently owned.");
    }

    if (amount > 0) {
      // Price scaling formulas from https://cookieclicker.fandom.com/wiki/Building.
      return Math.ceil(target.unitPrice()
              * Math.pow(PRICE_GROWTH_FACTOR, targetOwned)
              * (Math.pow(PRICE_GROWTH_FACTOR, amount) - 1)
              / (PRICE_GROWTH_FACTOR - 1));
    } else if (amount == 0) {
      return 0;
    } else {
      // The amount refunded is a quarter of the current price.
      return -Math.ceil(target.unitPrice() * REFUND_REDUCTION
              * Math.pow(PRICE_GROWTH_FACTOR, targetOwned + amount)
              * (Math.pow(PRICE_GROWTH_FACTOR, -amount) - 1)
              / (PRICE_GROWTH_FACTOR - 1));
    }
  }

  @Override
  public double getUpgradePrice(ProductionUpgrade upgrade) {
    // We do no upgrade price adjustment.
    Objects.requireNonNull(upgrade);
    if (upgrades.contains(upgrade)) {
      throw new IllegalArgumentException("Cannot purchase an upgrade already purchased.");
    }

    return Objects.requireNonNull(upgrade).price();
  }
}
