import React from 'react';
import styled from 'styled-components';

const MonthName = styled.td`
  font-size: 16px;
`;

export const Month = props => {
  return (
    <React.Fragment>
      <tr>
        <MonthName className="text-right py-1 pr-2">{props.name}</MonthName>
        {props.children}
      </tr>
    </React.Fragment>
  );
};
