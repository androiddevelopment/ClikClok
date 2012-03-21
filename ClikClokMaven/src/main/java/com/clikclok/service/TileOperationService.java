package com.clikclok.service;

import com.clikclok.domain.Tile;
import com.clikclok.service.impl.GameLogicServiceImpl;

public interface TileOperationService {

	/**
	 * This will be invoked when a user clicks on a tile. In this method we manually inject the {@link GameLogicServiceImpl} as using DI here doesn't add many benefits
	 * @param clickedTile
	 */
	public abstract void performUserOperation(final Tile clickedTile);

	/**
	 * This will be invoked when the level 5 timer expires and the AI gets a free turn
	 */
	public abstract void performAIOperationAfterTimeOut();

}