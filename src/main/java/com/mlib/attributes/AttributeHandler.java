package com.mlib.attributes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;

import java.util.UUID;

/** Handling attributes in easier and shorter way. */
public class AttributeHandler {
	private final UUID uuid;
	private final String name;
	private final Attribute attribute;
	private final AttributeModifier.Operation operation;
	private double value = 1.0;

	public AttributeHandler( String uuid, String name, Attribute attribute, AttributeModifier.Operation operation ) {
		this.uuid = UUID.fromString( uuid );
		this.name = name;
		this.attribute = attribute;
		this.operation = operation;
	}

	/** Setting current attribute value. */
	public AttributeHandler setValue( double value ) {
		this.value = value;

		return this;
	}

	/** Returns current attribute value. */
	public double getValue() {
		return this.value;
	}

	/**
	 Applying current attribute to the target.

	 @param target Entity to apply effects.
	 */
	public AttributeHandler apply( LivingEntity target ) {
		ModifiableAttributeInstance attributeInstance = target.getAttribute( this.attribute );

		if( attributeInstance != null ) {
			attributeInstance.removeModifier( this.uuid );
			AttributeModifier modifier = new AttributeModifier( this.uuid, this.name, this.value, this.operation );
			attributeInstance.applyPersistentModifier( modifier );
		}

		return this;
	}
}
