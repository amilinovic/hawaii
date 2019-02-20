import React from 'react';
import styled from 'styled-components';

const MonthName = styled.td`
  text-align: right;
  padding: 3px 10px 3px 0px;
  font-size: 16px;
`;

export const Month = props => {
  return (
    <React.Fragment>
      <tr>
        <MonthName>{props.name}</MonthName>
        {props.children}
      </tr>
    </React.Fragment>
  );
};
