# Installment Loan Calculator

A Clojure project designed to calculate amortization tables and installment
loans using the French amortization schedule (PRICE).

The project it's an API using Ring and Reitit library. With endpoints to return amortization
tables, rates, total interest value etc.

## Usage
It's necessary to install leiningen to run the project, you can now more [here](https://leiningen.org/). 

With leiningen installed run to install libraries:

```shell-session
lein deps
```

Then run to start the server:

```shell-session
lein run
```

Currently, the project has two main routes:

**/calculate-amortization-table**
Used to calculate only the amortization table, without more information about the loan

Returning the example below:

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

**/calculate-loan**
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

To keep thing simple, for now the default days between each installment
is a commercial month (30 days), and the loan start date is the date
of the request for the simulation.


Using that premise we ensure that the interest in each parcel is calculated
correctly. Since a longer/shorter period would change the interest in that
installment.

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
