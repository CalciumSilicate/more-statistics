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

package me.fallenbreath.morestatistics.utils;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PistonPlacingMemory
{
	private static final Map<Pair<DimensionWrapper, BlockPos>, ServerPlayer> MEMORY = Maps.newHashMap();

	public static void onPlayerPlacedPiston(ServerPlayer player, BlockPos pos)
	{
		//#if MC >= 1.20.1
		//$$ Level playerLevel = player.level();
		//#else
		Level playerLevel = player.getLevel();
		//#endif
		MEMORY.put(makePair(playerLevel, pos), player);
	}

	@Nullable
	public static ServerPlayer getTheOneWhoJustPlacedPiston(Level world, BlockPos pos)
	{
		return MEMORY.get(makePair(world, pos));
	}

	private static Pair<DimensionWrapper, BlockPos> makePair(Level world, BlockPos pos)
	{
		return Pair.of(DimensionWrapper.of(world), pos);
	}

	public static void cleanMemory()
	{
		MEMORY.clear();
	}
}
