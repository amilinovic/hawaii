import React, { Component } from 'react';
import styled from 'styled-components';
import { Day } from './Day';
import { Month } from './Month';

const TableHeading = styled.th`
  min-width: 20px;
  font-size: 14px;
`;

const numberOfDays = [];
for (let i = 1; i <= 31; i++) {
  numberOfDays.push(i);
}

class YearlyCalendar extends Component {
  render() {
    return (
      <table className="w-100">
        <thead>
          <tr>
            <th />
            {numberOfDays.map(day => (
              <TableHeading className="text-center py-1" key={day}>
                {day}
              </TableHeading>
            ))}
          </tr>
        </thead>
        <tbody>
          {this.props.calendar.map(month => (
            <Month key={month.name} {...month}>
              {month.days.map((day, index) =>
                day ? (
                  <Day
                    key={`${month.name}.${index}`}
                    {...day.personalDay}
                    {...day}
                  />
                ) : (
                  <Day key={`${month.name}.${index}`} disabled />
                )
              )}
            </Month>
          ))}
        </tbody>
      </table>
    );
  }
}

export default YearlyCalendar;
