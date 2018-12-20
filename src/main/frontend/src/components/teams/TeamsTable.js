import React from 'react';
import styled from 'styled-components';
import TeamItem from './TeamItem';

const TableContainer = styled.table`
  margin-top: 20px;
  width: 100%;
  border-spacing: 10px;
`;

const HeaderTr = styled.tr`
  width: 100%;
  border-top: 1px solid #757577;
  border-bottom: 1px solid #757577;
`;

const HeaderTh = styled.th`
  padding: 8px 0px 8px 20px;
`;

const TeamsTable = props => (
  <TableContainer>
    <thead>
      <HeaderTr>
        <HeaderTh>Name</HeaderTh>
        <HeaderTh>Members</HeaderTh>
        <HeaderTh>Approvers</HeaderTh>
      </HeaderTr>
    </thead>
    <tbody>
      {Object.keys(props.teams).map(key => (
        <TeamItem key={key} team={props.teams[key]} />
      ))}
    </tbody>
  </TableContainer>
);

export default TeamsTable;
