use std::collections::{BTreeMap, VecDeque};

type Price = u64;
type Quantity = u64;
type OrderId = u64;

#[derive(Debug, Clone, PartialEq)]
enum Side {
    Buy,
    Sell,
}

#[derive(Debug, Clone, PartialEq)]
enum OrderType {
    LIMIT,
    MARKET,
}

#[derive(Debug, Clone)]
struct Order {
    id: OrderId,
    side: Side,
    order_type: OrderType,
    price: Option<Price>,
    quantity: Quantity,
}

struct OrderBook {
    bids: BTreeMap<Price, VecDeque<Order>>,
    asks: BTreeMap<Price, VecDeque<Order>>,
}

impl OrderBook {
    fn new() -> Self {
        Self {
            bids: BTreeMap::new(),
            asks: BTreeMap::new(),
        }
    }

    fn add_order(&mut self, mut incoming: Order) {
        match incoming.side {
            Side::Buy => self.match_buy(&mut incoming),
            Side::Sell => self.match_sell(&mut incoming),
        }

        if incoming.quantity > 0 {
            self.insert_order(incoming);
        }
    }

    fn buy_can_match(&self, incoming: &Order, best_ask: Price) -> bool {
        match incoming.order_type {
            OrderType::MARKET => true,
            OrderType::LIMIT => incoming.price.unwrap() >= best_ask,
        }
    }

    fn sell_can_match(&self, incoming: &Order, best_bid: Price) -> bool {
        match incoming.order_type {
            OrderType::MARKET => true,
            OrderType::LIMIT => incoming.price.unwrap() <= best_bid,
        }
    }

    fn match_buy(&mut self, incoming: &mut Order) {
        while let Some((&best_ask_price, _)) = self.asks.iter().next() {
            if !self.buy_can_match(incoming, best_ask_price) {
                break;
            }

            let order = self.asks.get_mut(&best_ask_price).unwrap();

            while let Some(mut top_order) = order.pop_front() {
                let traded_qty = incoming.quantity.min(top_order.quantity);

                println!("TRADE: {} @ {}", traded_qty, best_ask_price);

                incoming.quantity -= traded_qty;
                top_order.quantity -= traded_qty;

                if top_order.quantity > 0 {
                    order.push_front(top_order)
                }

                if incoming.quantity == 0 {
                    break;
                }
            }

            if order.is_empty() {
                self.asks.remove(&best_ask_price);
            }

            if incoming.quantity == 0 {
                break;
            }
        }
    }

    fn match_sell(&mut self, incoming: &mut Order) {
        while let Some((&best_bid_price, _)) = self.bids.iter().next() {
            if !self.sell_can_match(incoming, best_bid_price) {
                break;
            }

            let order = self.bids.get_mut(&best_bid_price).unwrap();

            while let Some(mut top_order) = order.pop_front() {
                let traded_qty = incoming.quantity.min(top_order.quantity);

                println!("TRADE: {} @ {}", traded_qty, best_bid_price);

                incoming.quantity -= traded_qty;
                top_order.quantity -= traded_qty;

                if top_order.quantity > 0 {
                    order.push_front(top_order);
                }

                if incoming.quantity == 0 {
                    break;
                }
            }

            if order.is_empty() {
                self.bids.remove(&best_bid_price);
            }

            if incoming.quantity == 0 {
                break;
            }
        }
    }

    fn insert_order(&mut self, order: Order) {
        let price = order.price.expect("Limit order must have a price");

        let book = match order.side {
            Side::Buy => &mut self.bids,
            Side::Sell => &mut self.asks,
        };

        book.entry(price)
            .or_insert_with(VecDeque::new)
            .push_back(order);
    }
}

fn main() {
    let mut ob = OrderBook::new();

    ob.add_order(Order {
        id: 1,
        side: Side::Buy,
        order_type: OrderType::LIMIT,
        price: Some(100),
        quantity: 5,
    });

    ob.add_order(Order {
        id: 2,
        side: Side::Sell,
        order_type: OrderType::MARKET,
        price: None,
        quantity: 3,
    });

    ob.add_order(Order {
        id: 3,
        side: Side::Sell,
        order_type: OrderType::LIMIT,
        price: Some(99),
        quantity: 3,
    });

    println!("OrderBook bids state {:?}", ob.bids);
    println!("OrderBook asks state {:?}", ob.asks);
}
