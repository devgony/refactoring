#[cfg(test)]
mod tests {
    use chapter01::statement::{html_statement, statement, Invoice, Performance, Play};
    use std::collections::HashMap;

    fn before_each() -> (Invoice<'static>, HashMap<&'static str, Play<'static>>) {
        let invoice = Invoice {
            performances: vec![
                Performance {
                    play_id: "hamlet",
                    play: None,
                    audience: 55,
                    amount: None,
                    volume_credits: None,
                },
                Performance {
                    play_id: "as-like",
                    play: None,
                    audience: 35,
                    amount: None,
                    volume_credits: None,
                },
                Performance {
                    play_id: "othello",
                    play: None,
                    audience: 40,
                    amount: None,
                    volume_credits: None,
                },
            ],
            customer: "BigCo",
        };

        let plays = HashMap::from([
            (
                "hamlet",
                Play {
                    name: "Hamlet",
                    _type: "tragedy",
                },
            ),
            (
                "as-like",
                Play {
                    name: "As You Like It",
                    _type: "comedy",
                },
            ),
            (
                "othello",
                Play {
                    name: "Othello",
                    _type: "tragedy",
                },
            ),
        ]);

        (invoice, plays)
    }

    #[test]
    fn test_statement() {
        let (invoice, plays) = before_each();
        let actual = statement(invoice, plays);
        let expected = "Statement for BigCo\nHamlet: $650.00 (55 seats)\nAs You Like It: $580.00 (35 seats)\nOthello: $500.00 (40 seats)\nAmount owed is $1,730.00\nYou earned 47 credits\n";
        assert_eq!(actual, expected);
    }

    #[test]
    fn test_html_statement() {
        let (invoice, plays) = before_each();
        let actual = html_statement(invoice, plays);
        let expected = "<h1>Statement for BigCo</h1>\n<table>\n<tr><th>play</th><th>seats</th><th>cost</th></tr>  <tr><td>Hamlet</td><td>55</td><td>$65,000.00</td></tr>\n  <tr><td>As You Like It</td><td>35</td><td>$58,000.00</td></tr>\n  <tr><td>Othello</td><td>40</td><td>$50,000.00</td></tr>\n</table>\n<p>Amount owed is <em>$173,000.00</em></p>\n<p>You earned <em>47</em> credits</p>\n";
        assert_eq!(actual, expected);
    }
}
