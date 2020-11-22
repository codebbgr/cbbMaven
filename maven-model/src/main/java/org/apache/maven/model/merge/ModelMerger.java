package org.apache.maven.model.merge;

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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.maven.model.Activation;
import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.ConfigurationContainer;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.DeploymentRepository;
import org.apache.maven.model.Developer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Extension;
import org.apache.maven.model.FileSet;
import org.apache.maven.model.InputLocation;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.License;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.Model;
import org.apache.maven.model.ModelBase;
import org.apache.maven.model.Notifier;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Parent;
import org.apache.maven.model.PatternSet;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginConfiguration;
import org.apache.maven.model.PluginContainer;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Prerequisites;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Relocation;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.ReportSet;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.RepositoryBase;
import org.apache.maven.model.RepositoryPolicy;
import org.apache.maven.model.Resource;
import org.apache.maven.model.Scm;
import org.apache.maven.model.Site;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * This is a hand-crafted prototype of the default model merger that should eventually be generated
 * by Modello by a new Java plugin. Code structure to merge source (read-only) object into the
 * target object is:
 *
 * <ul>
 *   <li><code>
 *       merge<i>Classname</i>( <i>Classname</i> target, <i>Classname</i> source, boolean sourceDominant,
 * Map&lt;Object, Object&gt; context )</code> for each model class
 *   <li><code>
 *       merge<i>Classname</i>_<i>FieldName</i>( <i>Classname</i> target, <i>Classname</i> source, boolean
 * sourceDominant, Map&lt;Object, Object&gt; context )</code> for each field of each model class
 *   <li><code>Object get<i>Classname</i>Key( <i>Classname</i> <i>classname</i> )</code> for each
 *       class that is used in a list
 * </ul>
 *
 * Code is written like it could be generated, with default behaviour to be overridden when
 * necessary. This is particularly the case for <code>
 * Object get<i>Classname</i>Key( <i>Classname</i> <i>classname</i> )</code> method, which by
 * default return the object itself and is expected to be overridden to calculate better suited key
 * value.
 *
 * @author Benjamin Bentmann
 */
@SuppressWarnings({"checkstyle:methodname"})
public class ModelMerger {

  /**
   * Merges the specified source object into the given target object.
   *
   * @param target The target object whose existing contents should be merged with the source, must
   *     not be <code>null</code>.
   * @param source The (read-only) source object that should be merged into the target object, may
   *     be <code>null</code>.
   * @param sourceDominant A flag indicating whether either the target object or the source object
   *     provides the dominant data.
   * @param hints A set of key-value pairs that customized merger implementations can use to carry
   *     domain-specific information along, may be <code>null</code>.
   */
  public void merge(Model target, Model source, boolean sourceDominant, Map<?, ?> hints) {
    Objects.requireNonNull(target, "target cannot be null");

    if (source == null) {
      return;
    }

    Map<Object, Object> context = new HashMap<>();
    if (hints != null) {
      context.putAll(hints);
    }

    mergeModel(target, source, sourceDominant, context);
  }

  protected void mergeModel(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    mergeModelBase(target, source, sourceDominant, context);

    mergeModel_ChildProjectUrlInheritAppendPath(target, source, sourceDominant, context);
    mergeModel_ModelVersion(target, source, sourceDominant, context);
    mergeModel_Parent(target, source, sourceDominant, context);
    mergeModel_GroupId(target, source, sourceDominant, context);
    mergeModel_ArtifactId(target, source, sourceDominant, context);
    mergeModel_Version(target, source, sourceDominant, context);
    mergeModel_Packaging(target, source, sourceDominant, context);
    mergeModel_Name(target, source, sourceDominant, context);
    mergeModel_Description(target, source, sourceDominant, context);
    mergeModel_Url(target, source, sourceDominant, context);
    mergeModel_InceptionYear(target, source, sourceDominant, context);
    mergeModel_Organization(target, source, sourceDominant, context);
    mergeModel_Licenses(target, source, sourceDominant, context);
    mergeModel_MailingLists(target, source, sourceDominant, context);
    mergeModel_Developers(target, source, sourceDominant, context);
    mergeModel_Contributors(target, source, sourceDominant, context);
    mergeModel_IssueManagement(target, source, sourceDominant, context);
    mergeModel_Scm(target, source, sourceDominant, context);
    mergeModel_CiManagement(target, source, sourceDominant, context);
    mergeModel_Prerequisites(target, source, sourceDominant, context);
    mergeModel_Build(target, source, sourceDominant, context);
    mergeModel_Profiles(target, source, sourceDominant, context);
  }

  protected void mergeModel_ModelVersion(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getModelVersion();
    if (src != null) {
      if (sourceDominant || target.getModelVersion() == null) {
        target.setModelVersion(src);
        target.setLocation("modelVersion", source.getLocation("modelVersion"));
      }
    }
  }

