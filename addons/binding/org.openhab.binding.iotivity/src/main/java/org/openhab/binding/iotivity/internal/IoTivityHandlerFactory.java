/**
 * Copyright (c) 2018,2018 by the respective copyright holders.
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.iotivity.internal;

import static org.openhab.binding.iotivity.internal.IoTivityBindingConstants.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.iotivity.internal.discovery.IoTivityDiscoveryService;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;

/**
 * The {@link IoTivityHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Oleksandr Grytsov - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.iotivity", service = ThingHandlerFactory.class)
public class IoTivityHandlerFactory extends BaseThingHandlerFactory {

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Stream
            .concat(IoTivityBridgeHandler.SUPPORTED_THING_TYPES_UIDS.stream(),
                    IoTivityHandler.SUPPORTED_THING_TYPES_UIDS.stream())
            .collect(Collectors.toSet());

    private Map<ThingUID, ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_BRIDGE.equals(thingTypeUID)) {
            IoTivityBridgeHandler bridgeHandler = new IoTivityBridgeHandler((Bridge) thing);
            registerIoTivityDiscoveryService(bridgeHandler);
            return bridgeHandler;
        } else if (THING_TYPE_DEVICE.equals(thingTypeUID)) {
            return new IoTivityHandler(thing);
        }

        return null;
    }

    private synchronized void registerIoTivityDiscoveryService(IoTivityBridgeHandler bridgeHandler) {
        IoTivityDiscoveryService discoveryService = new IoTivityDiscoveryService(bridgeHandler);
        this.discoveryServiceRegs.put(bridgeHandler.getThing().getUID(),
                bundleContext.registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<>()));
    }

    @Override
    protected synchronized void removeHandler(ThingHandler thingHandler) {
        if (thingHandler instanceof IoTivityBridgeHandler) {
            ServiceRegistration<?> serviceReg = this.discoveryServiceRegs.remove(thingHandler.getThing().getUID());
            if (serviceReg != null) {
                // remove discovery service, if bridge handler is removed
                IoTivityDiscoveryService discoveryService = (IoTivityDiscoveryService) bundleContext
                        .getService(serviceReg.getReference());
                serviceReg.unregister();
            }
        }
    }

}
