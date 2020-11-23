/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module org.apache.maven.repository.metadata {
  requires plexus.utils;
  requires commons.jxpath;

  exports org.apache.maven.artifact.repository.metadata;
  exports org.apache.maven.artifact.repository.metadata.io.xpp3;
}
