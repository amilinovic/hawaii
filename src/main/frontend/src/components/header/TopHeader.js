import React from 'react';
import UserInfo from '../UserInfo';
import ExecomLogo from '../../img/execom_logo.png';
import styled from 'styled-components';

const NavHeader = styled.header`
  width: 100%;
  height: 50px;
  background-color: #ededed;
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: space-between;
  z-index: 3;
`;

const LogoImage = styled.img`
  height: 30px;
`;

const NavSpan = styled.span`
  width: 100px;
`;

const TopHeader = props => (
  <NavHeader>
    <NavSpan />
    <LogoImage src={ExecomLogo} />
    <UserInfo fullName={props.user.fullName} userEmail={props.user.email} />
  </NavHeader>
);

export default TopHeader;
