import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';
import styled from 'styled-components';
import { NavigationLink } from '../common/navigationLink';

const Aside = styled.aside`
  background-color: #3e3e48;
  order: -1;
  flex: 0 0 250px;
`;

const navLinks = [
  { url: '/leave', name: 'Leave' },
  { url: '/leave-history', name: 'Leave history' },
  { url: '/team-calendar', name: 'Team calendar' },
  { url: '/execom-calendar', name: 'Execom calendar' },
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
    return <Aside>{navLinks}</Aside>;
  }
}
