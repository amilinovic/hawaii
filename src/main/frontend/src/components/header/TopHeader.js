import React from 'react';
import styled from 'styled-components';
import ExecomLogo from '../../img/execom_logo.png';
import UserInfo from '../UserInfo';

const NavHeader = styled.header`
  background-color: #ededed;
  z-index: 3;
  flex: 0 0 auto;
`;

const LogoImage = styled.img`
  height: 30px;
`;

const NavSpan = styled.span`
  width: 100px;
`;

const TopHeader = props => (
  <NavHeader className="d-flex justify-content-between align-items-end">
    <NavSpan />
    <LogoImage src={ExecomLogo} />
    <UserInfo fullName={props.user.fullName} userEmail={props.user.email} />
  </NavHeader>
);

export default TopHeader;
