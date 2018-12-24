import React, { Component } from 'react';
import styled from 'styled-components';
import { ifProp } from 'styled-tools';

const TableTh = styled.th`
  border: ${ifProp('header', '0px', '1px solid #c0c0c0')};
  border: ${ifProp('monthName', '0px')};
  border: ${ifProp('first', '0px')};
  border-bottom: ${ifProp('header', '1px solid black')};
  border-right: ${ifProp('monthName', '1px solid black')};
  text-align: center;
  padding: 5px 0px 5px 0px;
  min-width: 20px;
  font-size: 14px;
  background-color: ${ifProp('disabled', '#9e9e9e')};
  background-color: ${ifProp('weekend', '#cccccc')};
  background-color: ${ifProp('today', 'red')};
  cursor: ${ifProp('disabled', 'no-drop')};
`;

const TableTd = styled(TableTh)`
  &:last-child {
    border-right: 1px solid black;
  }
`;

const TableTr = styled.tr`
  &:last-child {
    ${TableTd}:not(:first-child) {
      border-bottom: 1px solid black;
    }
  }
`;

class Calendar extends Component {
  createTableHeader = () => {
    let thRender = [];
    thRender.push(<TableTh first key="default" />);
    for (let i = 0; i < 31; i++) {
      thRender.push(
        <TableTh header key={i + 1}>
          {i + 1}
        </TableTh>
      );
    }

    return thRender;
  };

  createTableRows = month => {
    const { name, ...monthDays } = month;

    const tdRender = [];
    tdRender.push(
      <TableTd monthName key={name}>
        {name.substring(0, 3)}
      </TableTd>
    );
    for (let i = 1; i <= 31; i++) {
      if (monthDays[i] === null) {
        tdRender.push(<TableTd key={`${i}.${name}`} disabled />);
      } else {
        tdRender.push(
          monthDays[i] && monthDays[i].today ? (
            <TableTd today key={`${i}.${name}`} />
          ) : monthDays[i].weekend ? (
            <TableTd key={`${i}.${name}`} weekend />
          ) : (
            <TableTd key={`${i}.${name}`} />
          )
        );
      }
    }

    return <React.Fragment>{tdRender}</React.Fragment>;
  };

  render() {
    return (
      <table
        style={{
          borderCollapse: 'collapse',
          width: '100%'
        }}
      >
        <thead>
          <TableTr headerRow>{this.createTableHeader()}</TableTr>
        </thead>
        <tbody>
          {this.props.calendar &&
            this.props.calendar.table.map(month => (
              <TableTr key={month.name}>{this.createTableRows(month)}</TableTr>
            ))}
        </tbody>
      </table>
    );
  }
}

export default Calendar;
