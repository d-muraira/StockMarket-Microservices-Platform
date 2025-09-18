package service.core;

import java.io.Serializable;

/** Data Transfer Object
 * - Encapsulates data of a Trade into an object
 * - Object contains trade-related data necessary to exchange
 * - Can modify DTO to determine what data about Trades we want to expose
 * - Only send the data we want
 */
public record TradeDTO(Integer id, Integer clientId, Integer companyId,
                       String companyName, Double sharePrice,
                       Integer quantityPurchase) implements Serializable {}
