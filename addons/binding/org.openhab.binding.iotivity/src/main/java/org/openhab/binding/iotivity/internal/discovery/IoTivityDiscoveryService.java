package org.openhab.binding.iotivity.internal.discovery;

import static org.openhab.binding.iotivity.internal.IoTivityBindingConstants.THING_TYPE_DEVICE;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.iotivity.internal.IoTivityBridgeHandler;
import org.openhab.binding.iotivity.internal.IoTivityHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoTivityDiscoveryService extends AbstractDiscoveryService {
    private static final int DISCOVER_TIMEOUT_SECONDS = 10;

    private Logger logger = LoggerFactory.getLogger(IoTivityDiscoveryService.class);

    private IoTivityBridgeHandler bridgeHandler;

    public IoTivityDiscoveryService(IoTivityBridgeHandler bridgeHandler) {
        super(IoTivityHandler.SUPPORTED_THING_TYPES_UIDS, DISCOVER_TIMEOUT_SECONDS, true);
        this.bridgeHandler = bridgeHandler;
    }

    @Override
    protected void startBackgroundDiscovery() {
        logger.debug("Start IoTivity device background discovery");
    }

    @Override
    protected void stopBackgroundDiscovery() {
        logger.debug("Stop IoTivity device background discovery");
    }

    @Override
    protected void startScan() {
        logger.debug("Start IoTivity device active scan");

        ThingUID thingUID = new ThingUID(THING_TYPE_DEVICE, "NewDevice");

        DiscoveryResult result = DiscoveryResultBuilder.create(thingUID).withThingType(THING_TYPE_DEVICE)
                .withLabel("Super device").build();

        thingDiscovered(result);
    }
}
