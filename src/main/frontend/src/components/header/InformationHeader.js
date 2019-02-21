import React, { Component } from 'react';
import styled from 'styled-components';
import UserInfoComponent from '../UserInfoExtended';

const UserInfoWrapper = styled.div`
  border-top: 1px solid #c0c0c3;
  box-shadow: 0px -2px 10px 2px rgba(0, 0, 0, 0.35);
  z-index: 1;
  flex: 0 0 auto;
`;

class InformationHeader extends Component {
  render() {
    return (
      <UserInfoWrapper className="d-flex align-items-center">
        <UserInfoComponent userInfo={this.props.user} />
        <div className="d-flex flex-column flex-grow-1 justify-content-center">
          <p>{this.props.user.teamName}</p>
          <p>{this.props.user.userRole}</p>
        </div>
      </UserInfoWrapper>
    );
  }
}

export default InformationHeader;
