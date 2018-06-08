import React, { Component, Fragment } from 'react';
import { Switch, Route } from 'react-router-dom';
import Leave from '../components/Leave';
import LeaveHistory from '../components/LeaveHistory';
import TeamCalendar from '../components/TeamCalendar';
import ExecomCalendar from '../components/ExecomCalendar';
import Button from '../components/Button';
import Link from '../components/Link';
import TopHeader from '../components/TopHeader';

const navLinks = [
  { url: 'leave', name: 'Leave' },
  { url: 'leave-history', name: 'Leave history' },
  { url: 'team-calendar', name: 'Team calendar' },
  { url: 'execom-calendar', name: 'Execom calendar' }
];

export default class Control extends Component {
  render() {
    const linkItems = navLinks.map(navLink => (
      <Link color="white" key={navLink.url} url={navLink.url}>
        {navLink.name}
      </Link>
    ));
    return (
      <Fragment>
        {linkItems}
        <Switch>
          <Route path="/leave" component={Leave} />
          <Route path="/leave-history" component={LeaveHistory} />
          <Route path="/team-calendar" component={TeamCalendar} />
          <Route path="/execom-calendar" component={ExecomCalendar} />
        </Switch>
        <Link color="white" url="/login">
          Log out
        </Link>
        <TopHeader />
      </Fragment>
    );
  }
}
