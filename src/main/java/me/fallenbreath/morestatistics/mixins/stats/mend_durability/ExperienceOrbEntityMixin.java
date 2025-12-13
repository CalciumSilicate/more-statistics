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

package me.fallenbreath.morestatistics.mixins.stats.mend_durability;

import me.fallenbreath.morestatistics.MoreStatisticsRegistry;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// impl for mc < 1.21
@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbEntityMixin
{
	@ModifyVariable(
			//#if MC >= 11700
			//$$ method = "repairPlayerItems",
			//#else
			method = "playerTouch",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V"
			//#if MC >= 11700
			//$$ ),
			//$$ ordinal = 1
			//#else
			)
			//#endif
	)
	private int onMendingApplied(
			int durabilityMended, Player player
			//#if MC >= 11700
			//$$ , int amount
			//#endif
	)
	{
		player.awardStat(MoreStatisticsRegistry.MEND_DURABILITY, durabilityMended);
		return durabilityMended;
	}
}
