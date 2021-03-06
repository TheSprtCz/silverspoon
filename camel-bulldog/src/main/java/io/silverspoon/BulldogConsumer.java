/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.silverspoon;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The Bulldog consumer.
 *
 * @author <a href="mailto:pavel.macik@gmail.com">Pavel Macík</a>
 * @author <a href="mailto:pipistik.bunciak@gmail.com">Štefan Bunčiak</a>
 */
public class BulldogConsumer extends ScheduledPollConsumer {
   private final BulldogEndpoint endpoint;

   private Queue<String> eventQueue = new LinkedBlockingQueue<>();

   public BulldogConsumer(BulldogEndpoint endpoint, Processor processor) {
      super(endpoint, processor);
      this.endpoint = endpoint;
   }

   @Override
   protected int poll() throws Exception {
      int count = 0;
      String event;
      while (!eventQueue.isEmpty()) {
         event = eventQueue.remove();
         Exchange exchange = endpoint.createExchange();
         exchange.getIn().setBody(event);

         try {
            // send message to next processor in the route
            getProcessor().process(exchange);
            count++;
         } finally {
            // log exception if an exception occurred and was not handled
            if (exchange.getException() != null) {
               getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
         }
      }
      return count;
   }

   public BulldogEndpoint getEndpoint(){
      return this.endpoint;
   }
}
