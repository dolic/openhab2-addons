/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tankerkoenig.internal.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.tankerkoenig.internal.config.TankerkoenigListResult;
import org.openhab.binding.tankerkoenig.internal.serializer.CustomTankerkoenigListResultDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/***
 * Serivce class requesting data from tankerkoenig api and providing result objects
 *
 * @author Dennis Dollinger
 *
 */
public class TankerkoenigService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TankerkoenigListResult getTankstellenListData(String apikey, String locationIDs) {
        TankerkoenigListResult result = this.getTankerkoenigListResult(apikey, locationIDs);
        return result;
    }

    private String getResponseString(String apikey, String locationIDs) throws IOException {

        String urlbase = "https://creativecommons.tankerkoenig.de/json/prices.php?";
        String urlcomplete = urlbase + "ids=" + locationIDs + "&apikey=" + apikey;
        try {
            URL url = new URL(urlcomplete);
            URLConnection connection = url.openConnection();
            String response = IOUtils.toString(connection.getInputStream());
            return response;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private TankerkoenigListResult getTankerkoenigListResult(String apikey, String locationIDs) {
        String jsonData = "";
        try {
            jsonData = getResponseString(apikey, locationIDs);
        } catch (IOException e) {
            logger.debug("Error in getTankerkoenigListResult: {}", e.toString());
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(TankerkoenigListResult.class, new CustomTankerkoenigListResultDeserializer());
        Gson gson = gsonBuilder.create();
        TankerkoenigListResult res = gson.fromJson(jsonData, TankerkoenigListResult.class);
        return res;
    }

}
