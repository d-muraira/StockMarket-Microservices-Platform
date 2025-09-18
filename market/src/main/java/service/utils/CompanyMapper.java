package service.utils;

import org.springframework.stereotype.Component;
import service.core.Company;

@Component
public class CompanyMapper {

    public CompanyDTO toDTO(Company company) {
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getSharePrice(),
                company.getNumShares()
        );
    }
}
