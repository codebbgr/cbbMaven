/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

module org.apache.maven.resolver.provider {
  requires org.maven.model;
  requires org.maven.model.builder;
  requires plexus.interpolation;
  requires org.maven.artifact;
  requires org.maven.builder.support;
  requires org.apache.maven.xml;
  requires org.eclipse.sisu.inject;
  requires org.apache.maven.repository.metadata;
  requires org.apache.maven.resolver;
  requires org.apache.maven.resolver.spi;
  requires org.apache.maven.resolver.util;
  requires org.apache.maven.resolver.impl;
  requires org.apache.commons.lang3;
  requires plexus.utils;
  requires javax.inject;
  requires com.google.guice;
  requires com.google.common;
  requires failureaccess;
  requires listenablefuture;
  requires jsr305;
  requires checker.qual;
  requires error.prone.annotations;
  requires j2objc.annotations;
  requires animal.sniffer.annotations;
  requires org.slf4j;

  exports org.apache.maven.repository.internal;
}
