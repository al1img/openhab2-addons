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

import static org.openhab.binding.iotivity.internal.IoTivityBindingConstants.THING_TYPE_DEVICE;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.iotivity.base.ModeType;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.PlatformConfig;
import org.iotivity.base.QualityOfService;
import org.iotivity.base.ServiceType;
import org.openhab.binding.iotivity.internal.discovery.IoTivityDiscoveryService;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link IoTivityHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Oleksandr Grytsov - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.iotivity", service = ThingHandlerFactory.class)
public class IoTivityHandlerFactory extends BaseThingHandlerFactory {

    private Logger logger = LoggerFactory.getLogger(IoTivityHandlerFactory.class);

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_DEVICE);

    @Nullable
    private ServiceRegistration<?> discoveryServiceRegistration;

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_DEVICE.equals(thingTypeUID)) {
            return new IoTivityDeviceHandler(thing);
        }

        return null;
    }

    @Override
    protected synchronized void removeHandler(ThingHandler thingHandler) {
    }

    @Override
    protected synchronized void activate(ComponentContext componentContext) {
        logger.debug("Activate IoTivity binding");

        super.activate(componentContext);

        PlatformConfig platformConfig = new PlatformConfig(ServiceType.IN_PROC, ModeType.CLIENT_SERVER, "", 0,
                QualityOfService.LOW);
        OcPlatform.Configure(platformConfig);

        discoveryServiceRegistration = bundleContext.registerService(DiscoveryService.class.getName(),
                new IoTivityDiscoveryService(), new Hashtable<>());
    }

    @Override
    protected synchronized void deactivate(ComponentContext componentContext) {
        logger.debug("Deactivate IoTivity binding");

        if (discoveryServiceRegistration != null) {
            discoveryServiceRegistration.unregister();
            discoveryServiceRegistration = null;
        }

        OcPlatform.Shutdown();

        super.deactivate(componentContext);
    }
}
