use std::collections::{BTreeMap, VecDeque};

type Price = u64;

#[derive(Debug, Clone)]
struct Order {
    id: u64,
    side: Side,
    order_type: OrderType,
    price: Option<Price>,
    quantity: u64,
}

#[derive(Debug, Clone, PartialEq)]
enum Side {
    BUY,
    SELL,
}

#[derive(Debug, Clone, PartialEq)]
enum OrderType {
    LIMIT,
    MARKET,
}

#[derive(Debug, Clone)]
struct PriceLevel {
    orders: VecDeque<Order>,
    total_quantity: u64,
}

impl PriceLevel {
    fn new() -> Self {
        Self {
            orders: VecDeque::new(),
            total_quantity: 0,
        }
    }
}

struct OrderBook {
    bid: BTreeMap<Price, PriceLevel>,
    ask: BTreeMap<Price, PriceLevel>,
}

impl OrderBook {
    fn new() -> Self {
        Self {
            bid: BTreeMap::new(),
            ask: BTreeMap::new(),
        }
    }

    fn add_order(&mut self, mut incoming: Order) {
        match incoming.side {
            Side::BUY => self.match_buy(&mut incoming),
            Side::SELL => self.match_sell(&mut incoming),
        }

        if incoming.quantity > 0 {
            self.insert_order(incoming);
        }
    }

    fn insert_order(&mut self, incoming: Order) {
        let price = incoming.price.expect("Limit order must have price");

        let book = match incoming.side {
            Side::BUY => &mut self.bid,
            Side::SELL => &mut self.ask,
        };

        let level = book.entry(price).or_insert_with(PriceLevel::new());
    }

    fn can_match_buy(&mut self, incoming: &Order, best_price: Price) -> bool {
        match incoming.order_type {
            OrderType::MARKET => true,
            OrderType::LIMIT => incoming.price.unwrap() >= best_price,
        }
    }

    fn match_buy(&mut self, incoming: &mut Order) {
        while let Some(best_price) = self.bid.keys().next().cloned() {
            if !self.can_match_buy(incoming, best_price) {
                break;
            }

            let level = self.bid.get_mut(&best_price).unwrap();

            while let Some(order) = level.orders.front_mut() {
                let trade_quantity = incoming.quantity.min(order.quantity);

                incoming.quantity -= trade_quantity;
                order.quantity -= trade_quantity;
                level.total_quantity -= trade_quantity;

                if order.quantity == 0 {
                    level.orders.pop_front();
                }

                if incoming.quantity == 0 {
                    break;
                }
            }

            if level.orders.is_empty() {
                self.bid.remove(&best_price);
            }

            if incoming.quantity == 0 {
                break;
            }
        }
    }

    fn can_match_sell(&mut self, incoming: &Order, best_price: Price) -> bool {
        match incoming.order_type {
            OrderType::MARKET => true,
            OrderType::LIMIT => incoming.price.unwrap() <= best_price,
        }
    }

    fn match_sell(&mut self, incoming: &mut Order) {
        while let Some(best_price) = self.ask.keys().next().cloned() {
            if !self.can_match_sell(incoming, best_price) {
                break;
            }

            let level = self.ask.get_mut(&best_price).unwrap();

            while let Some(order) = level.orders.front_mut() {
                let trade_quantity = incoming.quantity.min(order.quantity);

                incoming.quantity -= trade_quantity;
                order.quantity -= trade_quantity;
                level.total_quantity -= trade_quantity;

                if order.quantity == 0 {
                    level.orders.pop_front();
                }

                if incoming.quantity == 0 {
                    break;
                }
            }

            if level.orders.is_empty() {
                self.ask.remove(&best_price);
            }

            if incoming.quantity == 0 {
                break;
            }
        }
    }
}

fn main() {
    let mut ob = OrderBook::new();

    ob.add_order(Order {
        id: 1,
        side: Side::SELL,
        order_type: OrderType::MARKET,
        price: None,
        quantity: 10,
    });
}
