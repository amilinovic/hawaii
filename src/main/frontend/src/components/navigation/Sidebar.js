import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';
import styled from 'styled-components';
import { NavigationLink } from '../common/navigationLink';
import { MainLogo } from '../common/mainLogo';
import ExecomLogo from '../../img/execom_logo.png';

const Aside = styled.aside`
  background-color: #323234;
  order: -1;
  width: 250px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 50px 0px;
  font-weight: 600;
`;
// TODO vrackovic: Change implementation of navLinks and controlLinks

const navLinks = [
  { url: '/leave', name: 'Leave' },
  { url: '/leave-history', name: 'Leave history' },
  { url: '/team-calendar', name: 'Team calendar' },
  { url: '/execom-calendar', name: 'Execom calendar' },
  { url: '/dashboard', name: 'Dashboard' }
].map(navLink => (
  <NavLink key={navLink.url} to={navLink.url}>
    <NavigationLink
      // TODO change mocked data with actual data
      icon="https://cdn1.iconfinder.com/data/icons/freeline/32/home_house_real_estate-512.png"
      background="transparent"
      display="block"
    >
      {navLink.name}
    </NavigationLink>
  </NavLink>
));

const controlLinks = [
  { url: '/archive', name: 'Archive' },
  { url: '/login', name: 'Log out' }
].map(navLink => (
  <NavLink key={navLink.url} to={navLink.url}>
    <NavigationLink
      // TODO change mocked data with actual data
      icon="https://cdn1.iconfinder.com/data/icons/freeline/32/home_house_real_estate-512.png"
      background="transparent"
      display="block"
    >
      {navLink.name}
    </NavigationLink>
  </NavLink>
));

export default class Sidebar extends Component {
  render() {
    return (
      <Aside>
        <MainLogo>
          <p>Hawaii</p>
          <span>
            <img src={ExecomLogo} alt="execom" />
            HR tool
          </span>
        </MainLogo>
        <div>{navLinks}</div>
        <div>{controlLinks}</div>
      </Aside>
    );
  }
}
