import React, { Component } from 'react';
import styled from 'styled-components';
import { Day } from './Day';
import { Month } from './Month';

const TableHeading = styled.th`
  min-width: 20px;
  font-size: 14px;
`;

class YearlyCalendar extends Component {
  render() {
    return (
      <table className="w-100">
        <thead>
          <tr>
            <th />
            {this.props.calendar[0].days.map((day, index) => (
              <TableHeading className="text-center py-1" key={index}>
                {index + 1}
              </TableHeading>
            ))}
          </tr>
        </thead>
        <tbody>
          {this.props.calendar.map(month => (
            <Month key={month.name} {...month}>
              {month.days.map(
                (day, index) =>
                  day ? (
                    <Day key={index} {...day.personalDay} {...day} />
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
