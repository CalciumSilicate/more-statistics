/*
 * This file is part of the More Statistics project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * More Statistics is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * More Statistics is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with More Statistics.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.fallenbreath.morestatistics.mixins.stats.summon_phantom;

import me.fallenbreath.morestatistics.MoreStatisticsRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 11200
//$$ import net.minecraft.server.level.ServerPlayer;
//#endif

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin
{
	private Player currentPlayer$moreStatistics = null;

	@ModifyVariable(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/Mth;clamp(III)I"
			)
	)
	//#if MC >= 12000
	//$$ private ServerPlayer recordsCurrentPlayer$moreStatistics(ServerPlayer player)
	//#else
	private Player recordsCurrentPlayer$moreStatistics(Player player)
	//#endif
	{
		this.currentPlayer$moreStatistics = player;
		return player;
	}

	@ModifyVariable(
			method = "tick",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/entity/EntityType;PHANTOM:Lnet/minecraft/world/entity/EntityType;"
			),
			ordinal = 1
	)
	private int addSummonPhantomStat$moreStatistics(int amount)
	{
		if (this.currentPlayer$moreStatistics != null)
		{
			this.currentPlayer$moreStatistics.awardStat(MoreStatisticsRegistry.SUMMON_PHANTOM, amount);
			this.currentPlayer$moreStatistics = null;
		}
		return amount;
	}
}
