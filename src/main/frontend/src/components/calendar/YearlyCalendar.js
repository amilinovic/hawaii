import React, { Component } from 'react';
import styled from 'styled-components';
import { Month } from './Month';
import { Day } from './Day';

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
  border-spacing: 0px;
`;

const TableHeading = styled.th`
  border-bottom: 1px solid black;
  border: none;
  text-align: center;
  padding: 5px 0px 5px 0px;
  min-width: 20px;
  font-size: 14px;
`;

class YearlyCalendar extends Component {
  render() {
    return (
      <Table>
        <thead>
          <tr>
            <th />
            {this.props.calendar[0].days.map((day, index) => (
              <TableHeading key={day.date}>{index + 1}</TableHeading>
            ))}
          </tr>
        </thead>
        <tbody>
          {this.props.calendar.map(month => (
            <Month key={month.name} {...month}>
              {month.days.map(
                (day, index) =>
                  day ? (
                    <Day key={day.date} {...day.personalDay} {...day} />
                  ) : (
                    <Day key={`${month.name}.${index}`} disabled />
                  )
              )}
            </Month>
          ))}
        </tbody>
      </Table>
    );
  }
}

export default YearlyCalendar;
