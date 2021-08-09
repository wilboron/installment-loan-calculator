# Installment Loan Calculator

A Clojure project designed to calculate amortization tables and installment
loans using the French amortization schedule (PRICE).

The project it's an API using Ring and Reitit library. With endpoints to return amortization
tables, rates, total interest value etc.

## Setup
It's necessary to install leiningen to run the project, you can learn more [here](https://leiningen.org/). 

With leiningen installed run to install libraries:

```shell-session
lein deps
```

Then run to start the server:

```shell-session
lein run
```

For the examples used in this readme calling the API, [HTTPie](https://httpie.io/) was used.

## Usage

Currently, the project has two main routes.

A valid payload to both routes:
```json
{
  "principal": 2000,
  "interest_rate": 5,
  "loan_term": 2,
  "start_date": "08/08/2030",
  "grace_period": 2
}
```
**Principal** is how much is loaned.  
**Interest rate** is the monthly effective rate for this loan.  
**Loan term** is the loan duration in months.  
**Start date** is **optional**, represent the day the loan will start.
If not supplied the current date is assumed.  
**Grace Period** is **optional**, represent the grace period in commercial months.
If not supplied grace period (0) is assumed.

**POST /calculate-amortization-table**  
Used to calculate only the amortization table, without more information about the loan

Return the example below:

`http POST :3000/calculate-amortization-table principal:=5000 interest_rate:=5 loan_term:=2`
```json
[
  {
    "balance": "2560.98",
    "interest": "250.00",
    "payday": "05/09/2021",
    "payment": "2689.02",
    "period": "1",
    "principal": "2439.02"
  },
  {
    "balance": "0.00",
    "interest": "128.05",
    "payday": "05/10/2021",
    "payment": "2689.02",
    "period": "2",
    "principal": "2560.98"
  }
]
```

**POST /calculate-loan**  
Used to calculate the amortization table, and more information about the loan

`http POST :3000/calculate-loan principal:=5000 interest_rate:=5 loan_term:=2`

```json
{
  "annual-interest-rate": "79.5856%",
  "balance": "5378.05",
  "capital": "5000",
  "installments": [
    {
      "balance": "2560.98",
      "interest": "250.00",
      "payday": "05/09/2021",
      "payment": "2689.02",
      "period": "1",
      "principal": "2439.02"
    },
    {
      "balance": "0.00",
      "interest": "128.05",
      "payday": "05/10/2021",
      "payment": "2689.02",
      "period": "2",
      "principal": "2560.98"
    }
  ],
  "interest": "378.05",
  "month-interest-rate": "5.0000%",
  "nominal-annual-interest-rate": "60.0000%"
}
```

## Notes
There is two ways <sup>[1](#footnote1)</sup> to calculate loan with grace period:  
* The borrower pay only the interest accrued during the grace periods each month,
then start paying the loan with the original principal borrowed.
* The borrower don't pay anything, and only starting paying after the grace period
with the interest accrued in the principal.

In this program we will use the second one.  
Meaning that if I loan 100 000,
with a 5% monthly interest with a grace period of 2 months the loan will be calculated
with 110 250 not 100 000 since the interest was accrued in these 2 months.

To keep things simple, for now the default days between each installment
is a commercial month (30 days).

Using that premise we ensure that the interest in each parcel is calculated
correctly. Since a longer/shorter period would change the interest in that
installment.

<br><br>

<a name="footnote1">1</a>: You could argue that there is a third option for loans with 
grace period where the borrower don't pay anything and start paying the loan with
the **original principal** without interest accrued.  
Considering the [time value of money](https://en.wikipedia.org/wiki/Time_value_of_money) I don't think its wise to offer this option since
it would mean a loss of profit.

## License

Copyright © 2021 William Mingardi

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
