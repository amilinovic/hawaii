import React from 'react';
import { NavLink } from 'react-router-dom';
import styled from 'styled-components';
import Button from '../common/Button';

const TableText = styled.span`
  text-overflow: ellipsis;
  overflow: hidden;
`;

const EmployeesImage = styled.div`
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 1px solid black;
  margin-right: 40px;
  background-color: #9d9da2;
  min-width: 40px;
`;

const TableRow = styled.div`
  width: ${props => (props.width ? props.width : '10%')};
  flex-grow: 1;
  display: flex;
`;

const EmployeeInfo = employee => {
  const { id, fullName, email, jobTitle, yearsOfService } = employee;
  return (
    <div className="align-items-center d-flex justify-content-between mb-3">
      <EmployeesImage />
      <TableRow width="15%">
        <TableText>{fullName}</TableText>
      </TableRow>
      <TableRow>
        <TableText>{jobTitle}</TableText>
      </TableRow>
      <TableRow>
        <TableText>{email}</TableText>
      </TableRow>
      <TableRow>
        <TableText>Years of Service: {yearsOfService}</TableText>
      </TableRow>
      <Button title="View Profile" />
      <NavLink to={`/employee/${email}/edit`}>Edit</NavLink>
    </div>
  );
};

export default EmployeeInfo;
