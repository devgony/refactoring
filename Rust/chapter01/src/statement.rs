use crate::create_statement_data::{create_statement_data, Invoice, Play, StatementData};
use std::collections::HashMap;
type Plays<'a> = HashMap<&'a str, Play<'a>>;

pub fn statement(invoice: Invoice, plays: Plays) -> String {
    render_plain_text(create_statement_data(&invoice, &plays))
}

pub fn html_statement(invoice: Invoice<'_>, plays: HashMap<&str, Play<'_>>) -> String {
    return render_html(create_statement_data(&invoice, &plays));
}

fn render_html(data: StatementData) -> String {
    let mut result = format!("<h1>Statement for {}</h1>\n", data.customer);
    result += "<table>\n";
    result += "<tr><th>play</th><th>seats</th><th>cost</th></tr>";
    for perf in data.performances {
        result += format!(
            "  <tr><td>{}</td><td>{}</td>",
            perf.play.as_ref().unwrap().name,
            perf.audience
        )
        .as_str();
        result += format!("<td>{}</td></tr>\n", usd(perf.amount.unwrap() as f64)).as_str();
    }
    result += "</table>\n";
    result += format!(
        "<p>Amount owed is <em>{}</em></p>\n",
        usd(data.total_amount.unwrap() as f64)
    )
    .as_str();
    result += format!(
        "<p>You earned <em>{}</em> credits</p>\n",
        data.total_volume_credits.unwrap()
    )
    .as_str();

    result
}

fn render_plain_text(data: StatementData) -> String {
    let mut result = format!("Statement for {}\n", data.customer);
    for perf in &data.performances {
        result += &format!(
            "{}: {} ({} seats)\n",
            &perf.play.as_ref().unwrap().name,
            (usd((perf.amount.unwrap() / 100) as f64)),
            perf.audience
        );
    }

    result += &format!(
        "Amount owed is {}\n",
        usd((data.total_amount.unwrap() / 100) as f64)
    );
    result.push_str(&format!(
        "You earned {} credits\n",
        data.total_volume_credits.unwrap()
    ));

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
