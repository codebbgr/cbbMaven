// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 1.11,
// any modifications will be overwritten.
// ==============================================================

package org.apache.maven.model;

/**
 * Describes the licenses for this project. This is used to generate the license page of the
 * project's web site, as well as being taken into consideration in other reporting and validation.
 * The licenses listed for the project are that of the project itself, and not of dependencies.
 *
 * @version $Revision$ $Date$
 */
@SuppressWarnings("all")
public class License
    implements java.io.Serializable,
        java.lang.Cloneable,
        org.apache.maven.model.InputLocationTracker {

  // --------------------------/
  // - Class/Member Variables -/
  // --------------------------/

  /** The full legal name of the license. */
  private String name;

  /** The official url for the license text. */
  private String url;

  /**
   * The primary method by which this project may be distributed.
   *
   * <dl>
   *   <dt>repo
   *   <dd>may be downloaded from the Maven repository
   *   <dt>manual
   *   <dd>user must manually download and install the dependency.
   * </dl>
   */
  private String distribution;

  /** Addendum information pertaining to this license. */
  private String comments;

  /** Field locations. */
  private java.util.Map<Object, InputLocation> locations;

  /** Field location. */
  private InputLocation location;

  /** Field nameLocation. */
  private InputLocation nameLocation;

  /** Field urlLocation. */
  private InputLocation urlLocation;

  /** Field distributionLocation. */
  private InputLocation distributionLocation;

  /** Field commentsLocation. */
  private InputLocation commentsLocation;

  // -----------/
  // - Methods -/
  // -----------/

  /**
   * Method clone.
   *
   * @return License
   */
  public License clone() {
    try {
      License copy = (License) super.clone();

      if (copy.locations != null) {
        copy.locations = new java.util.LinkedHashMap(copy.locations);
      }

      return copy;
    } catch (java.lang.Exception ex) {
      throw (java.lang.RuntimeException)
          new java.lang.UnsupportedOperationException(
                  getClass().getName() + " does not support clone()")
              .initCause(ex);
    }
  } // -- License clone()

  /**
   * Get addendum information pertaining to this license.
   *
   * @return String
   */
  public String getComments() {
    return this.comments;
  } // -- String getComments()

  /**
   * Get the primary method by which this project may be distributed.
   *
   * <dl>
   *   <dt>repo
   *   <dd>may be downloaded from the Maven repository
   *   <dt>manual
   *   <dd>user must manually download and install the dependency.
   * </dl>
   *
   * @return String
   */
  public String getDistribution() {
    return this.distribution;
  } // -- String getDistribution()

  /**
   * @param key
   * @return InputLocation
   */
  public InputLocation getLocation(Object key) {
    if (key instanceof String) {
      switch ((String) key) {
        case "":
          {
            return this.location;
          }
        case "name":
          {
            return nameLocation;
          }
        case "url":
          {
            return urlLocation;
          }
        case "distribution":
          {
            return distributionLocation;
          }
        case "comments":
          {
            return commentsLocation;
          }
        default:
          {
            return getOtherLocation(key);
          }
      }
    } else {
      return getOtherLocation(key);
    }
  } // -- InputLocation getLocation( Object )

  /**
   * Get the full legal name of the license.
   *
   * @return String
   */
  public String getName() {
    return this.name;
  } // -- String getName()

  /**
   * @param key
   * @param location
   */
  public void setLocation(Object key, InputLocation location) {
    if (key instanceof String) {
      switch ((String) key) {
        case "":
          {
            this.location = location;
            return;
          }
        case "name":
          {
            nameLocation = location;
            return;
          }
        case "url":
          {
            urlLocation = location;
            return;
          }
        case "distribution":
          {
            distributionLocation = location;
            return;
          }
        case "comments":
          {
            commentsLocation = location;
            return;
          }
        default:
          {
            setOtherLocation(key, location);
            return;
          }
      }
    } else {
      setOtherLocation(key, location);
    }
  } // -- void setLocation( Object, InputLocation )

  /**
   * @param key
   * @param location
   */
  public void setOtherLocation(Object key, InputLocation location) {
    if (location != null) {
      if (this.locations == null) {
        this.locations = new java.util.LinkedHashMap<Object, InputLocation>();
      }
      this.locations.put(key, location);
    }
  } // -- void setOtherLocation( Object, InputLocation )

  /**
   * @param key
   * @return InputLocation
   */
  private InputLocation getOtherLocation(Object key) {
    return (locations != null) ? locations.get(key) : null;
  } // -- InputLocation getOtherLocation( Object )

  /**
   * Get the official url for the license text.
   *
   * @return String
   */
  public String getUrl() {
    return this.url;
  } // -- String getUrl()

  /**
   * Set addendum information pertaining to this license.
   *
   * @param comments
   */
  public void setComments(String comments) {
    this.comments = comments;
  } // -- void setComments( String )

  /**
   * Set the primary method by which this project may be distributed.
   *
   * <dl>
   *   <dt>repo
   *   <dd>may be downloaded from the Maven repository
   *   <dt>manual
   *   <dd>user must manually download and install the dependency.
   * </dl>
   *
   * @param distribution
   */
  public void setDistribution(String distribution) {
    this.distribution = distribution;
  } // -- void setDistribution( String )

  /**
   * Set the full legal name of the license.
   *
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  } // -- void setName( String )

  /**
   * Set the official url for the license text.
   *
   * @param url
   */
  public void setUrl(String url) {
    this.url = url;
  } // -- void setUrl( String )
}
