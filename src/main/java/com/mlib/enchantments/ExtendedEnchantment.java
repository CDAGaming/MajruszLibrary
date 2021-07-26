package com.mlib.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;

/** Class removing redundancy and repetition from enchantments. */
public abstract class ExtendedEnchantment extends Enchantment {
	private static final int DISABLE_ENCHANTMENT_VALUE = 9001;
	protected final String registerName;
	private int differenceBetweenMinimumAndMaximum = 10;
	private int maximumEnchantmentLevel = 1;
	private MinimumEnchantabilityCalculator minimumEnchantabilityCalculator = level->level;

	protected ExtendedEnchantment( String registerName, Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot[] equipmentSlots ) {
		super( rarity, enchantmentCategory, equipmentSlots );

		this.registerName = registerName;
	}

	protected ExtendedEnchantment( String registerName, Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot equipmentSlot ) {
		this( registerName, rarity, enchantmentCategory, new EquipmentSlot[]{ equipmentSlot } );
	}

	@Override
	public int getMaxLevel() {
		return this.maximumEnchantmentLevel;
	}

	@Override
	public int getMinCost( int level ) {
		return isDisabled() ? DISABLE_ENCHANTMENT_VALUE : this.minimumEnchantabilityCalculator.getMinimumLevel( level );
	}

	@Override
	public int getMaxCost( int level ) {
		return this.getMinCost( level ) + this.differenceBetweenMinimumAndMaximum;
	}

	@Override
	public boolean canEnchant( ItemStack stack ) {
		return !isDisabled() && super.canEnchant( stack );
	}

	@Override
	public boolean isTradeable() {
		return !isDisabled() && super.isTradeable();
	}

	@Override
	public boolean isDiscoverable() {
		return !isDisabled() && super.isDiscoverable();
	}

	@Override
	public boolean canApplyAtEnchantingTable( ItemStack stack ) {
		return !isDisabled() && super.canApplyAtEnchantingTable( stack );
	}

	@Override
	public boolean isAllowedOnBooks() {
		return !isDisabled() && super.isAllowedOnBooks();
	}

	/** Registers given enchantment only if player has not disabled it. */
	public void register( DeferredRegister< Enchantment > enchantments ) {
		enchantments.register( this.registerName, ()->this );
	}

	/** Checks whether the enchantment is disabled. */
	protected abstract boolean isDisabled();

	/**
	 Setting up difference between minimum and maximum enchantability.
	 For example when this value is set to 20 and minimum level is equal 10
	 then player can receive this enchantment only when enchanting with level between 10 and 30.
	 */
	protected void setDifferenceBetweenMinimumAndMaximum( int differenceInLevels ) {
		this.differenceBetweenMinimumAndMaximum = Math.max( 1, differenceInLevels );
	}

	/**
	 Setting up minimum enchantability calculator.
	 By default in minecraft this is the same as getMinEnchantability() but
	 to work with disabling easily enchantments this needs to be a separate function.
	 */
	protected void setMinimumEnchantabilityCalculator( MinimumEnchantabilityCalculator minimumEnchantabilityCalculator ) {
		this.minimumEnchantabilityCalculator = minimumEnchantabilityCalculator;
	}

	/** Setting up maximum enchantment level this enchantment can have. */
	protected void setMaximumEnchantmentLevel( int enchantmentLevel ) {
		this.maximumEnchantmentLevel = Math.max( 1, enchantmentLevel );
	}

	@FunctionalInterface
	protected interface MinimumEnchantabilityCalculator {
		int getMinimumLevel( int level );
	}
}