  protected void mergeModel_Parent(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    Parent src = source.getParent();
    if (src != null) {
      Parent tgt = target.getParent();
      if (tgt == null) {
        tgt = new Parent();
        tgt.setRelativePath(null);
        target.setParent(tgt);
      }
      mergeParent(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeModel_GroupId(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getGroupId();
    if (src != null) {
      if (sourceDominant || target.getGroupId() == null) {
        target.setGroupId(src);
        target.setLocation("groupId", source.getLocation("groupId"));
      }
    }
  }

  protected void mergeModel_ArtifactId(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getArtifactId();
    if (src != null) {
      if (sourceDominant || target.getArtifactId() == null) {
        target.setArtifactId(src);
        target.setLocation("artifactId", source.getLocation("artifactId"));
      }
    }
  }

  protected void mergeModel_ChildProjectUrlInheritAppendPath(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getChildProjectUrlInheritAppendPath();
    if (src != null) {
      if (sourceDominant || target.getChildProjectUrlInheritAppendPath() == null) {
        target.setChildProjectUrlInheritAppendPath(src);
        target.setLocation(
            "child.project.url.inherit.append.path",
            source.getLocation("child.project.url.inherit.append.path"));
      }
    }
  }

  protected void mergeModel_Version(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getVersion();
    if (src != null) {
      if (sourceDominant || target.getVersion() == null) {
        target.setVersion(src);
        target.setLocation("version", source.getLocation("version"));
      }
    }
  }

  protected void mergeModel_Packaging(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getPackaging();
    if (src != null) {
      if (sourceDominant || target.getPackaging() == null) {
        target.setPackaging(src);
        target.setLocation("packaging", source.getLocation("packaging"));
      }
    }
  }

  protected void mergeModel_Name(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getName();
    if (src != null) {
      if (sourceDominant || target.getName() == null) {
        target.setName(src);
        target.setLocation("name", source.getLocation("name"));
      }
    }
  }

  protected void mergeModel_Description(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getDescription();
    if (src != null) {
      if (sourceDominant || target.getDescription() == null) {
        target.setDescription(src);
        target.setLocation("description", source.getLocation("description"));
      }
    }
  }

  protected void mergeModel_Url(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getUrl();
    if (src != null) {
      if (sourceDominant || target.getUrl() == null) {
        target.setUrl(src);
        target.setLocation("url", source.getLocation("url"));
      }
    }
  }

  protected void mergeModel_InceptionYear(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getInceptionYear();
    if (src != null) {
      if (sourceDominant || target.getInceptionYear() == null) {
        target.setInceptionYear(src);
        target.setLocation("inceptionYear", source.getLocation("inceptionYear"));
      }
    }
  }

  protected void mergeModel_Organization(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    Organization src = source.getOrganization();
    if (src != null) {
      Organization tgt = target.getOrganization();
      if (tgt == null) {
        tgt = new Organization();
        target.setOrganization(tgt);
      }
      mergeOrganization(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeModel_Licenses(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    target.setLicenses(
        merge(target.getLicenses(), source.getLicenses(), sourceDominant, getLicenseKey()));
  }

  protected void mergeModel_MailingLists(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    target.setMailingLists(
        merge(
            target.getMailingLists(),
            source.getMailingLists(),
            sourceDominant,
            getMailingListKey()));
  }

  protected void mergeModel_Developers(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    target.setDevelopers(
        merge(target.getDevelopers(), source.getDevelopers(), sourceDominant, getDeveloperKey()));
  }

  protected void mergeModel_Contributors(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    target.setContributors(
        merge(
            target.getContributors(),
            source.getContributors(),
            sourceDominant,
            getContributorKey()));
  }

  protected void mergeModel_IssueManagement(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    IssueManagement src = source.getIssueManagement();
    if (src != null) {
      IssueManagement tgt = target.getIssueManagement();
      if (tgt == null) {
        tgt = new IssueManagement();
        target.setIssueManagement(tgt);
      }
      mergeIssueManagement(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeModel_Scm(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    Scm src = source.getScm();
    if (src != null) {
      Scm tgt = target.getScm();
      if (tgt == null) {
        tgt = new Scm();
        tgt.setTag(null);
        target.setScm(tgt);
      }
      mergeScm(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeModel_CiManagement(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    CiManagement src = source.getCiManagement();
    if (src != null) {
      CiManagement tgt = target.getCiManagement();
      if (tgt == null) {
        tgt = new CiManagement();
        target.setCiManagement(tgt);
      }
      mergeCiManagement(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeModel_Prerequisites(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    Prerequisites src = source.getPrerequisites();
    if (src != null) {
      Prerequisites tgt = target.getPrerequisites();
      if (tgt == null) {
        tgt = new Prerequisites();
        tgt.setMaven(null);
        target.setPrerequisites(tgt);
      }
      mergePrerequisites(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeModel_Build(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    Build src = source.getBuild();
    if (src != null) {
      Build tgt = target.getBuild();
      if (tgt == null) {
        tgt = new Build();
        target.setBuild(tgt);
      }
      mergeBuild(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeModel_Profiles(
      Model target, Model source, boolean sourceDominant, Map<Object, Object> context) {
    target.setProfiles(
        merge(target.getProfiles(), source.getProfiles(), sourceDominant, getProfileKey()));
  }

  protected void mergeModelBase(
      ModelBase target, ModelBase source, boolean sourceDominant, Map<Object, Object> context) {
    mergeModelBase_DistributionManagement(target, source, sourceDominant, context);
    mergeModelBase_Modules(target, source, sourceDominant, context);
    mergeModelBase_Repositories(target, source, sourceDominant, context);
    mergeModelBase_PluginRepositories(target, source, sourceDominant, context);
    mergeModelBase_Dependencies(target, source, sourceDominant, context);
    mergeModelBase_Reporting(target, source, sourceDominant, context);
    mergeModelBase_DependencyManagement(target, source, sourceDominant, context);
    mergeModelBase_Properties(target, source, sourceDominant, context);
  }

  protected void mergeModelBase_Modules(
      ModelBase target, ModelBase source, boolean sourceDominant, Map<Object, Object> context) {
    target.setModules(merge(target.getModules(), source.getModules(), sourceDominant, e -> e));
  }

  protected void mergeModelBase_Dependencies(
      ModelBase target, ModelBase source, boolean sourceDominant, Map<Object, Object> context) {
    target.setDependencies(
        merge(
            target.getDependencies(),
            source.getDependencies(),
            sourceDominant,
            getDependencyKey()));
  }

  protected void mergeModelBase_Repositories(
      ModelBase target, ModelBase source, boolean sourceDominant, Map<Object, Object> context) {
    target.setRepositories(
        merge(
            target.getRepositories(),
            source.getRepositories(),
            sourceDominant,
            getRepositoryKey()));
  }

  protected void mergeModelBase_PluginRepositories(
      ModelBase target, ModelBase source, boolean sourceDominant, Map<Object, Object> context) {
    target.setPluginRepositories(
        merge(
            target.getPluginRepositories(),
            source.getPluginRepositories(),
            sourceDominant,
            getRepositoryKey()));
  }

  protected void mergeModelBase_DistributionManagement(
      ModelBase target, ModelBase source, boolean sourceDominant, Map<Object, Object> context) {
    DistributionManagement src = source.getDistributionManagement();
    if (src != null) {
      DistributionManagement tgt = target.getDistributionManagement();
      if (tgt == null) {
        tgt = new DistributionManagement();
        target.setDistributionManagement(tgt);
      }
      mergeDistributionManagement(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeModelBase_Reporting(
      ModelBase target, ModelBase source, boolean sourceDominant, Map<Object, Object> context) {
    Reporting src = source.getReporting();
    if (src != null) {
      Reporting tgt = target.getReporting();
      if (tgt == null) {
        tgt = new Reporting();
        target.setReporting(tgt);
      }
      mergeReporting(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeModelBase_DependencyManagement(
      ModelBase target, ModelBase source, boolean sourceDominant, Map<Object, Object> context) {
    DependencyManagement src = source.getDependencyManagement();
    if (src != null) {
      DependencyManagement tgt = target.getDependencyManagement();
      if (tgt == null) {
        tgt = new DependencyManagement();
        target.setDependencyManagement(tgt);
      }
      mergeDependencyManagement(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeModelBase_Properties(
      ModelBase target, ModelBase source, boolean sourceDominant, Map<Object, Object> context) {
    Properties merged = new Properties();
    if (sourceDominant) {
      merged.putAll(target.getProperties());
      merged.putAll(source.getProperties());
    } else {
      merged.putAll(source.getProperties());
      merged.putAll(target.getProperties());
    }
    target.setProperties(merged);
    target.setLocation(
        "properties",
        InputLocation.merge(
            target.getLocation("properties"), source.getLocation("properties"), sourceDominant));
  }

  protected void mergeDistributionManagement(
      DistributionManagement target,
      DistributionManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergeDistributionManagement_Repository(target, source, sourceDominant, context);
    mergeDistributionManagement_SnapshotRepository(target, source, sourceDominant, context);
    mergeDistributionManagement_Site(target, source, sourceDominant, context);
    mergeDistributionManagement_Status(target, source, sourceDominant, context);
    mergeDistributionManagement_DownloadUrl(target, source, sourceDominant, context);
  }

  protected void mergeDistributionManagement_Repository(
      DistributionManagement target,
      DistributionManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    DeploymentRepository src = source.getRepository();
    if (src != null) {
      DeploymentRepository tgt = target.getRepository();
      if (tgt == null) {
        tgt = new DeploymentRepository();
        target.setRepository(tgt);
      }
      mergeDeploymentRepository(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeDistributionManagement_SnapshotRepository(
      DistributionManagement target,
      DistributionManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    DeploymentRepository src = source.getSnapshotRepository();
    if (src != null) {
      DeploymentRepository tgt = target.getSnapshotRepository();
      if (tgt == null) {
        tgt = new DeploymentRepository();
        target.setSnapshotRepository(tgt);
      }
      mergeDeploymentRepository(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeDistributionManagement_Site(
      DistributionManagement target,
      DistributionManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    Site src = source.getSite();
    if (src != null) {
      Site tgt = target.getSite();
      if (tgt == null) {
        tgt = new Site();
        target.setSite(tgt);
      }
      mergeSite(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeDistributionManagement_Status(
      DistributionManagement target,
      DistributionManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getStatus();
    if (src != null) {
      if (sourceDominant || target.getStatus() == null) {
        target.setStatus(src);
        target.setLocation("status", source.getLocation("status"));
      }
    }
  }

  protected void mergeDistributionManagement_DownloadUrl(
      DistributionManagement target,
      DistributionManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getDownloadUrl();
    if (src != null) {
      if (sourceDominant || target.getDownloadUrl() == null) {
        target.setDownloadUrl(src);
        target.setLocation("downloadUrl", source.getLocation("downloadUrl"));
      }
    }
  }

  protected void mergeRelocation(
      Relocation target, Relocation source, boolean sourceDominant, Map<Object, Object> context) {
    mergeRelocation_GroupId(target, source, sourceDominant, context);
    mergeRelocation_ArtifactId(target, source, sourceDominant, context);
    mergeRelocation_Version(target, source, sourceDominant, context);
    mergeRelocation_Message(target, source, sourceDominant, context);
  }

  protected void mergeRelocation_GroupId(
      Relocation target, Relocation source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getGroupId();
    if (src != null) {
      if (sourceDominant || target.getGroupId() == null) {
        target.setGroupId(src);
        target.setLocation("groupId", source.getLocation("groupId"));
      }
    }
  }

  protected void mergeRelocation_ArtifactId(
      Relocation target, Relocation source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getArtifactId();
    if (src != null) {
      if (sourceDominant || target.getArtifactId() == null) {
        target.setArtifactId(src);
        target.setLocation("artifactId", source.getLocation("artifactId"));
      }
    }
  }

  protected void mergeRelocation_Version(
      Relocation target, Relocation source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getVersion();
    if (src != null) {
      if (sourceDominant || target.getVersion() == null) {
        target.setVersion(src);
        target.setLocation("version", source.getLocation("version"));
      }
    }
  }

  protected void mergeRelocation_Message(
      Relocation target, Relocation source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getMessage();
    if (src != null) {
      if (sourceDominant || target.getMessage() == null) {
        target.setMessage(src);
        target.setLocation("message", source.getLocation("message"));
      }
    }
  }

  protected void mergeDeploymentRepository(
      DeploymentRepository target,
      DeploymentRepository source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergeRepository(target, source, sourceDominant, context);
    mergeDeploymentRepository_UniqueVersion(target, source, sourceDominant, context);
  }

  protected void mergeDeploymentRepository_UniqueVersion(
      DeploymentRepository target,
      DeploymentRepository source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    if (sourceDominant) {
      target.setUniqueVersion(source.isUniqueVersion());
      target.setLocation("uniqueVersion", source.getLocation("uniqueVersion"));
    }
  }

  protected void mergeSite(
      Site target, Site source, boolean sourceDominant, Map<Object, Object> context) {
    mergeSite_ChildSiteUrlInheritAppendPath(target, source, sourceDominant, context);
    mergeSite_Id(target, source, sourceDominant, context);
    mergeSite_Name(target, source, sourceDominant, context);
    mergeSite_Url(target, source, sourceDominant, context);
  }

  protected void mergeSite_ChildSiteUrlInheritAppendPath(
      Site target, Site source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getChildSiteUrlInheritAppendPath();
    if (src != null) {
      if (sourceDominant || target.getChildSiteUrlInheritAppendPath() == null) {
        target.setChildSiteUrlInheritAppendPath(src);
        target.setLocation(
            "child.site.url.inherit.append.path",
            source.getLocation("child.site.url.inherit.append.path"));
      }
    }
  }

  protected void mergeSite_Id(
      Site target, Site source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getId();
    if (src != null) {
      if (sourceDominant || target.getId() == null) {
        target.setId(src);
        target.setLocation("id", source.getLocation("id"));
      }
    }
  }

  protected void mergeSite_Name(
      Site target, Site source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getName();
    if (src != null) {
      if (sourceDominant || target.getName() == null) {
        target.setName(src);
        target.setLocation("name", source.getLocation("name"));
      }
    }
  }

  protected void mergeSite_Url(
      Site target, Site source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getUrl();
    if (src != null) {
      if (sourceDominant || target.getUrl() == null) {
        target.setUrl(src);
        target.setLocation("url", source.getLocation("url"));
      }
    }
  }

  protected void mergeRepository(
      Repository target, Repository source, boolean sourceDominant, Map<Object, Object> context) {
    mergeRepositoryBase(target, source, sourceDominant, context);
    mergeRepository_Releases(target, source, sourceDominant, context);
    mergeRepository_Snapshots(target, source, sourceDominant, context);
  }

  protected void mergeRepository_Releases(
      Repository target, Repository source, boolean sourceDominant, Map<Object, Object> context) {
    RepositoryPolicy src = source.getReleases();
    if (src != null) {
      RepositoryPolicy tgt = target.getReleases();
      if (tgt == null) {
        tgt = new RepositoryPolicy();
        target.setReleases(tgt);
      }
      mergeRepositoryPolicy(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeRepository_Snapshots(
      Repository target, Repository source, boolean sourceDominant, Map<Object, Object> context) {
    RepositoryPolicy src = source.getSnapshots();
    if (src != null) {
      RepositoryPolicy tgt = target.getSnapshots();
      if (tgt == null) {
        tgt = new RepositoryPolicy();
        target.setSnapshots(tgt);
      }
      mergeRepositoryPolicy(tgt, src, sourceDominant, context);
    }
  }

  protected void mergeRepositoryBase(
      RepositoryBase target,
      RepositoryBase source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergeRepositoryBase_Id(target, source, sourceDominant, context);
    mergeRepositoryBase_Name(target, source, sourceDominant, context);
    mergeRepositoryBase_Url(target, source, sourceDominant, context);
    mergeRepositoryBase_Layout(target, source, sourceDominant, context);
  }

  protected void mergeRepositoryBase_Id(
      RepositoryBase target,
      RepositoryBase source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getId();
    if (src != null) {
      if (sourceDominant || target.getId() == null) {
        target.setId(src);
        target.setLocation("id", source.getLocation("id"));
      }
    }
  }

  protected void mergeRepositoryBase_Url(
      RepositoryBase target,
      RepositoryBase source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getUrl();
    if (src != null) {
      if (sourceDominant || target.getUrl() == null) {
        target.setUrl(src);
        target.setLocation("url", source.getLocation("url"));
      }
    }
  }

  protected void mergeRepositoryBase_Name(
      RepositoryBase target,
      RepositoryBase source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getName();
    if (src != null) {
      if (sourceDominant || target.getName() == null) {
        target.setName(src);
        target.setLocation("name", source.getLocation("name"));
      }
    }
  }

  protected void mergeRepositoryBase_Layout(
      RepositoryBase target,
      RepositoryBase source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getLayout();
    if (src != null) {
      if (sourceDominant || target.getLayout() == null) {
        target.setLayout(src);
        target.setLocation("layout", source.getLocation("layout"));
      }
    }
  }

  protected void mergeRepositoryPolicy(
      RepositoryPolicy target,
      RepositoryPolicy source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergeRepositoryPolicy_Enabled(target, source, sourceDominant, context);
    mergeRepositoryPolicy_UpdatePolicy(target, source, sourceDominant, context);
    mergeRepositoryPolicy_ChecksumPolicy(target, source, sourceDominant, context);
  }

  protected void mergeRepositoryPolicy_Enabled(
      RepositoryPolicy target,
      RepositoryPolicy source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getEnabled();
    if (src != null) {
      if (sourceDominant || target.getEnabled() == null) {
        target.setEnabled(src);
        target.setLocation("enabled", source.getLocation("enabled"));
      }
    }
  }

  protected void mergeRepositoryPolicy_UpdatePolicy(
      RepositoryPolicy target,
      RepositoryPolicy source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getUpdatePolicy();
    if (src != null) {
      if (sourceDominant || target.getUpdatePolicy() == null) {
        target.setUpdatePolicy(src);
        target.setLocation("updatePolicy", source.getLocation("updatePolicy"));
      }
    }
  }

  protected void mergeRepositoryPolicy_ChecksumPolicy(
      RepositoryPolicy target,
      RepositoryPolicy source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getChecksumPolicy();
    if (src != null) {
      if (sourceDominant || target.getChecksumPolicy() == null) {
        target.setChecksumPolicy(src);
        target.setLocation("checksumPolicy", source.getLocation("checksumPolicy"));
      }
    }
  }

  protected void mergeDependency(
      Dependency target, Dependency source, boolean sourceDominant, Map<Object, Object> context) {
    mergeDependency_GroupId(target, source, sourceDominant, context);
    mergeDependency_ArtifactId(target, source, sourceDominant, context);
    mergeDependency_Version(target, source, sourceDominant, context);
    mergeDependency_Type(target, source, sourceDominant, context);
    mergeDependency_Classifier(target, source, sourceDominant, context);
    mergeDependency_Scope(target, source, sourceDominant, context);
    mergeDependency_SystemPath(target, source, sourceDominant, context);
    mergeDependency_Optional(target, source, sourceDominant, context);
    mergeDependency_Exclusions(target, source, sourceDominant, context);
  }

  protected void mergeDependency_GroupId(
      Dependency target, Dependency source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getGroupId();
    if (src != null) {
      if (sourceDominant || target.getGroupId() == null) {
        target.setGroupId(src);
        target.setLocation("groupId", source.getLocation("groupId"));
      }
    }
  }

  protected void mergeDependency_ArtifactId(
      Dependency target, Dependency source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getArtifactId();
    if (src != null) {
      if (sourceDominant || target.getArtifactId() == null) {
        target.setArtifactId(src);
        target.setLocation("artifactId", source.getLocation("artifactId"));
      }
    }
  }

  protected void mergeDependency_Version(
      Dependency target, Dependency source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getVersion();
    if (src != null) {
      if (sourceDominant || target.getVersion() == null) {
        target.setVersion(src);
        target.setLocation("version", source.getLocation("version"));
      }
    }
  }

  protected void mergeDependency_Type(
      Dependency target, Dependency source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getType();
    if (src != null) {
      if (sourceDominant || target.getType() == null) {
        target.setType(src);
        target.setLocation("type", source.getLocation("type"));
      }
    }
  }

  protected void mergeDependency_Classifier(
      Dependency target, Dependency source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getClassifier();
    if (src != null) {
      if (sourceDominant || target.getClassifier() == null) {
        target.setClassifier(src);
        target.setLocation("classifier", source.getLocation("classifier"));
      }
    }
  }

  protected void mergeDependency_Scope(
      Dependency target, Dependency source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getScope();
    if (src != null) {
      if (sourceDominant || target.getScope() == null) {
        target.setScope(src);
        target.setLocation("scope", source.getLocation("scope"));
      }
    }
  }

  protected void mergeDependency_SystemPath(
      Dependency target, Dependency source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getSystemPath();
    if (src != null) {
      if (sourceDominant || target.getSystemPath() == null) {
        target.setSystemPath(src);
        target.setLocation("systemPath", source.getLocation("systemPath"));
      }
    }
  }

  protected void mergeDependency_Optional(
      Dependency target, Dependency source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getOptional();
    if (src != null) {
      if (sourceDominant || target.getOptional() == null) {
        target.setOptional(src);
        target.setLocation("optional", source.getLocation("optional"));
      }
    }
  }

  protected void mergeDependency_Exclusions(
      Dependency target, Dependency source, boolean sourceDominant, Map<Object, Object> context) {
    target.setExclusions(
        merge(target.getExclusions(), source.getExclusions(), sourceDominant, getExclusionKey()));
  }

  protected void mergeExclusion(
      Exclusion target, Exclusion source, boolean sourceDominant, Map<Object, Object> context) {
    mergeExclusion_GroupId(target, source, sourceDominant, context);
    mergeExclusion_ArtifactId(target, source, sourceDominant, context);
  }

  protected void mergeExclusion_GroupId(
      Exclusion target, Exclusion source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getGroupId();
    if (src != null) {
      if (sourceDominant || target.getGroupId() == null) {
        target.setGroupId(src);
        target.setLocation("groupId", source.getLocation("groupId"));
      }
    }
  }

  protected void mergeExclusion_ArtifactId(
      Exclusion target, Exclusion source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getArtifactId();
    if (src != null) {
      if (sourceDominant || target.getArtifactId() == null) {
        target.setArtifactId(src);
        target.setLocation("artifactId", source.getLocation("artifactId"));
      }
    }
  }

  protected void mergeReporting(
      Reporting target, Reporting source, boolean sourceDominant, Map<Object, Object> context) {
    mergeReporting_OutputDirectory(target, source, sourceDominant, context);
    mergeReporting_ExcludeDefaults(target, source, sourceDominant, context);
    mergeReporting_Plugins(target, source, sourceDominant, context);
  }

  protected void mergeReporting_OutputDirectory(
      Reporting target, Reporting source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getOutputDirectory();
    if (src != null) {
      if (sourceDominant || target.getOutputDirectory() == null) {
        target.setOutputDirectory(src);
        target.setLocation("outputDirectory", source.getLocation("outputDirectory"));
      }
    }
  }

  protected void mergeReporting_ExcludeDefaults(
      Reporting target, Reporting source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getExcludeDefaults();
    if (src != null) {
      if (sourceDominant || target.getExcludeDefaults() == null) {
        target.setExcludeDefaults(src);
        target.setLocation("excludeDefaults", source.getLocation("excludeDefaults"));
      }
    }
  }

  protected void mergeReporting_Plugins(
      Reporting target, Reporting source, boolean sourceDominant, Map<Object, Object> context) {
    target.setPlugins(
        merge(target.getPlugins(), source.getPlugins(), sourceDominant, getReportPluginKey()));
  }

  protected void mergeReportPlugin(
      ReportPlugin target,
      ReportPlugin source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergeConfigurationContainer(target, source, sourceDominant, context);
    mergeReportPlugin_GroupId(target, source, sourceDominant, context);
    mergeReportPlugin_ArtifactId(target, source, sourceDominant, context);
    mergeReportPlugin_Version(target, source, sourceDominant, context);
    mergeReportPlugin_ReportSets(target, source, sourceDominant, context);
  }

  protected void mergeReportPlugin_GroupId(
      ReportPlugin target,
      ReportPlugin source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getGroupId();
    if (src != null) {
      if (sourceDominant || target.getGroupId() == null) {
        target.setGroupId(src);
        target.setLocation("groupId", source.getLocation("groupId"));
      }
    }
  }

  protected void mergeReportPlugin_ArtifactId(
      ReportPlugin target,
      ReportPlugin source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getArtifactId();
    if (src != null) {
      if (sourceDominant || target.getArtifactId() == null) {
        target.setArtifactId(src);
        target.setLocation("artifactId", source.getLocation("artifactId"));
      }
    }
  }

  protected void mergeReportPlugin_Version(
      ReportPlugin target,
      ReportPlugin source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getVersion();
    if (src != null) {
      if (sourceDominant || target.getVersion() == null) {
        target.setVersion(src);
        target.setLocation("version", source.getLocation("version"));
      }
    }
  }

  protected void mergeReportPlugin_ReportSets(
      ReportPlugin target,
      ReportPlugin source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    target.setReportSets(
        merge(target.getReportSets(), source.getReportSets(), sourceDominant, getReportSetKey()));
  }

  protected void mergeReportSet(
      ReportSet target, ReportSet source, boolean sourceDominant, Map<Object, Object> context) {
    mergeConfigurationContainer(target, source, sourceDominant, context);
    mergeReportSet_Id(target, source, sourceDominant, context);
    mergeReportSet_Reports(target, source, sourceDominant, context);
  }

  protected void mergeReportSet_Id(
      ReportSet target, ReportSet source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getId();
    if (src != null) {
      if (sourceDominant || target.getId() == null) {
        target.setId(src);
        target.setLocation("id", source.getLocation("id"));
      }
    }
  }

  protected void mergeReportSet_Reports(
      ReportSet target, ReportSet source, boolean sourceDominant, Map<Object, Object> context) {
    target.setReports(merge(target.getReports(), source.getReports(), sourceDominant, e -> e));
  }

  protected void mergeDependencyManagement(
      DependencyManagement target,
      DependencyManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergeDependencyManagement_Dependencies(target, source, sourceDominant, context);
  }

  protected void mergeDependencyManagement_Dependencies(
      DependencyManagement target,
      DependencyManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    target.setDependencies(
        merge(
            target.getDependencies(),
            source.getDependencies(),
            sourceDominant,
            getDependencyKey()));
  }

  protected void mergeParent(
      Parent target, Parent source, boolean sourceDominant, Map<Object, Object> context) {
    mergeParent_GroupId(target, source, sourceDominant, context);
    mergeParent_ArtifactId(target, source, sourceDominant, context);
    mergeParent_Version(target, source, sourceDominant, context);
    mergeParent_RelativePath(target, source, sourceDominant, context);
  }

  protected void mergeParent_GroupId(
      Parent target, Parent source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getGroupId();
    if (src != null) {
      if (sourceDominant || target.getGroupId() == null) {
        target.setGroupId(src);
        target.setLocation("groupId", source.getLocation("groupId"));
      }
    }
  }

  protected void mergeParent_ArtifactId(
      Parent target, Parent source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getArtifactId();
    if (src != null) {
      if (sourceDominant || target.getArtifactId() == null) {
        target.setArtifactId(src);
        target.setLocation("artifactId", source.getLocation("artifactId"));
      }
    }
  }

  protected void mergeParent_Version(
      Parent target, Parent source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getVersion();
    if (src != null) {
      if (sourceDominant || target.getVersion() == null) {
        target.setVersion(src);
        target.setLocation("version", source.getLocation("version"));
      }
    }
  }

  protected void mergeParent_RelativePath(
      Parent target, Parent source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getRelativePath();
    if (src != null) {
      if (sourceDominant || target.getRelativePath() == null) {
        target.setRelativePath(src);
        target.setLocation("relativePath", source.getLocation("relativePath"));
      }
    }
  }

  protected void mergeOrganization(
      Organization target,
      Organization source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergeOrganization_Name(target, source, sourceDominant, context);
    mergeOrganization_Url(target, source, sourceDominant, context);
  }

  protected void mergeOrganization_Name(
      Organization target,
      Organization source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getName();
    if (src != null) {
      if (sourceDominant || target.getName() == null) {
        target.setName(src);
        target.setLocation("name", source.getLocation("name"));
      }
    }
  }

  protected void mergeOrganization_Url(
      Organization target,
      Organization source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getUrl();
    if (src != null) {
      if (sourceDominant || target.getUrl() == null) {
        target.setUrl(src);
        target.setLocation("url", source.getLocation("url"));
      }
    }
  }

  protected void mergeLicense(
      License target, License source, boolean sourceDominant, Map<Object, Object> context) {
    mergeLicense_Name(target, source, sourceDominant, context);
    mergeLicense_Url(target, source, sourceDominant, context);
    mergeLicense_Distribution(target, source, sourceDominant, context);
    mergeLicense_Comments(target, source, sourceDominant, context);
  }

  protected void mergeLicense_Name(
      License target, License source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getName();
    if (src != null) {
      if (sourceDominant || target.getName() == null) {
        target.setName(src);
        target.setLocation("name", source.getLocation("name"));
      }
    }
  }

  protected void mergeLicense_Url(
      License target, License source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getUrl();
    if (src != null) {
      if (sourceDominant || target.getUrl() == null) {
        target.setUrl(src);
        target.setLocation("url", source.getLocation("url"));
      }
    }
  }

  protected void mergeLicense_Distribution(
      License target, License source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getDistribution();
    if (src != null) {
      if (sourceDominant || target.getDistribution() == null) {
        target.setDistribution(src);
        target.setLocation("distribution", source.getLocation("distribution"));
      }
    }
  }

  protected void mergeLicense_Comments(
      License target, License source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getComments();
    if (src != null) {
      if (sourceDominant || target.getComments() == null) {
        target.setComments(src);
        target.setLocation("comments", source.getLocation("comments"));
      }
    }
  }

  protected void mergeMailingList(
      MailingList target, MailingList source, boolean sourceDominant, Map<Object, Object> context) {
    mergeMailingList_Name(target, source, sourceDominant, context);
    mergeMailingList_Subscribe(target, source, sourceDominant, context);
    mergeMailingList_Unsubscribe(target, source, sourceDominant, context);
    mergeMailingList_Post(target, source, sourceDominant, context);
    mergeMailingList_OtherArchives(target, source, sourceDominant, context);
  }

  protected void mergeMailingList_Name(
      MailingList target, MailingList source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getName();
    if (src != null) {
      if (sourceDominant || target.getName() == null) {
        target.setName(src);
        target.setLocation("name", source.getLocation("name"));
      }
    }
  }

  protected void mergeMailingList_Subscribe(
      MailingList target, MailingList source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getSubscribe();
    if (src != null) {
      if (sourceDominant || target.getSubscribe() == null) {
        target.setSubscribe(src);
        target.setLocation("subscribe", source.getLocation("subscribe"));
      }
    }
  }

  protected void mergeMailingList_Unsubscribe(
      MailingList target, MailingList source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getUnsubscribe();
    if (src != null) {
      if (sourceDominant || target.getUnsubscribe() == null) {
        target.setUnsubscribe(src);
        target.setLocation("unsubscribe", source.getLocation("unsubscribe"));
      }
    }
  }

  protected void mergeMailingList_Post(
      MailingList target, MailingList source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getPost();
    if (src != null) {
      if (sourceDominant || target.getPost() == null) {
        target.setPost(src);
        target.setLocation("post", source.getLocation("post"));
      }
    }
  }

  protected void mergeMailingList_Archive(
      MailingList target, MailingList source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getArchive();
    if (src != null) {
      if (sourceDominant || target.getArchive() == null) {
        target.setArchive(src);
        target.setLocation("archive", source.getLocation("archive"));
      }
    }
  }

  protected void mergeMailingList_OtherArchives(
      MailingList target, MailingList source, boolean sourceDominant, Map<Object, Object> context) {
    target.setOtherArchives(
        merge(target.getOtherArchives(), source.getOtherArchives(), sourceDominant, e -> e));
  }

  protected void mergeDeveloper(
      Developer target, Developer source, boolean sourceDominant, Map<Object, Object> context) {
    mergeContributor(target, source, sourceDominant, context);
    mergeDeveloper_Id(target, source, sourceDominant, context);
  }

  protected void mergeDeveloper_Id(
      Developer target, Developer source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getId();
    if (src != null) {
      if (sourceDominant || target.getId() == null) {
        target.setId(src);
        target.setLocation("id", source.getLocation("id"));
      }
    }
  }

  protected void mergeContributor(
      Contributor target, Contributor source, boolean sourceDominant, Map<Object, Object> context) {
    mergeContributor_Name(target, source, sourceDominant, context);
    mergeContributor_Email(target, source, sourceDominant, context);
    mergeContributor_Url(target, source, sourceDominant, context);
    mergeContributor_Organization(target, source, sourceDominant, context);
    mergeContributor_OrganizationUrl(target, source, sourceDominant, context);
    mergeContributor_Timezone(target, source, sourceDominant, context);
    mergeContributor_Roles(target, source, sourceDominant, context);
    mergeContributor_Properties(target, source, sourceDominant, context);
  }

  protected void mergeContributor_Name(
      Contributor target, Contributor source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getName();
    if (src != null) {
      if (sourceDominant || target.getName() == null) {
        target.setName(src);
        target.setLocation("name", source.getLocation("name"));
      }
    }
  }

  protected void mergeContributor_Email(
      Contributor target, Contributor source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getEmail();
    if (src != null) {
      if (sourceDominant || target.getEmail() == null) {
        target.setEmail(src);
        target.setLocation("email", source.getLocation("email"));
      }
    }
  }

  protected void mergeContributor_Url(
      Contributor target, Contributor source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getUrl();
    if (src != null) {
      if (sourceDominant || target.getUrl() == null) {
        target.setUrl(src);
        target.setLocation("url", source.getLocation("url"));
      }
    }
  }

  protected void mergeContributor_Organization(
      Contributor target, Contributor source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getOrganization();
    if (src != null) {
      if (sourceDominant || target.getOrganization() == null) {
        target.setOrganization(src);
        target.setLocation("organization", source.getLocation("organization"));
      }
    }
  }

  protected void mergeContributor_OrganizationUrl(
      Contributor target, Contributor source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getOrganizationUrl();
    if (src != null) {
      if (sourceDominant || target.getOrganizationUrl() == null) {
        target.setOrganizationUrl(src);
        target.setLocation("organizationUrl", source.getLocation("organizationUrl"));
      }
    }
  }

  protected void mergeContributor_Timezone(
      Contributor target, Contributor source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getTimezone();
    if (src != null) {
      if (sourceDominant || target.getTimezone() == null) {
        target.setTimezone(src);
        target.setLocation("timezone", source.getLocation("timezone"));
      }
    }
  }

  protected void mergeContributor_Roles(
      Contributor target, Contributor source, boolean sourceDominant, Map<Object, Object> context) {
    target.setRoles(merge(target.getRoles(), source.getRoles(), sourceDominant, e -> e));
  }

  protected void mergeContributor_Properties(
      Contributor target, Contributor source, boolean sourceDominant, Map<Object, Object> context) {
    Properties merged = new Properties();
    if (sourceDominant) {
      merged.putAll(target.getProperties());
      merged.putAll(source.getProperties());
    } else {
      merged.putAll(source.getProperties());
      merged.putAll(target.getProperties());
    }
    target.setProperties(merged);
    target.setLocation(
        "properties",
        InputLocation.merge(
            target.getLocation("properties"), source.getLocation("properties"), sourceDominant));
  }

  protected void mergeIssueManagement(
      IssueManagement target,
      IssueManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergeIssueManagement_Url(target, source, sourceDominant, context);
    mergeIssueManagement_System(target, source, sourceDominant, context);
  }

  protected void mergeIssueManagement_System(
      IssueManagement target,
      IssueManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getSystem();
    if (src != null) {
      if (sourceDominant || target.getSystem() == null) {
        target.setSystem(src);
        target.setLocation("system", source.getLocation("system"));
      }
    }
  }

  protected void mergeIssueManagement_Url(
      IssueManagement target,
      IssueManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getUrl();
    if (src != null) {
      if (sourceDominant || target.getUrl() == null) {
        target.setUrl(src);
        target.setLocation("url", source.getLocation("url"));
      }
    }
  }

  protected void mergeScm(
      Scm target, Scm source, boolean sourceDominant, Map<Object, Object> context) {
    mergeScm_ChildScmConnectionInheritAppendPath(target, source, sourceDominant, context);
    mergeScm_ChildScmDeveloperConnectionInheritAppendPath(target, source, sourceDominant, context);
    mergeScm_ChildScmUrlInheritAppendPath(target, source, sourceDominant, context);
    mergeScm_Url(target, source, sourceDominant, context);
    mergeScm_Connection(target, source, sourceDominant, context);
    mergeScm_DeveloperConnection(target, source, sourceDominant, context);
    mergeScm_Tag(target, source, sourceDominant, context);
  }

  protected void mergeScm_ChildScmConnectionInheritAppendPath(
      Scm target, Scm source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getChildScmConnectionInheritAppendPath();
    if (src != null) {
      if (sourceDominant || target.getChildScmConnectionInheritAppendPath() == null) {
        target.setChildScmConnectionInheritAppendPath(src);
        target.setLocation(
            "child.scm.connection.inherit.append.path",
            source.getLocation("child.scm.connection.inherit.append.path"));
      }
    }
  }

  protected void mergeScm_ChildScmDeveloperConnectionInheritAppendPath(
      Scm target, Scm source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getChildScmDeveloperConnectionInheritAppendPath();
    if (src != null) {
      if (sourceDominant || target.getChildScmDeveloperConnectionInheritAppendPath() == null) {
        target.setChildScmDeveloperConnectionInheritAppendPath(src);
        target.setLocation(
            "child.scm.developerConnection.inherit.append.path",
            source.getLocation("child.scm.developerConnection.inherit.append.path"));
      }
    }
  }

  protected void mergeScm_ChildScmUrlInheritAppendPath(
      Scm target, Scm source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getChildScmUrlInheritAppendPath();
    if (src != null) {
      if (sourceDominant || target.getChildScmUrlInheritAppendPath() == null) {
        target.setChildScmUrlInheritAppendPath(src);
        target.setLocation(
            "child.scm.url.inherit.append.path",
            source.getLocation("child.scm.url.inherit.append.path"));
      }
    }
  }

  protected void mergeScm_Url(
      Scm target, Scm source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getUrl();
    if (src != null) {
      if (sourceDominant || target.getUrl() == null) {
        target.setUrl(src);
        target.setLocation("url", source.getLocation("url"));
      }
    }
  }

  protected void mergeScm_Connection(
      Scm target, Scm source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getConnection();
    if (src != null) {
      if (sourceDominant || target.getConnection() == null) {
        target.setConnection(src);
        target.setLocation("connection", source.getLocation("connection"));
      }
    }
  }

  protected void mergeScm_DeveloperConnection(
      Scm target, Scm source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getDeveloperConnection();
    if (src != null) {
      if (sourceDominant || target.getDeveloperConnection() == null) {
        target.setDeveloperConnection(src);
        target.setLocation("developerConnection", source.getLocation("developerConnection"));
      }
    }
  }

  protected void mergeScm_Tag(
      Scm target, Scm source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getTag();
    if (src != null) {
      if (sourceDominant || target.getTag() == null) {
        target.setTag(src);
        target.setLocation("tag", source.getLocation("tag"));
      }
    }
  }

  protected void mergeCiManagement(
      CiManagement target,
      CiManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergeCiManagement_System(target, source, sourceDominant, context);
    mergeCiManagement_Url(target, source, sourceDominant, context);
    mergeCiManagement_Notifiers(target, source, sourceDominant, context);
  }

  protected void mergeCiManagement_System(
      CiManagement target,
      CiManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getSystem();
    if (src != null) {
      if (sourceDominant || target.getSystem() == null) {
        target.setSystem(src);
        target.setLocation("system", source.getLocation("system"));
      }
    }
  }

  protected void mergeCiManagement_Url(
      CiManagement target,
      CiManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getUrl();
    if (src != null) {
      if (sourceDominant || target.getUrl() == null) {
        target.setUrl(src);
        target.setLocation("url", source.getLocation("url"));
      }
    }
  }

  protected void mergeCiManagement_Notifiers(
      CiManagement target,
      CiManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    target.setNotifiers(
        merge(target.getNotifiers(), source.getNotifiers(), sourceDominant, getNotifierKey()));
  }

  protected void mergeNotifier(
      Notifier target, Notifier source, boolean sourceDominant, Map<Object, Object> context) {
    mergeNotifier_Type(target, source, sourceDominant, context);
    mergeNotifier_Address(target, source, sourceDominant, context);
    mergeNotifier_Configuration(target, source, sourceDominant, context);
    mergeNotifier_SendOnError(target, source, sourceDominant, context);
    mergeNotifier_SendOnFailure(target, source, sourceDominant, context);
    mergeNotifier_SendOnSuccess(target, source, sourceDominant, context);
    mergeNotifier_SendOnWarning(target, source, sourceDominant, context);
  }

  protected void mergeNotifier_Type(
      Notifier target, Notifier source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getType();
    if (src != null) {
      if (sourceDominant || target.getType() == null) {
        target.setType(src);
      }
    }
  }

  protected void mergeNotifier_Address(
      Notifier target, Notifier source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getAddress();
    if (src != null) {
      if (sourceDominant || target.getAddress() == null) {
        target.setAddress(src);
      }
    }
  }

  protected void mergeNotifier_Configuration(
      Notifier target, Notifier source, boolean sourceDominant, Map<Object, Object> context) {
    Properties merged = new Properties();
    if (sourceDominant) {
      merged.putAll(target.getConfiguration());
      merged.putAll(source.getConfiguration());
    } else {
      merged.putAll(source.getConfiguration());
      merged.putAll(target.getConfiguration());
    }
    target.setConfiguration(merged);
  }

  protected void mergeNotifier_SendOnError(
      Notifier target, Notifier source, boolean sourceDominant, Map<Object, Object> context) {
    if (sourceDominant) {
      target.setSendOnError(source.isSendOnError());
    }
  }

  protected void mergeNotifier_SendOnFailure(
      Notifier target, Notifier source, boolean sourceDominant, Map<Object, Object> context) {
    if (sourceDominant) {
      target.setSendOnFailure(source.isSendOnFailure());
    }
  }

  protected void mergeNotifier_SendOnSuccess(
      Notifier target, Notifier source, boolean sourceDominant, Map<Object, Object> context) {
    if (sourceDominant) {
      target.setSendOnSuccess(source.isSendOnSuccess());
    }
  }

  protected void mergeNotifier_SendOnWarning(
      Notifier target, Notifier source, boolean sourceDominant, Map<Object, Object> context) {
    if (sourceDominant) {
      target.setSendOnWarning(source.isSendOnWarning());
    }
  }

  protected void mergePrerequisites(
      Prerequisites target,
      Prerequisites source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergePrerequisites_Maven(target, source, sourceDominant, context);
  }

  protected void mergePrerequisites_Maven(
      Prerequisites target,
      Prerequisites source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getMaven();
    if (src != null) {
      if (sourceDominant || target.getMaven() == null) {
        target.setMaven(src);
        target.setLocation("maven", source.getLocation("maven"));
      }
    }
  }

  protected void mergeBuild(
      Build target, Build source, boolean sourceDominant, Map<Object, Object> context) {
    mergeBuildBase(target, source, sourceDominant, context);
    mergeBuild_SourceDirectory(target, source, sourceDominant, context);
    mergeBuild_ScriptSourceDirectory(target, source, sourceDominant, context);
    mergeBuild_TestSourceDirectory(target, source, sourceDominant, context);
    mergeBuild_OutputDirectory(target, source, sourceDominant, context);
    mergeBuild_TestOutputDirectory(target, source, sourceDominant, context);
    mergeBuild_Extensions(target, source, sourceDominant, context);
  }

  protected void mergeBuild_SourceDirectory(
      Build target, Build source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getSourceDirectory();
    if (src != null) {
      if (sourceDominant || target.getSourceDirectory() == null) {
        target.setSourceDirectory(src);
        target.setLocation("sourceDirectory", source.getLocation("sourceDirectory"));
      }
    }
  }

  protected void mergeBuild_ScriptSourceDirectory(
      Build target, Build source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getScriptSourceDirectory();
    if (src != null) {
      if (sourceDominant || target.getScriptSourceDirectory() == null) {
        target.setScriptSourceDirectory(src);
        target.setLocation("scriptSourceDirectory", source.getLocation("scriptSourceDirectory"));
      }
    }
  }

  protected void mergeBuild_TestSourceDirectory(
      Build target, Build source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getTestSourceDirectory();
    if (src != null) {
      if (sourceDominant || target.getTestSourceDirectory() == null) {
        target.setTestSourceDirectory(src);
        target.setLocation("testSourceDirectory", source.getLocation("testSourceDirectory"));
      }
    }
  }

  protected void mergeBuild_OutputDirectory(
      Build target, Build source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getOutputDirectory();
    if (src != null) {
      if (sourceDominant || target.getOutputDirectory() == null) {
        target.setOutputDirectory(src);
        target.setLocation("outputDirectory", source.getLocation("outputDirectory"));
      }
    }
  }

  protected void mergeBuild_TestOutputDirectory(
      Build target, Build source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getTestOutputDirectory();
    if (src != null) {
      if (sourceDominant || target.getTestOutputDirectory() == null) {
        target.setTestOutputDirectory(src);
        target.setLocation("testOutputDirectory", source.getLocation("testOutputDirectory"));
      }
    }
  }

  protected void mergeBuild_Extensions(
      Build target, Build source, boolean sourceDominant, Map<Object, Object> context) {
    target.setExtensions(
        merge(target.getExtensions(), source.getExtensions(), sourceDominant, getExtensionKey()));
  }

  protected void mergeExtension(
      Extension target, Extension source, boolean sourceDominant, Map<Object, Object> context) {
    mergeExtension_GroupId(target, source, sourceDominant, context);
    mergeExtension_ArtifactId(target, source, sourceDominant, context);
    mergeExtension_Version(target, source, sourceDominant, context);
  }

  protected void mergeExtension_GroupId(
      Extension target, Extension source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getGroupId();
    if (src != null) {
      if (sourceDominant || target.getGroupId() == null) {
        target.setGroupId(src);
        target.setLocation("groupId", source.getLocation("groupId"));
      }
    }
  }

  protected void mergeExtension_ArtifactId(
      Extension target, Extension source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getArtifactId();
    if (src != null) {
      if (sourceDominant || target.getArtifactId() == null) {
        target.setArtifactId(src);
        target.setLocation("artifactId", source.getLocation("artifactId"));
      }
    }
  }

  protected void mergeExtension_Version(
      Extension target, Extension source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getVersion();
    if (src != null) {
      if (sourceDominant || target.getVersion() == null) {
        target.setVersion(src);
        target.setLocation("version", source.getLocation("version"));
      }
    }
  }

  protected void mergeBuildBase(
      BuildBase target, BuildBase source, boolean sourceDominant, Map<Object, Object> context) {
    mergePluginConfiguration(target, source, sourceDominant, context);
    mergeBuildBase_DefaultGoal(target, source, sourceDominant, context);
    mergeBuildBase_FinalName(target, source, sourceDominant, context);
    mergeBuildBase_Directory(target, source, sourceDominant, context);
    mergeBuildBase_Resources(target, source, sourceDominant, context);
    mergeBuildBase_TestResources(target, source, sourceDominant, context);
    mergeBuildBase_Filters(target, source, sourceDominant, context);
  }

  protected void mergeBuildBase_DefaultGoal(
      BuildBase target, BuildBase source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getDefaultGoal();
    if (src != null) {
      if (sourceDominant || target.getDefaultGoal() == null) {
        target.setDefaultGoal(src);
        target.setLocation("defaultGoal", source.getLocation("defaultGoal"));
      }
    }
  }

  protected void mergeBuildBase_Directory(
      BuildBase target, BuildBase source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getDirectory();
    if (src != null) {
      if (sourceDominant || target.getDirectory() == null) {
        target.setDirectory(src);
        target.setLocation("directory", source.getLocation("directory"));
      }
    }
  }

  protected void mergeBuildBase_FinalName(
      BuildBase target, BuildBase source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getFinalName();
    if (src != null) {
      if (sourceDominant || target.getFinalName() == null) {
        target.setFinalName(src);
        target.setLocation("finalName", source.getLocation("finalName"));
      }
    }
  }

  protected void mergeBuildBase_Filters(
      BuildBase target, BuildBase source, boolean sourceDominant, Map<Object, Object> context) {
    target.setFilters(merge(target.getFilters(), source.getFilters(), sourceDominant, e -> e));
  }

  protected void mergeBuildBase_Resources(
      BuildBase target, BuildBase source, boolean sourceDominant, Map<Object, Object> context) {
    target.setResources(
        merge(target.getResources(), source.getResources(), sourceDominant, getResourceKey()));
  }

  protected void mergeBuildBase_TestResources(
      BuildBase target, BuildBase source, boolean sourceDominant, Map<Object, Object> context) {
    target.setTestResources(
        merge(
            target.getTestResources(),
            source.getTestResources(),
            sourceDominant,
            getResourceKey()));
  }

  protected void mergePluginConfiguration(
      PluginConfiguration target,
      PluginConfiguration source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergePluginContainer(target, source, sourceDominant, context);
    mergePluginConfiguration_PluginManagement(target, source, sourceDominant, context);
  }

  protected void mergePluginConfiguration_PluginManagement(
      PluginConfiguration target,
      PluginConfiguration source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    PluginManagement src = source.getPluginManagement();
    if (src != null) {
      PluginManagement tgt = target.getPluginManagement();
      if (tgt == null) {
        tgt = new PluginManagement();
        target.setPluginManagement(tgt);
      }
      mergePluginManagement(tgt, src, sourceDominant, context);
    }
  }

  protected void mergePluginContainer(
      PluginContainer target,
      PluginContainer source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergePluginContainer_Plugins(target, source, sourceDominant, context);
  }

  protected void mergePluginContainer_Plugins(
      PluginContainer target,
      PluginContainer source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    target.setPlugins(
        merge(target.getPlugins(), source.getPlugins(), sourceDominant, getPluginKey()));
  }

  protected void mergePluginManagement(
      PluginManagement target,
      PluginManagement source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergePluginContainer(target, source, sourceDominant, context);
  }

  protected void mergePlugin(
      Plugin target, Plugin source, boolean sourceDominant, Map<Object, Object> context) {
    mergeConfigurationContainer(target, source, sourceDominant, context);
    mergePlugin_GroupId(target, source, sourceDominant, context);
    mergePlugin_ArtifactId(target, source, sourceDominant, context);
    mergePlugin_Version(target, source, sourceDominant, context);
    mergePlugin_Extensions(target, source, sourceDominant, context);
    mergePlugin_Dependencies(target, source, sourceDominant, context);
    mergePlugin_Executions(target, source, sourceDominant, context);
  }

  protected void mergePlugin_GroupId(
      Plugin target, Plugin source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getGroupId();
    if (src != null) {
      if (sourceDominant || target.getGroupId() == null) {
        target.setGroupId(src);
        target.setLocation("groupId", source.getLocation("groupId"));
      }
    }
  }

  protected void mergePlugin_ArtifactId(
      Plugin target, Plugin source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getArtifactId();
    if (src != null) {
      if (sourceDominant || target.getArtifactId() == null) {
        target.setArtifactId(src);
        target.setLocation("artifactId", source.getLocation("artifactId"));
      }
    }
  }

  protected void mergePlugin_Version(
      Plugin target, Plugin source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getVersion();
    if (src != null) {
      if (sourceDominant || target.getVersion() == null) {
        target.setVersion(src);
        target.setLocation("version", source.getLocation("version"));
      }
    }
  }

  protected void mergePlugin_Extensions(
      Plugin target, Plugin source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getExtensions();
    if (src != null) {
      if (sourceDominant || target.getExtensions() == null) {
        target.setExtensions(src);
        target.setLocation("extensions", source.getLocation("extensions"));
      }
    }
  }

  protected void mergePlugin_Dependencies(
      Plugin target, Plugin source, boolean sourceDominant, Map<Object, Object> context) {
    target.setDependencies(
        merge(
            target.getDependencies(),
            source.getDependencies(),
            sourceDominant,
            getDependencyKey()));
  }

  protected void mergePlugin_Executions(
      Plugin target, Plugin source, boolean sourceDominant, Map<Object, Object> context) {
    target.setExecutions(
        merge(
            target.getExecutions(),
            source.getExecutions(),
            sourceDominant,
            getPluginExecutionKey()));
  }

  protected void mergeConfigurationContainer(
      ConfigurationContainer target,
      ConfigurationContainer source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergeConfigurationContainer_Inherited(target, source, sourceDominant, context);
    mergeConfigurationContainer_Configuration(target, source, sourceDominant, context);
  }

  protected void mergeConfigurationContainer_Inherited(
      ConfigurationContainer target,
      ConfigurationContainer source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getInherited();
    if (src != null) {
      if (sourceDominant || target.getInherited() == null) {
        target.setInherited(src);
        target.setLocation("inherited", source.getLocation("inherited"));
      }
    }
  }

  protected void mergeConfigurationContainer_Configuration(
      ConfigurationContainer target,
      ConfigurationContainer source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    Xpp3Dom src = (Xpp3Dom) source.getConfiguration();
    if (src != null) {
      Xpp3Dom tgt = (Xpp3Dom) target.getConfiguration();
      if (sourceDominant || tgt == null) {
        tgt = Xpp3Dom.mergeXpp3Dom(new Xpp3Dom(src), tgt);
      } else {
        tgt = Xpp3Dom.mergeXpp3Dom(tgt, src);
      }
      target.setConfiguration(tgt);
    }
  }

  protected void mergePluginExecution(
      PluginExecution target,
      PluginExecution source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    mergeConfigurationContainer(target, source, sourceDominant, context);
    mergePluginExecution_Id(target, source, sourceDominant, context);
    mergePluginExecution_Phase(target, source, sourceDominant, context);
    mergePluginExecution_Goals(target, source, sourceDominant, context);
  }

  protected void mergePluginExecution_Id(
      PluginExecution target,
      PluginExecution source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getId();
    if (src != null) {
      if (sourceDominant || target.getId() == null) {
        target.setId(src);
        target.setLocation("id", source.getLocation("id"));
      }
    }
  }

  protected void mergePluginExecution_Phase(
      PluginExecution target,
      PluginExecution source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    String src = source.getPhase();
    if (src != null) {
      if (sourceDominant || target.getPhase() == null) {
        target.setPhase(src);
        target.setLocation("phase", source.getLocation("phase"));
      }
    }
  }

  protected void mergePluginExecution_Goals(
      PluginExecution target,
      PluginExecution source,
      boolean sourceDominant,
      Map<Object, Object> context) {
    target.setGoals(merge(target.getGoals(), source.getGoals(), sourceDominant, e -> e));
  }

  protected void mergeResource(
      Resource target, Resource source, boolean sourceDominant, Map<Object, Object> context) {
    mergeFileSet(target, source, sourceDominant, context);
    mergeResource_TargetPath(target, source, sourceDominant, context);
    mergeResource_Filtering(target, source, sourceDominant, context);
    mergeResource_MergeId(target, source, sourceDominant, context);
  }

  protected void mergeResource_TargetPath(
      Resource target, Resource source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getTargetPath();
    if (src != null) {
      if (sourceDominant || target.getTargetPath() == null) {
        target.setTargetPath(src);
        target.setLocation("targetPath", source.getLocation("targetPath"));
      }
    }
  }

  protected void mergeResource_Filtering(
      Resource target, Resource source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getFiltering();
    if (src != null) {
      if (sourceDominant || target.getFiltering() == null) {
        target.setFiltering(src);
        target.setLocation("filtering", source.getLocation("filtering"));
      }
    }
  }

  protected void mergeResource_MergeId(
      Resource target, Resource source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getMergeId();
    if (src != null) {
      if (sourceDominant || target.getMergeId() == null) {
        target.setMergeId(src);
      }
    }
  }

  protected void mergeFileSet(
      FileSet target, FileSet source, boolean sourceDominant, Map<Object, Object> context) {
    mergePatternSet(target, source, sourceDominant, context);
    mergeFileSet_Directory(target, source, sourceDominant, context);
  }

  protected void mergeFileSet_Directory(
      FileSet target, FileSet source, boolean sourceDominant, Map<Object, Object> context) {
    String src = source.getDirectory();
    if (src != null) {
      if (sourceDominant || target.getDirectory() == null) {
        target.setDirectory(src);
        target.setLocation("directory", source.getLocation("directory"));
      }
    }
  }

  protected void mergePatternSet(
      PatternSet target, PatternSet source, boolean sourceDominant, Map<Object, Object> context) {
    mergePatternSet_Includes(target, source, sourceDominant, context);
    mergePatternSet_Excludes(target, source, sourceDominant, context);
  }

  protected void mergePatternSet_Includes(
      PatternSet target, PatternSet source, boolean sourceDominant, Map<Object, Object> context) {
    target.setIncludes(merge(target.getIncludes(), source.getIncludes(), sourceDominant, e -> e));
  }

  protected void mergePatternSet_Excludes(
      PatternSet target, PatternSet source, boolean sourceDominant, Map<Object, Object> context) {
    target.setExcludes(merge(target.getExcludes(), source.getExcludes(), sourceDominant, e -> e));
  }

  protected void mergeProfile(
      Profile target, Profile source, boolean sourceDominant, Map<Object, Object> context) {
    mergeModelBase(target, source, sourceDominant, context);
    // TODO
  }

  protected void mergeActivation(
      Activation target, Activation source, boolean sourceDominant, Map<Object, Object> context) {
    // TODO
  }

  @Deprecated
  protected Object getDependencyKey(Dependency dependency) {
    return dependency;
  }

  @Deprecated
  protected Object getPluginKey(Plugin plugin) {
    return plugin;
  }

  @Deprecated
  protected Object getPluginExecutionKey(PluginExecution pluginExecution) {
    return pluginExecution;
  }

  @Deprecated
  protected Object getReportPluginKey(ReportPlugin reportPlugin) {
    return reportPlugin;
  }

  @Deprecated
  protected Object getReportSetKey(ReportSet reportSet) {
    return reportSet;
  }

  @Deprecated
  protected Object getLicenseKey(License license) {
    return license;
  }

  @Deprecated
  protected Object getMailingListKey(MailingList mailingList) {
    return mailingList;
  }

  @Deprecated
  protected Object getDeveloperKey(Developer developer) {
    return developer;
  }

  @Deprecated
  protected Object getContributorKey(Contributor contributor) {
    return contributor;
  }

  @Deprecated
  protected Object getProfileKey(Profile profile) {
    return profile;
  }

  @Deprecated
  protected Object getRepositoryKey(Repository repository) {
    return getRepositoryBaseKey(repository);
  }

  @Deprecated
  protected Object getRepositoryBaseKey(RepositoryBase repositoryBase) {
    return repositoryBase;
  }

  @Deprecated
  protected Object getNotifierKey(Notifier notifier) {
    return notifier;
  }

  @Deprecated
  protected Object getResourceKey(Resource resource) {
    return resource;
  }

  @Deprecated
  protected Object getExtensionKey(Extension extension) {
    return extension;
  }

  @Deprecated
  protected Object getExclusionKey(Exclusion exclusion) {
    return exclusion;
  }

  protected KeyComputer<Dependency> getDependencyKey() {
    return d -> d;
  }

  protected KeyComputer<Plugin> getPluginKey() {
    return p -> p;
  }

  protected KeyComputer<PluginExecution> getPluginExecutionKey() {
    return e -> e;
  }

  protected KeyComputer<ReportPlugin> getReportPluginKey() {
    return p -> p;
  }

  protected KeyComputer<ReportSet> getReportSetKey() {
    return s -> s;
  }

  protected KeyComputer<License> getLicenseKey() {
    return l -> l;
  }

  protected KeyComputer<MailingList> getMailingListKey() {
    return l -> l;
  }

  protected KeyComputer<Developer> getDeveloperKey() {
    return d -> d;
  }

  protected KeyComputer<Contributor> getContributorKey() {
    return c -> c;
  }

  protected KeyComputer<Profile> getProfileKey() {
    return p -> p;
  }

  protected KeyComputer<Repository> getRepositoryKey() {
    return r -> r;
  }

  protected KeyComputer<RepositoryBase> getRepositoryBaseKey() {
    return r -> r;
  }

  protected KeyComputer<Notifier> getNotifierKey() {
    return n -> n;
  }

  protected KeyComputer<Resource> getResourceKey() {
    return r -> r;
  }

  protected KeyComputer<Extension> getExtensionKey() {
    return e -> e;
  }

  protected KeyComputer<Exclusion> getExclusionKey() {
    return e -> e;
  }

  /**
   * Use to compute keys for data structures
   *
   * @param <T>
   */
  @FunctionalInterface
  public interface KeyComputer<T> extends Function<T, Object> {}

  /** Merge two lists */
  private static <T> List<T> merge(
      List<T> tgt, List<T> src, boolean sourceDominant, KeyComputer<T> computer) {
    return merge(tgt, src, computer, (t, s) -> sourceDominant ? s : t);
  }

  private static <T> List<T> merge(
      List<T> tgt, List<T> src, KeyComputer<T> computer, BinaryOperator<T> remapping) {
    if (src.isEmpty()) {
      return tgt;
    }

    MergingList<T> list;
    if (tgt instanceof MergingList) {
      list = (MergingList<T>) tgt;
    } else {
      list = new MergingList<>(computer, src.size() + tgt.size());
      list.mergeAll(tgt, (t, s) -> s);
    }

    list.mergeAll(src, remapping);
    return list;
  }

  /**
   * Merging list
   *
   * @param <V>
   */
  private static class MergingList<V> extends AbstractList<V> {
    private final KeyComputer<V> keyComputer;
    private Map<Object, V> map;
    private List<V> list;

    MergingList(KeyComputer<V> keyComputer, int initialCapacity) {
      this.map = new LinkedHashMap<>(initialCapacity);
      this.keyComputer = keyComputer;
    }

    @Override
    public Iterator<V> iterator() {
      if (map != null) {
        return map.values().iterator();
      } else {
        return list.iterator();
      }
    }

    void mergeAll(Collection<V> vs, BinaryOperator<V> remapping) {
      if (map == null) {
        map =
            list.stream()
                .collect(
                    Collectors.toMap(keyComputer, Function.identity(), null, LinkedHashMap::new));

        list = null;
      }

      if (vs instanceof MergingList && ((MergingList<V>) vs).map != null) {
        for (Map.Entry<Object, V> e : ((MergingList<V>) vs).map.entrySet()) {
          Object key = e.getKey();
          V v = e.getValue();
          map.merge(key, v, remapping);
        }
      } else {
        for (V v : vs) {
          Object key = keyComputer.apply(v);

          map.merge(key, v, remapping);
        }
      }
    }

    @Override
    public boolean contains(Object o) {
      if (map != null) {
        return map.containsValue(o);
      } else {
        return list.contains(o);
      }
    }

    private List<V> asList() {
      if (list == null) {
        list = new ArrayList<>(map.values());
        map = null;
      }
      return list;
    }

    @Override
    public void add(int index, V element) {
      asList().add(index, element);
    }

    @Override
    public V remove(int index) {
      return asList().remove(index);
    }

    @Override
    public V get(int index) {
      return asList().get(index);
    }

    @Override
    public int size() {
      if (map != null) {
        return map.size();
      } else {
        return list.size();
      }
    }
  }
}
