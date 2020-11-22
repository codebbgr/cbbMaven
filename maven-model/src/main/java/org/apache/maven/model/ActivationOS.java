// =================== DO NOT EDIT THIS FILE ====================
// Generated by Modello 1.11,
// any modifications will be overwritten.
// ==============================================================

package org.apache.maven.model;

/**
 * This is an activator which will detect an operating system's attributes in order to activate its
 * profile.
 *
 * @version $Revision$ $Date$
 */
@SuppressWarnings("all")
public class ActivationOS
    implements java.io.Serializable,
        java.lang.Cloneable,
        org.apache.maven.model.InputLocationTracker {

  // --------------------------/
  // - Class/Member Variables -/
  // --------------------------/

  /**
   * The name of the operating system to be used to activate the profile. This must be an exact
   * match of the <code>${os.name}</code> Java property, such as <code>Windows XP</code>.
   */
  private String name;

  /**
   * The general family of the OS to be used to activate the profile, such as <code>windows</code>
   * or <code>unix</code>.
   */
  private String family;

  /** The architecture of the operating system to be used to activate the profile. */
  private String arch;

  /** The version of the operating system to be used to activate the profile. */
  private String version;

  /** Field locations. */
  private java.util.Map<Object, InputLocation> locations;

  /** Field location. */
  private InputLocation location;

  /** Field nameLocation. */
  private InputLocation nameLocation;

  /** Field familyLocation. */
  private InputLocation familyLocation;

  /** Field archLocation. */
  private InputLocation archLocation;

  /** Field versionLocation. */
  private InputLocation versionLocation;

  // -----------/
  // - Methods -/
  // -----------/

  /**
   * Method clone.
   *
   * @return ActivationOS
   */
  public ActivationOS clone() {
    try {
      ActivationOS copy = (ActivationOS) super.clone();

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
  } // -- ActivationOS clone()

  /**
   * Get the architecture of the operating system to be used to activate the profile.
   *
   * @return String
   */
  public String getArch() {
    return this.arch;
  } // -- String getArch()

  /**
   * Get the general family of the OS to be used to activate the profile, such as <code>windows
   * </code> or <code>unix</code>.
   *
   * @return String
   */
  public String getFamily() {
    return this.family;
  } // -- String getFamily()

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
        case "family":
          {
            return familyLocation;
          }
        case "arch":
          {
            return archLocation;
          }
        case "version":
          {
            return versionLocation;
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
   * Get the name of the operating system to be used to activate the profile. This must be an exact
   * match of the <code>${os.name}</code> Java property, such as <code>Windows XP</code>.
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
        case "family":
          {
            familyLocation = location;
            return;
          }
        case "arch":
          {
            archLocation = location;
            return;
          }
        case "version":
          {
            versionLocation = location;
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
   * Get the version of the operating system to be used to activate the profile.
   *
   * @return String
   */
  public String getVersion() {
    return this.version;
  } // -- String getVersion()

  /**
   * Set the architecture of the operating system to be used to activate the profile.
   *
   * @param arch
   */
  public void setArch(String arch) {
    this.arch = arch;
  } // -- void setArch( String )

  /**
   * Set the general family of the OS to be used to activate the profile, such as <code>windows
   * </code> or <code>unix</code>.
   *
   * @param family
   */
  public void setFamily(String family) {
    this.family = family;
  } // -- void setFamily( String )

  /**
   * Set the name of the operating system to be used to activate the profile. This must be an exact
   * match of the <code>${os.name}</code> Java property, such as <code>Windows XP</code>.
   *
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  } // -- void setName( String )

  /**
   * Set the version of the operating system to be used to activate the profile.
   *
   * @param version
   */
  public void setVersion(String version) {
    this.version = version;
  } // -- void setVersion( String )
}
