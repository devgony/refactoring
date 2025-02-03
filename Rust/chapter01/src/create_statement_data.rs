use std::collections::HashMap;

pub struct Invoice<'a> {
    pub performances: Vec<Performance<'a>>,
    pub customer: &'a str,
}

pub struct Play<'a> {
    pub name: &'a str,
    pub _type: &'a str,
}

#[derive(Clone)]
pub struct Performance<'a> {
    // original fields
    pub play_id: &'a str,

    // enriched fields
    pub play: Option<&'a Play<'a>>,
    pub audience: i32,
    pub amount: Option<i32>,
    pub volume_credits: Option<i32>,
}

pub struct StatementData<'a> {
    pub customer: &'a str,
    pub performances: Vec<Performance<'a>>,
    pub total_amount: Option<i32>,
    pub total_volume_credits: Option<i32>,
}

pub fn create_statement_data<'a>(
    invoice: &'a Invoice<'a>,
    plays: &'a HashMap<&'a str, Play<'a>>,
) -> StatementData<'a> {
    let play_for = |a_performance: &'a Performance<'_>| -> &'a Play<'a> {
        plays.get(a_performance.play_id).unwrap()
    };

    let amount_for = |a_performance: &Performance<'_>| -> i32 {
        let mut result;
        match a_performance.play.as_ref().unwrap()._type {
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
                println!(
                    "error: unknown type: {}",
                    a_performance.play.as_ref().unwrap()._type
                );
                result = 0;
            }
        }

        result
    };

    let volume_credits_for = |a_performance: &Performance<'_>| {
        let mut result = 0;
        result += (a_performance.audience - 30).max(0);
        // add extra credit for every ten comedy attendees
        if "comedy" == a_performance.play.as_ref().unwrap()._type {
            result += a_performance.audience / 5;
        }

        result
    };

    let enrich_performance = |a_performance: &'a Performance<'a>| {
        let mut mut_performance = a_performance.clone(); // Cow
        mut_performance.play = Some(play_for(&a_performance));
        mut_performance.amount = Some(amount_for(&mut_performance));
        mut_performance.volume_credits = Some(volume_credits_for(&mut_performance));

        mut_performance
    };

    let total_amount = |data: &StatementData| -> i32 {
        data.performances
            .iter()
            .fold(0, |acc, perf| acc + perf.amount.unwrap())
    };

    let total_volume_credits = |data: &StatementData| -> i32 {
        data.performances
            .iter()
            .fold(0, |acc, perf| acc + perf.volume_credits.unwrap())
    };

    let performances = invoice
        .performances
        .iter()
        .map(enrich_performance)
        .collect();

    let mut statement_data = StatementData {
        customer: invoice.customer,
        performances,
        total_amount: None,
        total_volume_credits: None,
    };

    statement_data.total_amount = Some(total_amount(&statement_data));
    statement_data.total_volume_credits = Some(total_volume_credits(&statement_data));
    statement_data
}
