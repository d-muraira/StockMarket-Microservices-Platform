package service.utils;

import org.springframework.stereotype.Component;
import service.core.Trade;

@Component
public class TradeMapper {

    public TradeDTO toTradeDTO(Trade trade) {
        return new TradeDTO(
                trade.getId(),
                trade.getClientId(),
                trade.getCompanyId(),
                trade.getCompanyName(),
                trade.getSharePrice(),
                trade.getQuantityPurchased()
        );
    }

    // public List<TradeDTO> toTradeDTOs ...

    public Trade toTrade(TradeDTO tradeDTO) {
        return new Trade(
                tradeDTO.clientId(),
                tradeDTO.companyId(),
                tradeDTO.companyName(),
                tradeDTO.sharePrice(),
                tradeDTO.quantityPurchased()
        );
    }
}
