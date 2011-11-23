package com.clikclok.test;

import java.util.Collection;

import junit.framework.TestCase;

import com.clikclok.domain.TileDirection;
import com.clikclok.domain.TilePosition;

public class TilePositionTest extends TestCase{
	private TilePosition tilePosition;
	
	@Override
	protected void setUp() throws Exception {
		tilePosition = new TilePosition(1,1);
	}
	
	public void testGetAdjacentTilePosition()
	{
		TilePosition adjacentTilePosition = tilePosition.getAdjacentTilePosition(TileDirection.EAST);
		
		assertEquals(new TilePosition(2,1), adjacentTilePosition);
	}
	
	public void testGetAdjacentTilePositions()
	{
		Collection<TilePosition> tilePositions = tilePosition.getAdjacentTilePositions();
		
		assertEquals(4, tilePositions.size());
		assertTrue(tilePositions.contains(new TilePosition(1,0)));
		assertTrue(tilePositions.contains(new TilePosition(2,1)));
		assertTrue(tilePositions.contains(new TilePosition(1,2)));
		assertTrue(tilePositions.contains(new TilePosition(0,1)));
	}
	
}
