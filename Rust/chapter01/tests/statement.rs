use chapter01::statement::{statement, Invoice, Performance, Play};
use std::collections::HashMap;

#[test]
fn test_statement() {
    let invoice = Invoice {
        performances: vec![
            Performance {
                play_id: "hamlet",
                audience: 55,
            },
            Performance {
                play_id: "as-like",
                audience: 35,
            },
            Performance {
                play_id: "othello",
                audience: 40,
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

    let actual = statement(invoice, plays);
    let expected = "Statement for BigCo\nHamlet: $650.00 (55 seats)\nAs You Like It: $580.00 (35 seats)\nOthello: $500.00 (40 seats)\nAmount owed is $1,730.00\nYou earned 47 credits\n";
    assert_eq!(actual, expected);
}
