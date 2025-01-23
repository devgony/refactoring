use std::collections::HashMap;

pub struct Invoice<'a> {
    pub performances: Vec<Performance<'a>>,
    pub customer: &'a str,
}

pub struct Play<'a> {
    pub name: &'a str,
    pub _type: &'a str,
}

pub struct Performance<'a> {
    pub play_id: &'a str,
    pub audience: i32,
}

pub fn statement(invoice: Invoice, plays: HashMap<String, Play>) -> String {
    let mut total_amount = 0;
    let mut volume_credits = 0;

    let mut result = format!("Statement for {}\n", invoice.customer);

    let format = |amount: f64| -> String {
        let is_negative = amount < 0.0;
        let abs_amount = amount.abs();
        let formatted = format!("{:.2}", abs_amount);
        let parts: Vec<&str> = formatted.split('.').collect();
        let int_part = parts[0];
        let decimal_part = parts[1];
        let chars: Vec<char> = int_part.chars().rev().collect();
        let mut formatted_int = String::new();

        for (i, c) in chars.iter().enumerate() {
            if i > 0 && i % 3 == 0 {
                formatted_int.push(',');
            }
            formatted_int.push(*c);
        }
        let formatted_int: String = formatted_int.chars().rev().collect();

        if is_negative {
            format!("-${}.{}", formatted_int, decimal_part)
        } else {
            format!("${}.{}", formatted_int, decimal_part)
        }
    };

    for perf in invoice.performances {
        let play = plays.get(perf.play_id).unwrap();
        let mut this_amount;

        match play._type {
            "tragedy" => {
                this_amount = 40000;
                if perf.audience > 30 {
                    this_amount += 1000 * (perf.audience - 30);
                }
            }
            "comedy" => {
                this_amount = 30000;
                if perf.audience > 20 {
                    this_amount += 10000 + 500 * (perf.audience - 20);
                }
                this_amount += 300 * perf.audience;
            }
            _ => {
                println!("error: unknown type: {}", play._type);
                this_amount = 0;
            }
        }
        // add volume credits
        volume_credits += (perf.audience - 30).max(0);
        // add extra credit for every ten comedy attendees
        if "comedy" == play._type {
            volume_credits += perf.audience / 5;
        }
        // print line for this order
        result += &format!(
            "{}: {} ({} seats)\n",
            play.name,
            (format((this_amount / 100) as f64)),
            perf.audience
        );
        total_amount += this_amount;
    }
    result += &format!("Amount owed is {}\n", format((total_amount / 100) as f64));
    result.push_str(&format!("You earned {} credits\n", volume_credits));

    result
}
