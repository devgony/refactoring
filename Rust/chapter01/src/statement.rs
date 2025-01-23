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

struct StatementData<'a> {
    customer: &'a str,
    performances: Vec<Performance<'a>>,
}

pub fn statement(invoice: Invoice, plays: HashMap<&str, Play>) -> String {
    let statement_data = StatementData {
        customer: invoice.customer,
        performances: invoice.performances,
    };
    render_plain_text(statement_data, plays)
}

fn render_plain_text(data: StatementData, plays: HashMap<&str, Play<'_>>) -> String {
    let play_for = |a_performance: &Performance<'_>| -> &Play<'_> {
        plays.get(a_performance.play_id).unwrap()
    };

    let amount_for = |a_performance: &Performance<'_>| -> i32 {
        let mut result;
        match play_for(a_performance)._type {
            "tragedy" => {
                result = 40000;
                if a_performance.audience > 30 {
                    result += 1000 * (a_performance.audience - 30);
                }
            }
            "comedy" => {
                result = 30000;
                if a_performance.audience > 20 {
                    result += 10000 + 500 * (a_performance.audience - 20);
                }
                result += 300 * a_performance.audience;
            }
            _ => {
                println!("error: unknown type: {}", play_for(a_performance)._type);
                result = 0;
            }
        }

        result
    };

    let volume_credits_for = |a_performance: &Performance<'_>| {
        let mut result = 0;
        result += (a_performance.audience - 30).max(0);
        // add extra credit for every ten comedy attendees
        if "comedy" == play_for(a_performance)._type {
            result += a_performance.audience / 5;
        }

        result
    };

    let total_amount = || -> i32 {
        let mut result = 0;
        for perf in &data.performances {
            result += amount_for(perf);
        }

        result
    };

    let total_volume_credits = || -> i32 {
        let mut volume_credits = 0;
        for perf in &data.performances {
            volume_credits += volume_credits_for(perf);
        }

        volume_credits
    };

    let mut result = format!("Statement for {}\n", data.customer);
    for perf in &data.performances {
        result += &format!(
            "{}: {} ({} seats)\n",
            play_for(perf).name,
            (usd((amount_for(perf) / 100) as f64)),
            perf.audience
        );
    }

    result += &format!("Amount owed is {}\n", usd((total_amount() / 100) as f64));
    result.push_str(&format!("You earned {} credits\n", total_volume_credits()));

    result
}

fn usd(amount: f64) -> String {
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
}
