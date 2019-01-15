import React, { Component } from 'react';
import { UserImage } from './common/userImage';
import styled from 'styled-components';

const UserInfoExtended = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-start;
  flex-grow: 1;
  height: fit-content;
  padding: 10px 0px 10px 20px;
`;

export default class UserInfoComponent extends Component {
  render() {
    const { fullName, jobTitle, email } = this.props.userInfo;

    return (
      <UserInfoExtended>
        {/* TODO vrackovic: Replace hardcoded image when blob storage for users is implemented on backend */}
        <UserImage
          image="https://randomuser.me/api/portraits/men/85.jpg"
          size="100px"
        />
        <span>
          <h3>{fullName}</h3>
          <h5>{jobTitle}</h5>
          <h5>{email}</h5>
        </span>
      </UserInfoExtended>
    );
  }
}
