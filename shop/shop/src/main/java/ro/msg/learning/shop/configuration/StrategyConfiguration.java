package ro.msg.learning.shop.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.strategies.MostAbundantStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;
import ro.msg.learning.shop.strategies.StrategyChoiceInterface;
import ro.msg.learning.shop.strategies.StrategyEnum;

@Configuration
public class StrategyConfiguration {

    @Value("${strategy}")
    private String strategy;

    private SingleLocationStrategy singleLocationStrategy = new SingleLocationStrategy();
    private MostAbundantStrategy mostAbundantStrategy = new MostAbundantStrategy();

    @Bean
    public StrategyChoiceInterface decideStrategy() {
        if (testIfValueIsValid()) {
            StrategyEnum strategyType = StrategyEnum.valueOf(strategy.toUpperCase());
            if (strategyType == StrategyEnum.SINGLE_LOCATION) {
                return singleLocationStrategy;
            } else {
                return mostAbundantStrategy;
            }
        }
        throw new ProductsCantBeShipped("Can't get");
    }

    public boolean testIfValueIsValid() {
        for (StrategyEnum enumItem : StrategyEnum.values()) {
            if (enumItem.name().equals(strategy.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

}
