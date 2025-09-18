package service.utils;

import java.io.Serializable;

/** Data Transfer Object
 * - Encapsulates data of a company into an object
 * - Object contains company-related data necessary to exchange
 * - Can modify DTO to determine what data about Companies we want to expose
 * - Only send the data we want
 * - Generalizable so that controllers only need to be aware about the transfer object
 * --- opposed to CompanyA, CompanyB, etc.
 */

// implementing serializable is not necessary for record classes (built-in) but doing so for clarity
public record CompanyDTO(Integer id, String name, Double sharePrice, Integer numShares) implements Serializable {}