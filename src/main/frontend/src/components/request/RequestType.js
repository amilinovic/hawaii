import React from 'react';
import styled from 'styled-components';
import BonusIcon from '../../img/icons/bonus_ss.png';
import LeaveIcon from '../../img/icons/leave_ss.png';
import SicknessIcon from '../../img/icons/sickness_ss.png';
import RequestTypeConstants from './requestTypeConstants';

const RequestContainer = styled.div`
  display: flex;
  padding: 0px 100px 0px 100px;
  flex-direction: row;
  justify-content: space-between;
`;

const LeaveWrapper = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  margin: 20px;

  p {
    padding: 10px;
  }
`;

const IconWrapper = styled.div`
  border: 1px solid black;
  padding: 10px;
  transition: ease-in 100ms;
  &:hover {
    transform: scale(1.1);
  }
`;

const RequestType = props => {
  return (
    <RequestContainer>
      <LeaveWrapper
        onClick={() => props.selectRequestType(RequestTypeConstants.LEAVE)}
      >
        <IconWrapper>
          {/* TODO vrackovic: Remove this icons when designed icons are ready (screenshots currently applied) */}
          <img src={LeaveIcon} alt="leave_icon" />
        </IconWrapper>
        <p>Leave</p>
      </LeaveWrapper>
      <LeaveWrapper
        onClick={() => props.selectRequestType(RequestTypeConstants.BONUS_DAYS)}
      >
        <IconWrapper>
          <img src={BonusIcon} alt="bonus_icon" />
        </IconWrapper>
        <p>Bonus</p>
      </LeaveWrapper>
      <LeaveWrapper
        onClick={() => props.selectRequestType(RequestTypeConstants.SICKNESS)}
      >
        <IconWrapper>
          <img src={SicknessIcon} alt="sickness_icon" />
        </IconWrapper>
        <p>Sickness</p>
      </LeaveWrapper>
    </RequestContainer>
  );
};

export default RequestType;
