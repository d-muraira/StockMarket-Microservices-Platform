package service.core;

import java.io.Serializable;

/** Data Transfer Object
 * - Encapsulates data of a company into an object
 * - Object contains company-related data necessary to exchange
 * - Can modify DTO to determine what data about Companies we want to expose
 * - Only send the data we want
 */

// implementing serializable is unnecessary for record classes (already implemented) but adding it for clarity
public record CompanyDTO(Integer id, String name, Double sharePrice, Integer numShares) implements Serializable {}
