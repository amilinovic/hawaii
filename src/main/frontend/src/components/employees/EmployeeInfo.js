import React from 'react';
import styled from 'styled-components';
import Button from '../common/Button';

const EmployeeContainer = styled.div`
  width: 100%;
  padding: 5px;
  border: 1px solid #c5c5c6;
  background-color: #f9f9f9;
  border-radius: 5px;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
  margin-top: 10px;
`;

const EmployeeInfoWrapper = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-start;
  width: 30%;
  padding-left: 30;
  align-items: center;
`;

const EmployeesName = styled.p`
  font-weight: 500;
`;

const EmployeesImage = styled.div`
  width: 40px;
  height: 40px;
  border-radius: 30px;
  border: 1px solid black;
  margin-right: 40px;
  background-color: #9d9da2;
`;

const EmployeesAdditionalInfo = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  flex-grow: 1;
  padding-right: 10px;
  align-items: center;
  font-size: 14px;
`;

const EmployeeInfo = employee => {
  const { fullName, email, jobTitle, yearsOfService } = employee;
  return (
    <EmployeeContainer>
      {/* User picture should be rendered here */}

      <div
        style={{
          display: 'flex',
          flexDirection: 'row',
          justifyContent: 'space-start',
          width: '30%',
          paddingLeft: 30,
          alignItems: 'center'
        }}
      >
        <EmployeesImage />
        <EmployeesName>{fullName}</EmployeesName>
      </div>

      <EmployeesAdditionalInfo>
        <p>{jobTitle}</p>
        <p>{email}</p>
        <p>Years of Service: {yearsOfService}</p>
        <Button title="View Profile" />
      </EmployeesAdditionalInfo>
    </EmployeeContainer>
  );
};

export default EmployeeInfo;
