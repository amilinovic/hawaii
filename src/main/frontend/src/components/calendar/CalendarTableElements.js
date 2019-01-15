import React from 'react';
import styled from 'styled-components';
import { ifProp } from 'styled-tools';

const TableTh = styled.th`
  border: ${ifProp('header', '0px', '1px solid #c0c0c0')};
  border: ${ifProp('monthName', '0px')};
  border: ${ifProp('first', '0px')};
  border-bottom: ${ifProp('header', '1px solid black')};
  text-align: center;
  padding: 5px 0px 5px 0px;
  min-width: 20px;
  font-size: 14px;
  cursor: ${ifProp('disabled', 'no-drop')};
`;

const TableTd = styled.td`
  border: ${ifProp('header', '0px', '1px solid #c0c0c0')};
  border: ${ifProp('monthName', '0px')};
  border: ${ifProp('first', '0px')};
  border-right: ${ifProp('monthName', '1px solid black', '0px')};
  border-right: ${ifProp('today', '1px')};
  border-bottom: ${ifProp('today', '1px')};
  border: ${ifProp('today', '1px solid grey')};
  text-align: center;
  padding: ${ifProp('publicHoliday', '', '5px 0px 5px 0px')};
  min-width: 20px;
  font-size: 14px;
  border-bottom: 1px solid transparent;
  border-right: 1px solid transparent;
  border-color: ${ifProp('today', 'grey')};
  background-color: ${ifProp('disabled', '#9e9e9e')};
  background-color: ${ifProp('weekend', '#E0E0E0')};
  background-color: ${ifProp('today', 'transparent')};
  border-color: ${ifProp('selected', '#e45052')};
  vertical-align: middle;
  cursor: ${ifProp('disabled', 'no-drop')};
  &:last-child {
    border-right: 1px solid black;
  }
  &:hover {
    background-color: rgba(0, 0, 0, 0.5);
  }
`;

const TableTr = styled.tr`
  &:last-child {
    ${TableTd}:not(:first-child) {
      border-bottom: 1px solid black;
    }
    ${TableTd} {
      border: ${ifProp('today', '1px solid red')};
    }
  }
  &:first-child {
    ${TableTd}:first-child {
      border-left: 0px;
    }
    ${TableTd} {
      border-top: 1px solid transparent;
    }
  }
  ${TableTd}:first-child + ${TableTd} {
    border-left: 1px solid black;
  }
`;

export const tableTd = (key, props, children) => (
  <TableTd key={key} {...props} onClick={props && props.click}>
    {children}
  </TableTd>
);

export const tableTh = (key, props, children) => (
  <TableTh key={key} {...props}>
    {children}
  </TableTh>
);

export const tableTr = (key, props, children) => (
  <TableTr key={key} {...props}>
    {children}
  </TableTr>
);
