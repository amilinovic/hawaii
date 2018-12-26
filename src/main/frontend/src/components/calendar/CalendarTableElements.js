import React from 'react';
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

const TableTd = styled.td`
  border: ${ifProp('header', '0px', '1px solid #c0c0c0')};
  border: ${ifProp('monthName', '0px')};
  border: ${ifProp('first', '0px')};
  border-bottom: ${ifProp('header', '1px solid black')};
  border-right: ${ifProp('monthName', '1px solid black')};
  box-shadow: ${ifProp('today', '0px 0px 2px 1px rgba(0,0,0,0.75)')};
  text-align: center;
  padding: 5px 0px 5px 0px;
  min-width: 20px;
  font-size: 14px;
  background-color: ${ifProp('disabled', '#9e9e9e')};
  background-color: ${ifProp('weekend', '#cccccc')};
  background-color: ${ifProp('today', 'transparent')};
  cursor: ${ifProp('disabled', 'no-drop')};
  &:last-child {
    border-right: 1px solid black;
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
`;

export const tableTd = (key, props, children) => (
  <TableTd key={key} {...props}>
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
