import React, { Component, Fragment } from 'react';
import { NavLink, Switch, Route } from 'react-router-dom';
import Leave from '../components/Leave';
import LeaveHistory from '../components/LeaveHistory';
import TeamCalendar from '../components/TeamCalendar';
import ExecomCalendar from '../components/ExecomCalendar';
import { StyledLink } from '../components/StyledLink';
import InformationHeader from '../components/InformationHeader';

const navLinks = [
  { url: 'leave', name: 'Leave' },
  { url: 'leave-history', name: 'Leave history' },
  { url: 'team-calendar', name: 'Team calendar' },
  { url: 'execom-calendar', name: 'Execom calendar' }
].map(navLink => (
  <NavLink color="white" key={navLink.url} to={navLink.url}>
    <StyledLink>{navLink.name}</StyledLink>
  </NavLink>
));

export default class NavBar extends Component {
  render() {
    return (
      <Fragment>
        <InformationHeader />
        {navLinks}
        <Switch>
          <Route path="/leave" component={Leave} />
          <Route path="/leave-history" component={LeaveHistory} />
          <Route path="/team-calendar" component={TeamCalendar} />
          <Route path="/execom-calendar" component={ExecomCalendar} />
        </Switch>
        <NavLink to="/login">
          <StyledLink>Log Out</StyledLink>
        </NavLink>
      </Fragment>
    );
  }
}
