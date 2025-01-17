package com.mlib.time;

import com.mlib.TimeConverter;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnegative;

/** Class for easier handling delays. */
@Mod.EventBusSubscriber
public class TimeHelper {
	private static long clientCounter = 1;
	private static long serverCounter = 1;

	@SubscribeEvent
	public static void onPlayerTick( TickEvent.ClientTickEvent event ) {
		if( isEndPhase( event ) )
			++clientCounter;
	}

	@SubscribeEvent
	public static void onServerTick( TickEvent.ServerTickEvent event ) {
		if( isEndPhase( event ) )
			++serverCounter;
	}

	/** Returns whether given event's phase is 'END'. */
	public static boolean isEndPhase( TickEvent event ) {
		return event.phase == TickEvent.Phase.END;
	}

	/** Returns whether client counter can be divided by given value. */
	public static boolean hasClientTicksPassed( @Nonnegative int tickDelay ) {
		return clientCounter % tickDelay == 0;
	}

	/** Returns whether client counter can be divided by given value. (in seconds) */
	public static boolean hasClientSecondsPassed( @Nonnegative double secondDelay ) {
		return hasClientTicksPassed( TimeConverter.secondsToTicks( secondDelay ) );
	}

	/** Returns whether client counter can be divided by given value. */
	public static boolean hasServerTicksPassed( @Nonnegative int tickDelay ) {
		return serverCounter % tickDelay == 0;
	}

	/** Returns whether client counter can be divided by given value. (in seconds) */
	public static boolean hasServerSecondsPassed( @Nonnegative double secondDelay ) {
		return hasServerTicksPassed( TimeConverter.secondsToTicks( secondDelay ) );
	}
}
