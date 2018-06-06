import React, {Component} from 'react';
import {NavLink} from 'react-router-dom';
import {Switch, Route} from 'react-router-dom';
import Leave from '../components/Leave';
import LeaveHistory from '../components/LeaveHistory';
import TeamCalendar from '../components/TeamCalendar';
import ExecomCalendar from '../components/ExecomCalendar';

export default class Control extends Component {
  render() {
    return (
      <div>
        <NavLink className="link" to="/leave">
          Leave
        </NavLink>
        <NavLink className="link" to="/leave-history">
          Leave history
        </NavLink>
        <NavLink className="link" to="/team-calendar">
          Team calendar
        </NavLink>
        <NavLink className="link" to="/execom-calendar">
          Execom calendar
        </NavLink>
        <Switch>
          <Route path="/leave" component={Leave} />
          <Route path="/leave-history" component={LeaveHistory} />
          <Route path="/team-calendar" component={TeamCalendar} />
          <Route path="/execom-calendar" component={ExecomCalendar} />
        </Switch>
        <NavLink className="link" to="/login">
          Log out
        </NavLink>
      </div>
    );
  }
}
