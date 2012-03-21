package com.clikclok;

import com.clikclok.service.AICalculationQueue;
import com.clikclok.service.GameLogicService;
import com.clikclok.service.SoundsService;
import com.clikclok.service.TileOperationService;
import com.clikclok.service.TileUpdateLogicService;
import com.clikclok.service.UIOperationQueue;
import com.clikclok.service.impl.AICalculationQueueImpl;
import com.clikclok.service.impl.GameLogicServiceImpl;
import com.clikclok.service.impl.SoundsServiceImpl;
import com.clikclok.service.impl.TileOperationServiceImpl;
import com.clikclok.service.impl.TileUpdateLogicServiceImpl;
import com.clikclok.service.impl.UIOperationQueueImpl;
import com.google.inject.AbstractModule;

/**
 * Performs mappings between interfaces and implementations
 * @author David
 */
public class ClikClokIOCModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(UIOperationQueue.class).to(UIOperationQueueImpl.class);
		bind(AICalculationQueue.class).to(AICalculationQueueImpl.class);
		bind(GameLogicService.class).to(GameLogicServiceImpl.class);
		bind(SoundsService.class).to(SoundsServiceImpl.class);
		bind(TileOperationService.class).to(TileOperationServiceImpl.class);
		bind(TileUpdateLogicService.class).to(TileUpdateLogicServiceImpl.class);
		bind(UIOperationQueue.class).to(UIOperationQueueImpl.class);
	}
}
