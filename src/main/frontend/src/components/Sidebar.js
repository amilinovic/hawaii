import React, { Component } from 'react';
import { StyledLink } from '../components/StyledLink';
import { NavLink } from 'react-router-dom';
import { StyledAside } from './StyledAside';

const navLinks = [
  { url: 'leave', name: 'Leave' },
  { url: 'leave-history', name: 'Leave history' },
  { url: 'team-calendar', name: 'Team calendar' },
  { url: 'execom-calendar', name: 'Execom calendar' },
  { url: 'login', name: 'Logout' }
].map(navLink => (
  <NavLink key={navLink.url} to={navLink.url}>
    <StyledLink
      icon="https://cdn1.iconfinder.com/data/icons/freeline/32/home_house_real_estate-512.png"
      background="transparent"
      display="block"
    >
      {navLink.name}
    </StyledLink>
  </NavLink>
));

export default class Sidebar extends Component {
  render() {
    return <StyledAside>{navLinks}</StyledAside>;
  }
}
