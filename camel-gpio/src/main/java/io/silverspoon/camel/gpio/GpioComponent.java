/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.silverspoon.camel.gpio;

import org.apache.camel.CamelException;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

import java.util.Map;

/**
 * Represents the component that manages {@link GpioEndpoint}.
 *
 * @author Pavel Macík <pavel.macik@gmail.com>
 */
public class GpioComponent extends DefaultComponent {
   public static final String HIGH = "HIGH";
   public static final String LOW = "LOW";

   protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
      final GpioEndpoint endpoint = new GpioEndpoint(uri, this);
      setProperties(endpoint, parameters);
      final String pinValue = endpoint.getValue();
      if (pinValue != null) {
         switch (pinValue) {
            case HIGH:
            case LOW:
               return endpoint;
            default:
               throw new CamelException("The value of the 'value' parameter is invalid [" + pinValue + "].");
         }
      } else {
         return endpoint;
      }
   }
}
