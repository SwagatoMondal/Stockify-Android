package com.aas.stockify.ui.views;

import com.aas.stockify.entity.Stock;
import com.firebase.ui.firestore.SnapshotParser;

public class AdapterUtil {

    public static SnapshotParser<Stock> getParser() {
        return snapshot -> {
            Stock stock = new Stock();
            stock.setId(snapshot.getId());
            stock.setName(snapshot.getString("Name"));
            stock.setSymbol(snapshot.getString("Symbol"));
            stock.setExchangeId(snapshot.getString("Exchange_ID"));
            stock.setInstrumentId(snapshot.getString("Instrument_ID"));

            Double price = snapshot.getDouble("Price");
            if (price == null) {
                price = snapshot.getDouble("CurrentPrice");
            }
            stock.setPrice(price == null ? 0.0f : price.floatValue());

            stock.setExchange(snapshot.getString("Exchange"));

            Double percent = snapshot.getDouble("LTPChangePerc");
            stock.setLtpChangePercentage(percent == null ? 0.0f : percent.floatValue());

            Double returns = snapshot.getDouble("ExpectedReturnsPerc");
            stock.setReturns(returns == null ? 0.0f : returns.floatValue());

            Double target = snapshot.getDouble("TargetPrice");
            stock.setTargetPrice(target == null ? 0.0f : target.floatValue());

            return stock;
        };
    }
}
