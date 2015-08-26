/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/** @module vertx-js/message_producer */
var utils = require('vertx-js/util/utils');
var WriteStream = require('vertx-js/write_stream');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JMessageProducer = io.vertx.core.eventbus.MessageProducer;
var DeliveryOptions = io.vertx.core.eventbus.DeliveryOptions;

/**
 Represents a stream of message that can be written to.
 <p>

 @class
*/
var MessageProducer = function(j_val) {

  var j_messageProducer = j_val;
  var that = this;
  WriteStream.call(this, j_val);

  /**
   This will return <code>true</code> if there are more bytes in the write queue than the value set using {@link MessageProducer#setWriteQueueMaxSize}

   @public

   @return {boolean} true if write queue is full
   */
  this.writeQueueFull = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_messageProducer["writeQueueFull()"]();
    } else utils.invalidArgs();
  };

  /**

   @public
   @param handler {function} 
   @return {MessageProducer}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_messageProducer["exceptionHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnTypeUnknown(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param data {Object} 
   @return {MessageProducer}
   */
  this.write = function(data) {
    var __args = arguments;
    if (__args.length === 1 && true) {
      j_messageProducer["write(java.lang.Object)"](utils.convParamTypeUnknown(data));
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param maxSize {number} 
   @return {MessageProducer}
   */
  this.setWriteQueueMaxSize = function(maxSize) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_messageProducer["setWriteQueueMaxSize(int)"](maxSize);
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param handler {function} 
   @return {MessageProducer}
   */
  this.drainHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_messageProducer["drainHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Update the delivery options of this producer.

   @public
   @param options {Object} the new options 
   @return {MessageProducer} this producer object
   */
  this.deliveryOptions = function(options) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object') {
      j_messageProducer["deliveryOptions(io.vertx.core.eventbus.DeliveryOptions)"](options != null ? new DeliveryOptions(new JsonObject(JSON.stringify(options))) : null);
      return that;
    } else utils.invalidArgs();
  };

  /**
   @return The address to which the producer produces messages.

   @public

   @return {string}
   */
  this.address = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_messageProducer["address()"]();
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_messageProducer;
};

// We export the Constructor function
module.exports = MessageProducer;