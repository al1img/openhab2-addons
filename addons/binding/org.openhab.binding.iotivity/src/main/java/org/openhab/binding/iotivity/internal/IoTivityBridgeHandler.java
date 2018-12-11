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

import static org.openhab.binding.iotivity.internal.IoTivityBindingConstants.THING_TYPE_BRIDGE;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.iotivity.base.ModeType;
import org.iotivity.base.OcConnectivityType;
import org.iotivity.base.OcException;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.OcResource;
import org.iotivity.base.PlatformConfig;
import org.iotivity.base.QualityOfService;
import org.iotivity.base.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoTivityBridgeHandler extends BaseBridgeHandler implements OcPlatform.OnResourceFoundListener {

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_BRIDGE);

    private final Logger logger = LoggerFactory.getLogger(IoTivityHandler.class);

    public IoTivityBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing IoTivity bridge handler");

        try {
            PlatformConfig platformConfig = new PlatformConfig(ServiceType.IN_PROC, ModeType.CLIENT_SERVER,
                    QualityOfService.LOW);
            OcPlatform.Configure(platformConfig);

            String requestUri = OcPlatform.WELL_KNOWN_QUERY + "?rt=core.light";
            OcPlatform.findResource("", requestUri, EnumSet.of(OcConnectivityType.CT_DEFAULT), this);
            updateStatus(ThingStatus.ONLINE);
        } catch (OcException e) {
            logger.error("Intialization failed: " + e.toString());
            updateStatus(ThingStatus.UNINITIALIZED, ThingStatusDetail.BRIDGE_UNINITIALIZED, e.toString());
        }
    }

    @Override
    public void dispose() {
        OcPlatform.Shutdown();
        super.dispose();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Handling command, channelUID: {}, command: {}", channelUID, command);
    }

    @Override
    public synchronized void onResourceFound(OcResource ocResource) {
    }

    @Override
    public synchronized void onFindResourceFailed(Throwable throwable, String uri) {
    }
}
