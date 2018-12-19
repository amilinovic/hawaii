import React, { Component } from 'react';
import styled from 'styled-components';

const UserInfoContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 12px;
  padding: 5px;
`;

export default class UserInfo extends Component {
  render() {
    return (
      <UserInfoContainer>
        <p>{this.props.fullName}</p>
        <p>{this.props.userEmail}</p>
      </UserInfoContainer>
    );
  }
}
