package org.openhab.binding.iotivity.internal.discovery;

import static org.openhab.binding.iotivity.internal.IoTivityBindingConstants.THING_TYPE_DEVICE;

import java.util.Collections;
import java.util.Set;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = DiscoveryService.class, immediate = true, configurationPid = "discovery.iotivity")
public class IoTivityDiscoveryService extends AbstractDiscoveryService {
    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_DEVICE);
    private static final int DISCOVER_TIMEOUT_SECONDS = 10;

    private Logger logger = LoggerFactory.getLogger(IoTivityDiscoveryService.class);

    public IoTivityDiscoveryService() {
        super(SUPPORTED_THING_TYPES_UIDS, DISCOVER_TIMEOUT_SECONDS, true);
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
