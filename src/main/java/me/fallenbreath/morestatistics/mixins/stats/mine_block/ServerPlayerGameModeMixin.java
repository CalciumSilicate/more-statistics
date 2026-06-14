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

package me.fallenbreath.morestatistics.mixins.stats.mine_block;

import me.fallenbreath.morestatistics.MoreStatisticsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin
{
	@Shadow protected ServerPlayer player;

	@Redirect(
			method = "destroyBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"
			)
	)
	private boolean onRemoveBlock(ServerLevel world, BlockPos blockPos, boolean moving)
	{
		boolean removed = world.removeBlock(blockPos, moving);
		if (removed)
		{
			this.player.awardStat(MoreStatisticsRegistry.MINE_BLOCK, 1);
		}
		return removed;
	}
}
