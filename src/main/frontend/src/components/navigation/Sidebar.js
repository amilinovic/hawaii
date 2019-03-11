import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';
import styled from 'styled-components';
import { ifProp } from 'styled-tools';
import ExecomLogo from '../../img/execom_logo.png';
import ArchiveIcon from '../../img/icons/icons archive white (24).png';
import DashboardIcon from '../../img/icons/icons dashboard white (24).png';
import TeamCalendarIcon from '../../img/icons/icons execom calendar white (24).png';
import LeaveHistoryIcon from '../../img/icons/icons leave history white (24).png';
import LeaveIcon from '../../img/icons/icons leave white (24).png';
import LogoutIcon from '../../img/icons/icons logout white (24).png';
import AdministrationIcon from '../../img/icons/icons team calendar white (24).png';
import { MainLogo } from '../common/mainLogo';
import { MinimizeIcon } from '../common/MinimizeIcon';
import { NavigationLink } from '../common/navigationLink';

const Aside = styled.aside`
  background-color: #323234;
  width: ${ifProp('isMinimized', '45px', '300px')};
  min-width: ${ifProp('isMinimized', '45px', '300px')};
  display: flex;
  flex-direction: column;
  padding-bottom: 50px;
  font-weight: 600;
  transition: 0.2s ease-in-out;
  overflow: hidden;
`;

const LinkWrapper = styled.div`
  padding: ${ifProp('isMinimized', '0 10px', '0 50px')};
  transition: 0.2s ease-in-out;
  max-height: 55px;
  width: fit-content;
`;

const navLinks = isMinimized =>
  [
    { url: '/dashboard', name: 'Dashboard', icon: DashboardIcon },
    { url: '/leave', name: 'Leave', icon: LeaveIcon },
    { url: '/leave-history', name: 'Leave history', icon: LeaveHistoryIcon },
    { url: '/team-calendar', name: 'Team calendar', icon: TeamCalendarIcon },
    { url: '/administration', name: 'Administration', icon: AdministrationIcon }
  ].map(navLink => (
    <NavLink key={navLink.url} to={navLink.url}>
      <LinkWrapper
        isMinimized={isMinimized}
        className="align-items-center d-flex py-3"
      >
        <img src={navLink.icon} className="mr-3" alt="" />
        <NavigationLink isMinimized={isMinimized} background="transparent">
          {navLink.name}
        </NavigationLink>
      </LinkWrapper>
    </NavLink>
  ));

const controlLinks = isMinimized =>
  [
    { url: '/archive', name: 'Archive', icon: ArchiveIcon },
    { url: '/login', name: 'Log out', icon: LogoutIcon }
  ].map(navLink => (
    <NavLink key={navLink.url} to={navLink.url}>
      <LinkWrapper
        isMinimized={isMinimized}
        className="align-items-center d-flex py-3"
      >
        <img src={navLink.icon} className="mr-3" alt="" />
        <NavigationLink isMinimized={isMinimized} background="transparent">
          {navLink.name}
        </NavigationLink>
      </LinkWrapper>
    </NavLink>
  ));

export default class Sidebar extends Component {
  state = {
    isMinimized: localStorage.getItem('isMinimized')
      ? JSON.parse(localStorage.getItem('isMinimized'))
      : false
  };

  toggleSidebar = () => {
    this.setState({
      isMinimized: !this.state.isMinimized
    });

    localStorage.setItem('isMinimized', !this.state.isMinimized);
  };

  render() {
    return (
      <Aside isMinimized={this.state.isMinimized}>
        <span className="d-flex justify-content-center text-white">
          <MinimizeIcon
            click={this.toggleSidebar}
            isMinimized={this.state.isMinimized}
          />
        </span>
        <div className="justify-content-between d-flex flex-column flex-grow-1">
          <MainLogo isMinimized={this.state.isMinimized}>
            <p>Hawaii</p>
            <span>
              <img src={ExecomLogo} alt="execom" />
              HR tool
            </span>
          </MainLogo>
          <div>{navLinks(this.state.isMinimized)}</div>
          <div>{controlLinks(this.state.isMinimized)}</div>
        </div>
      </Aside>
    );
  }
}
