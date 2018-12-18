import React, { Component, Fragment } from 'react';
import { Route, Switch } from 'react-router-dom';
import Leave from '../pages/Leave';
import InformationHeader from '../components/header/InformationHeader';
import LeaveHistory from '../pages/LeaveHistory';
import TeamCalendar from '../pages/TeamCalendar';
import ExecomCalendar from '../pages/ExecomCalendar';
import Sidebar from '../components/navigation/Sidebar';
import TopHeader from '../components/header/TopHeader';
import Dashboard from '../components/dashboard/Dashboard';

import { getUser } from '../store/selectors';
import { requestUser } from '../store/actions/userActions';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
class Panel extends Component {
  componentDidMount() {
    // Will be removed after `/me` endpoint is created, so user info could be fetched and stored in userStore
    this.props.requestUser(sessionStorage.getItem('userEmail'));
  }
  render() {
    return (
      <Fragment>
        <div className="d-flex flex-grow-1">
          <Sidebar />
          <div className="d-flex w-100 flex-column">
            <TopHeader user={this.props.user} />
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

const mapStateToProps = state => ({
  user: getUser(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestUser
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Panel);
