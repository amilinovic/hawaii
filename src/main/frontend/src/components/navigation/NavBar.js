import React, { Component, Fragment } from 'react';
import { NavLink, Switch, Route } from 'react-router-dom';
import Leave from '../../pages/Leave';
import LeaveHistory from '../../pages/LeaveHistory';
import TeamCalendar from '../../pages/TeamCalendar';
import ExecomCalendar from '../../pages/ExecomCalendar';
import { NavigationLink } from '../common/NavigationLink';
import Sidebar from '../navigation/Sidebar';
import TopHeader from '../header/TopHeader';

const navLinks = [
  { url: 'leave', name: 'Leave' },
  { url: 'leave-history', name: 'Leave history' },
  { url: 'team-calendar', name: 'Team calendar' },
  { url: 'execom-calendar', name: 'Execom calendar' }
].map(navLink => (
  <NavLink color="white" key={navLink.url} to={navLink.url}>
    <NavigationLink>{navLink.name}</NavigationLink>
  </NavLink>
));

export default class NavBar extends Component {
  render() {
    return (
      <Fragment>
        <Sidebar />
        <TopHeader />
        {navLinks}
        <Switch>
          <Route path="/leave" component={Leave} />
          <Route path="/leave-history" component={LeaveHistory} />
          <Route path="/team-calendar" component={TeamCalendar} />
          <Route path="/execom-calendar" component={ExecomCalendar} />
        </Switch>
        <NavLink to="/login">
          <NavigationLink>Log Out</NavigationLink>
        </NavLink>
      </Fragment>
    );
  }
}
