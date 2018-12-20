import React, { Component } from 'react';
import UserInfoComponent from '../UserInfoExtended';
import styled from 'styled-components';

const UserInfoWrapper = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  background-color: #ededed;
  border-top: 1px solid #c0c0c3;
  box-shadow: 0px -2px 10px 2px rgba(0, 0, 0, 0.35);
  z-index: 1;
  font-size: 14px;
`;

const UserTeamInfo = styled.div`
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  justify-content: center;
`;

class InformationHeader extends Component {
  render() {
    return (
      <UserInfoWrapper>
        <UserInfoComponent userInfo={this.props.user} />
        <UserTeamInfo>
          <p>{this.props.user.teamName}</p>
          <p>{this.props.user.userRole}</p>
        </UserTeamInfo>
      </UserInfoWrapper>
    );
  }
}

export default InformationHeader;
