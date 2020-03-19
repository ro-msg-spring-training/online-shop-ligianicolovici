package ro.msg.learning.shop.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.strategies.*;

import java.util.Objects;

@Configuration
public class StrategyConfiguration {

    @Value("${strategy}")
    private String strategy;

    private SingleLocationStrategy singleLocationStrategy = new SingleLocationStrategy();
    private MostAbundantStrategy mostAbundantStrategy = new MostAbundantStrategy();
    private GreedyStrategy greedyStrategy = new GreedyStrategy();

    @Bean
    public StrategyChoiceInterface decideStrategy() {
        if (testIfValueIsValid()) {
            StrategyEnum strategyType = StrategyEnum.valueOf(strategy.toUpperCase());
            if (strategyType == StrategyEnum.SINGLE_LOCATION) {
                return singleLocationStrategy;
            } else {
                if (strategyType == StrategyEnum.MOST_ABUNDANT) {
                    return mostAbundantStrategy;
                } else {
                    return greedyStrategy;
                }
            }
        }
        throw new ProductsCantBeShipped("Can't get place your order. We are sorry!");
    }

    public boolean testIfValueIsValid() {
        for (StrategyEnum enumItem : StrategyEnum.values()) {
            if (Objects.equals(strategy.toUpperCase(), enumItem.name())) return true;
        }
        return false;
    }

}
