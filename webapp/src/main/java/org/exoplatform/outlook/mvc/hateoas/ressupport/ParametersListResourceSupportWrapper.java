package org.exoplatform.outlook.mvc.hateoas.ressupport;

import org.springframework.hateoas.ResourceSupport;

import java.util.LinkedList;
import java.util.List;

/**
 * The Parameters list resource support wrapper.
 */
public class ParametersListResourceSupportWrapper extends ResourceSupport {

  /**
   * The Name.
   */
  String      name;

  /**
   * The Parameters.
   */
  List<Param> parameters = new LinkedList<>();

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Adds parameter.
   *
   * @param name the name
   * @param parameter the parameter
   */
  public void addParameter(String name, Object parameter) {
    parameters.add(new Param(name, parameter));
  }

  /**
   * Gets parameters.
   *
   * @return the parameters
   */
  public List<Param> getParameters() {
    return parameters;
  }

  /**
   * Sets parameters.
   *
   * @param parameters the parameters
   */
  public void setParameters(List<Param> parameters) {
    this.parameters = parameters;
  }

}
