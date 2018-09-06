import React, { Component, Fragment } from 'react';
import { Route, Switch } from 'react-router-dom';
import Leave from '../../pages/Leave';
import InformationHeader from '../header/InformationHeader';
import LeaveHistory from '../../pages/LeaveHistory';
import TeamCalendar from '../../pages/TeamCalendar';
import ExecomCalendar from '../../pages/ExecomCalendar';
import Sidebar from './Sidebar';
import TopHeader from '../header/TopHeader';
import Dashboard from '../dashboard/Dashboard';

export default class NavBar extends Component {
  render() {
    return (
      <Fragment>
        <TopHeader />
        <div className="d-flex flex-grow-1">
          <Sidebar />
          <div className="d-flex w-100 flex-column">
            <InformationHeader />
            <Switch>
              <Route path="/leave" component={Leave} />
              <Route path="/leave-history" component={LeaveHistory} />
              <Route path="/team-calendar" component={TeamCalendar} />
              <Route path="/execom-calendar" component={ExecomCalendar} />
              <Route path="/dashboard" component={Dashboard} />
            </Switch>
          </div>
        </div>
      </Fragment>
    );
  }
}
