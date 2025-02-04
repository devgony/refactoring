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

pub trait PerformanceCalculator<'a> {
    fn performance(&self) -> &'a Performance<'a>;
    fn play(&self) -> &'a Play<'a>;
    fn amount(&self) -> i32 {
        unimplemented!("subclass responsibility")
    }
    fn volume_credits(&self) -> i32 {
        self.super_volume_credits()
    }
    /// Only for trait default impl
    fn super_volume_credits(&self) -> i32 {
        (self.performance().audience - 30).max(0)
    }
}

/// factory method to create a performance calculator with the concrete type
fn create_performance_calculator<'a>(
    performance: &'a Performance<'a>,
    play: &'a Play<'a>,
) -> Box<dyn PerformanceCalculator<'a> + 'a> {
    match play._type {
        "tragedy" => Box::new(TragedyCalculator { performance, play }),
        "comedy" => Box::new(ComedyCalculator { performance, play }),
        _ => panic!("unknown type: {}", play._type),
    }
}

struct TragedyCalculator<'a> {
    performance: &'a Performance<'a>,
    play: &'a Play<'a>,
}

impl<'a> PerformanceCalculator<'a> for TragedyCalculator<'a> {
    fn performance(&self) -> &'a Performance<'a> {
        self.performance
    }

    fn play(&self) -> &'a Play<'a> {
        self.play
    }

    fn amount(&self) -> i32 {
        let mut result = 40000;
        if self.performance.audience > 30 {
            result += 1000 * (self.performance.audience - 30);
        }

        result
    }
}

struct ComedyCalculator<'a> {
    performance: &'a Performance<'a>,
    play: &'a Play<'a>,
}

impl<'a> PerformanceCalculator<'a> for ComedyCalculator<'a> {
    fn performance(&self) -> &'a Performance<'a> {
        self.performance
    }
    fn play(&self) -> &'a Play<'a> {
        self.play
    }
    fn amount(&self) -> i32 {
        let mut result = 30000;
        if self.performance.audience > 20 {
            result += 10000 + 500 * (self.performance.audience - 20);
        }
        result += 300 * self.performance.audience;

        result
    }
    fn volume_credits(&self) -> i32 {
        self.super_volume_credits() + (self.performance.audience / 5)
    }
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

    let enrich_performance = |a_performance: &'a Performance<'a>| {
        let calculator = create_performance_calculator(a_performance, play_for(a_performance));

        let mut mut_performance = a_performance.clone(); // Cow
        mut_performance.play = Some(calculator.play());
        mut_performance.amount = Some(calculator.amount());
        mut_performance.volume_credits = Some(calculator.volume_credits());

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
