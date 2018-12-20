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
  padding: 20px 0px 20px 20px;
`;

const UserFullName = styled.p`
  font-size: 18px;
  font-weight: 600;
`;

export default class UserInfoComponent extends Component {
  render() {
    const { fullName, jobTitle, email } = this.props.userInfo;

    return (
      <UserInfoExtended>
        {/* Replace hardcoded image when blob storage for users is implemented on backend */}
        <UserImage
          image="https://randomuser.me/api/portraits/men/85.jpg"
          size="80px"
        />
        <span>
          <UserFullName>{fullName}</UserFullName>
          <p>{jobTitle}</p>
          <p>{email}</p>
        </span>
      </UserInfoExtended>
    );
  }
}
